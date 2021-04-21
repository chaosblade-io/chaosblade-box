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

package com.alibaba.chaosblade.box.service.model.experiment;

import com.alibaba.chaosblade.box.service.model.MachineResponse;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivity;
import com.alibaba.chaosblade.box.service.model.metric.MetricModel;
import com.alibaba.chaosblade.box.service.model.scene.SceneResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentResponse {

    private Long experimentId;

    private String experimentName;

    private Integer taskCount;

    private Long lastTaskId;

    private Date lastTaskStartTime;

    private Date lastTaskEndTime;

    private Byte lastTaskStatus;

    private Byte lastTaskResult;

    private Long taskDuration;

    private Date createTime;

    private Date modifyTime;

    private String dimension;

    private List<MachineResponse> machines;

    private List<SceneResponse> scenarios;

    private List<MetricModel> metrics;

    private List<ExperimentActivity> activities;

}
