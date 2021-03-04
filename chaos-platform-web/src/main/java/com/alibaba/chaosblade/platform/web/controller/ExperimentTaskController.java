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

package com.alibaba.chaosblade.platform.web.controller;

import com.alibaba.chaosblade.platform.cmmon.utils.Preconditions;
import com.alibaba.chaosblade.platform.service.ExperimentTaskService;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentRequest;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentTaskRequest;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.ID_IS_NULL;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class ExperimentTaskController {

    @Autowired
    private ExperimentTaskService experimentTaskService;

    @RequestMapping("/GetTasksByExperimentId")
    public List<ExperimentTaskResponse> getExperimentById(@RequestBody ExperimentRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getExperimentId(), ID_IS_NULL);
        return experimentTaskService.getExperimentById(experimentRequest);
    }

    @RequestMapping("/QueryTaskResult")
    public ExperimentTaskResponse queryTaskInfo(@RequestBody ExperimentTaskRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getTaskId(), ID_IS_NULL);
        return experimentTaskService.queryTaskInfo(experimentRequest);
    }

    @RequestMapping("/QueryTaskLog")
    public List<String> queryTaskLog(@RequestBody ExperimentTaskRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getTaskId(), ID_IS_NULL);
        return experimentTaskService.queryTaskLog(experimentRequest);
    }

    @RequestMapping("/FailRetryExperiment")
    public ExperimentTaskResponse failRetryExperiment(@RequestBody ExperimentTaskRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getTaskId(), ID_IS_NULL);
        return experimentTaskService.failRetryExperiment(experimentRequest);
    }

}
