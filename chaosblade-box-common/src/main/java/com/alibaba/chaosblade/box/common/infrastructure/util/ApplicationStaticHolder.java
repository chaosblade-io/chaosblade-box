package com.alibaba.chaosblade.box.common.infrastructure.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationStaticHolder implements ApplicationContextAware {

    private static ApplicationContext globalApplicationContext;

    public static ApplicationContext get() {
        return globalApplicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        globalApplicationContext = applicationContext;
    }
}
