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

package com.alibaba.chaosbox.web.controller;

import com.alibaba.chaosbox.service.MetricTaskService;
import com.alibaba.chaosbox.service.model.metric.MetricCategoryResponse;
import com.alibaba.chaosbox.service.model.metric.MetricTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class MetricController {

    @Autowired
    private MetricTaskService metricTaskService;

    @RequestMapping("/QueryMetricCategory")
    public List<MetricCategoryResponse> queryMetricCategory() {
        return metricTaskService.queryMetricCategory();
    }

    @RequestMapping("/QueryTaskMonitor")
    public List<MetricCategoryResponse> queryTaskMonitor(@RequestBody MetricTaskRequest metricTaskRequest) {
        return metricTaskService.selectByTaskId(metricTaskRequest);
    }
}
