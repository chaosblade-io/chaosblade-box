package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosApp;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosInject;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import java.lang.reflect.Field;

/**
 * @author haibin
 *
 *
 */
@Component
public class MiniAppSpringProcessor implements MiniAppProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterInit(ChaosAppDescriptor chaosAppDescriptor) {
        ChaosApp chaosApp = chaosAppDescriptor.getReference();
        ReflectionUtils.doWithFields(chaosApp.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ChaosInject chaosInject = AnnotationUtils.findAnnotation(field, ChaosInject.class);
                if (chaosInject == null) { return; }
                injectBean(chaosInject, chaosApp, field);
            }
        });
    }

    private void injectBean(ChaosInject chaosInject, ChaosApp chaosApp, Field field) throws IllegalAccessException {
        Class<?> fieldClass = field.getType();
        Object bean;
        if (!Strings.isNullOrEmpty(chaosInject.beanName())) {
            bean = applicationContext.getBean(chaosInject.beanName());
        } else {
            bean = applicationContext.getBean(fieldClass);
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(chaosApp, bean);
    }

}
