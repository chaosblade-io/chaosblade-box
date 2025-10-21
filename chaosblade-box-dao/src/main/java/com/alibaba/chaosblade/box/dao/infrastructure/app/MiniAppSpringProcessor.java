/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosApp;
import com.alibaba.chaosblade.box.common.app.sdk.annotations.ChaosInject;
import com.alibaba.chaosblade.box.dao.infrastructure.app.descriptors.ChaosAppDescriptor;
import com.google.common.base.Strings;
import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/** @author haibin */
@Component
public class MiniAppSpringProcessor implements MiniAppProcessor {
  @Autowired private ApplicationContext applicationContext;

  @Override
  public void afterInit(ChaosAppDescriptor chaosAppDescriptor) {
    ChaosApp chaosApp = chaosAppDescriptor.getReference();
    ReflectionUtils.doWithFields(
        chaosApp.getClass(),
        new FieldCallback() {
          @Override
          public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ChaosInject chaosInject = AnnotationUtils.findAnnotation(field, ChaosInject.class);
            if (chaosInject == null) {
              return;
            }
            injectBean(chaosInject, chaosApp, field);
          }
        });
  }

  private void injectBean(ChaosInject chaosInject, ChaosApp chaosApp, Field field)
      throws IllegalAccessException {
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
