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

package com.alibaba.chaosbox.service.task.stateless;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosbox.common.enums.ExperimentDimension;
import com.alibaba.chaosbox.common.exception.BizException;
import com.alibaba.chaosbox.common.TaskLogRecord;
import com.alibaba.chaosbox.service.task.ActivityTask;
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
public class ActivityTaskHandlerStrategyContext implements ActivityTaskHandler, BeanPostProcessor {

    private ConcurrentHashMap<ActivityTaskHandlerType, ActivityTaskHandler> map = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(ActivityTask activityTask) {
        ActivityTaskHandler activityTaskPhaseHandler = select(activityTask);
        return activityTaskPhaseHandler.preHandle(activityTask);
    }

    @Override
    public void handle(ActivityTask activityTask) {
        ActivityTaskHandler activityTaskPhaseHandler = select(activityTask);
        activityTaskPhaseHandler.handle(activityTask);
    }

    @Override
    public void postHandle(ActivityTask activityTask, Throwable e) {
        ActivityTaskHandler activityTaskPhaseHandler = select(activityTask);
        activityTaskPhaseHandler.postHandle(activityTask, e);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ActivityTaskHandler) {
            ActivityTaskHandler activityTaskPhaseHandler = (ActivityTaskHandler) bean;
            ActivityTaskHandlerType activityTaskHandlerType = bean.getClass().getAnnotation(ActivityTaskHandlerType.class);
            if (activityTaskHandlerType != null) {
                map.put(activityTaskHandlerType, activityTaskPhaseHandler);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private ActivityTaskHandler select(ActivityTask activityTask) {

        for (ActivityTaskHandlerType activityTaskType : map.keySet()) {
            for (ExperimentDimension experimentDimension : activityTaskType.dimension()) {
                if (experimentDimension == activityTask.getExperimentDimension()) {
                    String[] value = activityTaskType.value();
                    if (ArrayUtil.isNotEmpty(value)) {
                        for (String s : value) {
                            if (s.equalsIgnoreCase(activityTask.getPhase())) {
                                return map.get(activityTaskType);
                            }
                        }
                    }
                }
            }
        }
        throw new BizException("ActivityTaskHandler is null");
    }

}
