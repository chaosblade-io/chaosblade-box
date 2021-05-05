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

package com.alibaba.chaosblade.box.service.task;

import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ExperimentDimension;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(exclude = "activityTaskId")
public class ActivityTask {

    private Long flowId;

    private Long experimentTaskId;

    private Long activityId;

    private Long activityTaskId;

    private Long preActivityTaskId;

    private Long nextActivityTaskId;

    private Long sceneId;

    private String sceneCode;

    private String phase;

    private String target;

    private String action;

    private Boolean manualChecked;

    private Long waitOfBefore;

    private Long waitOfAfter;

    private Map<String, String> arguments;

    private ExperimentDimension experimentDimension;

    private List<DeviceMeta> deviceMetas;

    private boolean retry;

    public boolean isRecoverPhase() {
        return ChaosConstant.PHASE_RECOVER.equals(phase);
    }

    public boolean isAttackPhase() {
        return ChaosConstant.PHASE_ATTACK.equals(phase);
    }

    @JsonIgnore
    private final CompletableFuture<Void> completableFuture;

    @JsonIgnore
    private final AtomicBoolean isExecuted = new AtomicBoolean();

    @JsonIgnore
    private ActivityTaskExecutePipeline activityTaskExecutePipeline;

    public ActivityTask() {
        this.completableFuture = new CompletableFuture<>();
    }

    public CompletableFuture<Void> future() {
        return completableFuture;
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
