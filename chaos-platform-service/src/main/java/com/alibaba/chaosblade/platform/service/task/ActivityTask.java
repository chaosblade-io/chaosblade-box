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

package com.alibaba.chaosblade.platform.service.task;

import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yefei
 */
public class ActivityTask {

    private final ActivityTaskDTO activityTaskDTO;

    private final CompletableFuture<Void> completableFuture;

    private final AtomicBoolean isExecuted = new AtomicBoolean();

    private ActivityTaskExecutePipeline activityTaskExecutePipeline;

    public ActivityTask(ActivityTaskDTO activityTaskDTO) {
        this.activityTaskDTO = activityTaskDTO;
        this.completableFuture = new CompletableFuture<>();
    }

    public CompletableFuture<Void> future() {
        return completableFuture;
    }

    public ActivityTaskDTO activityTaskDTO() {
        return activityTaskDTO;
    }

    public boolean canExecuted() {
        return isExecuted.compareAndSet(false, true);
    }

    public ActivityTaskExecutePipeline getActivityTaskExecutePipeline() {
        return activityTaskExecutePipeline;
    }

    public void setActivityTaskExecutePipeline(ActivityTaskExecutePipeline activityTaskExecutePipeline) {
        this.activityTaskExecutePipeline = activityTaskExecutePipeline;
    }
}
