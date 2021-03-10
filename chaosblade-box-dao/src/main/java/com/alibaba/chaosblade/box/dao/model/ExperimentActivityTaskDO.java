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

package com.alibaba.chaosblade.box.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * t_chaos_experiment_activity_task
 * @author yefei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_chaos_experiment_activity_task")
public class ExperimentActivityTaskDO extends BaseDO {

    private Long activityId;

    private String activityName;

    private Long experimentTaskId;

    private Long flowId;

    private String phase;

    private Date gmtEnd;

    private String errorMessage;

    private Byte runStatus;

    private Byte resultStatus;

    private Date gmtStart;

    private Long preActivityTaskId;

    private Long nextActivityTaskId;

    private String runParam;

    private Integer activityOrder;

    private String sceneCode;

    private Long appId;

}
