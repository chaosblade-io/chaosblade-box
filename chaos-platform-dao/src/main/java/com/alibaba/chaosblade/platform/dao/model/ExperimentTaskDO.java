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

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * t_chaos_experiment_task
 * @author yefei
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_chaos_experiment_task")
public class ExperimentTaskDO extends BaseDO {

    private String taskName;

    private Long experimentId;

    private Long activityTaskId;

    /**
     *  1 auto, 0 manual
     */
    private Byte taskType;

    private String result;

    private Date gmtEnd;

    private String hosts;

    private Long activityId;

    private Byte runStatus;

    private Byte resultStatus;

    private Date gmtStart;

    private String errorMessage;

    private Integer duration;

    private String metric;

}
