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

package com.alibaba.chaosblade.platform.service.task.stateless;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.stateless.phase.ActivityTaskPhase;
import com.alibaba.chaosblade.platform.service.task.stateless.phase.ActivityTaskPhaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
public abstract class AbstractActivityTaskHandler implements ActivityTaskPhaseHandler, BeanPostProcessor {

    private ConcurrentHashMap<String, ActivityTaskPhaseHandler> phaseHandlerMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(ActivityTaskExecuteContext context, ActivityTask activityTask) {
        ActivityTaskPhaseHandler activityTaskPhaseHandler = phaseHandlerMap.get(activityTask.activityTaskDTO().getPhase());
        return activityTaskPhaseHandler.preHandle(context, activityTask);
    }

    @Override
    public void postHandle(ActivityTaskExecuteContext context, ActivityTask activityTask, Throwable e) {
        ActivityTaskPhaseHandler activityTaskPhaseHandler = phaseHandlerMap.get(activityTask.activityTaskDTO().getPhase());
        activityTaskPhaseHandler.postHandle(context, activityTask, e);
    }

    @Override
    public RequestCommand requestCommand(ActivityTask activityTask, DeviceMeta deviceMeta) {
        ActivityTaskPhaseHandler activityTaskPhaseHandler = phaseHandlerMap.get(activityTask.activityTaskDTO().getPhase());
        return activityTaskPhaseHandler.requestCommand(activityTask, deviceMeta);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ActivityTaskPhaseHandler) {
            ActivityTaskPhaseHandler activityTaskPhaseHandler = (ActivityTaskPhaseHandler) bean;
            ActivityTaskPhase activityTaskPhase = bean.getClass().getAnnotation(ActivityTaskPhase.class);
            if (activityTaskPhase != null) {
                phaseHandlerMap.put(activityTaskPhase.value(), activityTaskPhaseHandler);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
