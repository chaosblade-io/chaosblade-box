/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.service.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskLogDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author yefei
 */
@Slf4j
public class DBLoggerAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final static String LONGS_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS}, %green(%-5level), %red([%thread]) %boldMagenta(%logger{72}) - %msg%n";

    private final static String APPENDER_NAME = "DBLoggerAppender";

    private final static PatternLayoutEncoder encoder;

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern(LONGS_PATTERN);
        encoder.start();
    }

    private static ApplicationContext applicationContext;

    @Override
    public void append(ILoggingEvent iLoggingEvent) {
        String message = new String(encoder.encode(iLoggingEvent));

        ExperimentTaskLogRepository logRepository = applicationContext.getBean(ExperimentTaskLogRepository.class);
        if (logRepository == null) {
            log.warn("record experiment task id fail");
        }

        Long taskId = null;
        Object[] argumentArray = iLoggingEvent.getArgumentArray();
        if (ArrayUtil.isNotEmpty(argumentArray)) {
            if (argumentArray[0] instanceof Long) {
                taskId = (Long) argumentArray[0];
            }
        }
        // insert db
        logRepository.insert(ExperimentTaskLogDO.builder()
                .taskId(taskId)
                .logDate(DateUtil.date())
                .content(message)
                .build());
    }

    public static void configure(LoggerContext lc, Class<?> clazz, ApplicationContext applicationContext) {
        DBLoggerAppender.applicationContext = applicationContext;

        StatusManager sm = lc.getStatusManager();
        if (sm != null) {
            sm.add(new InfoStatus("Setting up default configuration.", lc));
        }
        ch.qos.logback.classic.Logger logger = lc.getLogger(clazz);
        if (logger.getAppender(APPENDER_NAME) == null) {
            // thread safety
            DBLoggerAppender DBLoggerAppender = new DBLoggerAppender();
            DBLoggerAppender.setContext(lc);
            DBLoggerAppender.setName(APPENDER_NAME);
            DBLoggerAppender.start();

            logger.setAdditive(true);
            logger.addAppender(DBLoggerAppender);
        }
    }
}
