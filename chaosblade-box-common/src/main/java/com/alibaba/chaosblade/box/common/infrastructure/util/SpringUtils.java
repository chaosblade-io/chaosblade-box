package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext globalApplicationContext;

    public static ApplicationContext getApplicationContext() {
        return globalApplicationContext;
    }

    public static <T> List<T> getBeans(ApplicationContext applicationContext, Class<T> tClass) {
        String[] beanNames = applicationContext.getBeanNamesForType(tClass);
        if (beanNames.length == 0) {
            return Lists.newArrayList();
        }

        List<T> beans = new ArrayList<>();
        for (String beanName : beanNames) {
            beans.add(applicationContext.getBean(beanName, tClass));
        }
        AnnotationAwareOrderComparator.sort(beans);
        return beans;
    }

    public static <T> T getBean(ApplicationContext applicationContext, Class<T> tClass) {
        try {
            return applicationContext.getBean(tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> Optional<T> getOptionalBean(ApplicationContext applicationContext, Class<T> tClass) {
        return Optional.ofNullable(getBean(applicationContext, tClass));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        globalApplicationContext = applicationContext;
    }
}
