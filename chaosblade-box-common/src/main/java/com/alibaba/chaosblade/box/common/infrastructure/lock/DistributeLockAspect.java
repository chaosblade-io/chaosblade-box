package com.alibaba.chaosblade.box.common.infrastructure.lock;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockingTaskExecutor.TaskWithResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author haibin.lhb
 *
 *
 */
@Aspect
@Configuration
@Slf4j
public class DistributeLockAspect {

    private static String defaultLockAtMostFor = "PT5M";
    private static String defaultLockAtLeastFor = "PT0S";

    public static final StringToDurationConverter durationConverter = StringToDurationConverter.INSTANCE;

    @Autowired
    private LockingTaskExecutor lockingTaskExecutor;

    @Around("@annotation(com.alibaba.chaosblade.box.common.infrastructure.lock.DistributeLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        DistributeLock mapping = methodSignature.getMethod().getAnnotation(DistributeLock.class);
        return lockingTaskExecutor.executeWithLock(new TaskWithResult<Object>() {
            @Override
            public Object call() throws Throwable {
                return joinPoint.proceed(joinPoint.getArgs());
            }
        }, extractFromLock(mapping)).getResult();
    }

    private LockConfiguration extractFromLock(DistributeLock distributeLock) {
        String mostTime = Strings.isNullOrEmpty(distributeLock.lockAtMostFor()) ? defaultLockAtMostFor
            : distributeLock.lockAtMostFor();
        String leastTime = Strings.isNullOrEmpty(distributeLock.lockAtLeastFor()) ? defaultLockAtLeastFor
            : distributeLock.lockAtLeastFor();
        return new LockConfiguration(distributeLock.name(), durationConverter.convert(mostTime),
            durationConverter.convert(leastTime));
    }

    public static class StringToDurationConverter implements Converter<String, Duration> {

        static final StringToDurationConverter INSTANCE = new StringToDurationConverter();

        private static final Pattern ISO8601 = Pattern.compile("^[\\+\\-]?P.*$");

        private static final Pattern SIMPLE = Pattern.compile("^([\\+\\-]?\\d+)([a-zA-Z]{0,2})$");

        private static final Map<String, ChronoUnit> UNITS;

        static {
            Map<String, ChronoUnit> units = new LinkedHashMap<>();
            units.put("us", ChronoUnit.MICROS);
            units.put("ns", ChronoUnit.NANOS);
            units.put("ms", ChronoUnit.MILLIS);
            units.put("s", ChronoUnit.SECONDS);
            units.put("m", ChronoUnit.MINUTES);
            units.put("h", ChronoUnit.HOURS);
            units.put("d", ChronoUnit.DAYS);
            units.put("", ChronoUnit.MILLIS);
            UNITS = Collections.unmodifiableMap(units);
        }

        @Override
        public Duration convert(String source) {
            try {
                if (ISO8601.matcher(source).matches()) {
                    return Duration.parse(source);
                }
                Matcher matcher = SIMPLE.matcher(source);
                Assert.state(matcher.matches(), "'" + source + "' is not a valid duration");
                long amount = Long.parseLong(matcher.group(1));
                ChronoUnit unit = getUnit(matcher.group(2));
                return Duration.of(amount, unit);
            } catch (Exception ex) {
                throw new IllegalStateException("'" + source + "' is not a valid duration", ex);
            }
        }

        private ChronoUnit getUnit(String value) {
            ChronoUnit unit = UNITS.get(value.toLowerCase());
            Assert.state(unit != null, "Unknown unit '" + value + "'");
            return unit;
        }

    }

}
