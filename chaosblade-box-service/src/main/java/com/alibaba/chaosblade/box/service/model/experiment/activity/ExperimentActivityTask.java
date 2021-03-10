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

package com.alibaba.chaosblade.box.service.model.experiment.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yefei
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentActivityTask {

    private String activityId;

    private String activityName;

    private String experimentTaskId;

    private String phase;

    private Date gmtEnd;

    private String errorMessage;

    private Byte runState;

    private Byte resultState;

    private Date gmtStart;

    private String namespace;

    private Byte taskMode;

    private ExperimentActivityTask nextActivityTask;

    private Long nextActivityTaskId;

    private Byte userCheckState;

    private String runParam;

    private Integer activityOrder;

    private String appCode;

    private Long appId;

}
