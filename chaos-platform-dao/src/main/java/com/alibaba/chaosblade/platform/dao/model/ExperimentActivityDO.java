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

package com.alibaba.chaosblade.platform.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_chaos_experiment_activity
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_experiment_activity")
public class ExperimentActivityDO extends BaseDO {

    private String activityName;

    private Long experimentId;

    private Long flowId;

    private String phase;

    private Integer activityOrder;

    private Byte activityPriority;

    private String activityDefinition;

    private String sceneCode;

}
