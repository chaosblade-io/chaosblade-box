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

package com.alibaba.chaosblade.platform.metric.processor;

import ch.qos.logback.classic.LoggerContext;
import com.alibaba.chaosblade.platform.service.logback.DBLoggerAppender;
import com.alibaba.chaosblade.platform.cmmon.TaskLogRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Component
public class ExperimentTaskLogBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        TaskLogRecord annotation = bean.getClass().getAnnotation(TaskLogRecord.class);
        if (annotation != null) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            DBLoggerAppender.configure(loggerContext, bean.getClass(), applicationContext);
            Class<?> clazz = bean.getClass();
            while ((clazz = clazz.getSuperclass()) != null) {
                if (clazz.getAnnotation(TaskLogRecord.class) != null) {
                    DBLoggerAppender.configure(loggerContext, clazz, applicationContext);
                }
            }
        }
        return bean;
    }
}
