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

package com.alibaba.chaosbox.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * t_chaos_metric_task
 * @author yefei
 */
@Data
@TableName("t_chaos_metric_task")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricTaskDO extends BaseDO {

    private Long taskId;

    private Long deviceId;

    private String ip;

    private String hostname;

    private Date date;

    private String value;

    private String unit;

    private Long categoryId;

    private String categoryCode;

    private String metric;

}
