package com.alibaba.chaosblade.box.common.infrastructure.util;

/**
 * @author haibin
 *
 *
 */

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component
@Slf4j
public class SpringApplicationContextHolder implements ApplicationContextAware {

    /**
     * spring application context.
     */
    @Getter
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationContextHolder.context = context;
    }

    /**
     * get spring bean via nameSuffix.
     *
     * @param beanName the bean's nameSuffix.
     * @param <T>      the generic of the spring bean.
     * @return the found spring bean.
     */
    public static <T> T getSpringBean(String beanName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(beanName), "bean nameSuffix is required");
        if (null == context) {
            log.warn("spring application context is not injected");
            return null;
        }
        return (T)context.getBean(beanName);
    }

    public static <T> Map<String, T> getBeanNamesForType(Class<T> type) {
        return context.getBeansOfType(type);
    }

    public static <T> Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return context.getBeansWithAnnotation(annotationType);
    }

    /**
     * get the spring bean via the Class.
     *
     * @param beanClass the bean class.
     * @param <T>       the generic type of the bean.
     * @return the found bean.
     */
    public static <T> T getSpringBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * get spring bean names.
     *
     * @return
     */
    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }
}
