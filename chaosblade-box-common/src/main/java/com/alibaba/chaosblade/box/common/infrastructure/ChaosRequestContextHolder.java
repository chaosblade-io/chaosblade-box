package com.alibaba.chaosblade.box.common.infrastructure;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import org.springframework.core.NamedThreadLocal;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author haibin
 *
 *
 */
public class ChaosRequestContextHolder {

    private static final ThreadLocal<ChaosApplicationContext> applicationContextThreadLocal =
        new NamedThreadLocal<ChaosApplicationContext>("chaos-application-context");

    public static void resetApplicationContextContext() {
        applicationContextThreadLocal.remove();
    }

    public static void setApplicationContext(ChaosApplicationContext chaosApplicationContext) {
        if (chaosApplicationContext == null) {
            resetApplicationContextContext();
        }
        applicationContextThreadLocal.set(chaosApplicationContext);
    }

    public static Optional<ChaosApplicationContext> getApplicationContext() {
        return Optional.ofNullable(applicationContextThreadLocal.get());
    }

    public static Optional<ChaosUser> getLoginUser() {
        return getApplicationContext().map(new Function<ChaosApplicationContext, ChaosUser>() {
            @Override
            public ChaosUser apply(ChaosApplicationContext chaosApplicationContext) {
                return chaosApplicationContext.getLoginUser();
            }
        });
    }
}
