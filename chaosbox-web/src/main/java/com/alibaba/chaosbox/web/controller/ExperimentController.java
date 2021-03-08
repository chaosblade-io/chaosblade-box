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

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosbox.common.utils.Preconditions;
import com.alibaba.chaosbox.service.ExperimentService;
import com.alibaba.chaosbox.service.model.experiment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.alibaba.chaosbox.common.exception.ExceptionMessageEnum.*;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @RequestMapping("/CreateExperiment")
    public ExperimentResponse createExperiment(@RequestBody CreateExperimentRequest createExperimentRequest)
            throws Exception {
        Preconditions.checkArgument(StrUtil.isBlank(createExperimentRequest.getExperimentName()), EXPERIMENT_NAME_IS_NULL);
        Preconditions.checkNotNull(createExperimentRequest.getScenarioId(), EXPERIMENT_SCENE_IS_NULL);
        return experimentService.createExperiment(createExperimentRequest);
    }

    @RequestMapping("/DeleteExperiment")
    public void deleteExperiment(@RequestBody ExperimentRequest experimentRequest)
            throws Exception {
        Preconditions.checkNotNull(experimentRequest.getExperimentId(), ID_IS_NULL);
        experimentService.deleteExperiment(experimentRequest);
    }

    @RequestMapping("/GetExperimentById")
    public ExperimentResponse getExperimentById(@RequestBody ExperimentRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getExperimentId(), ID_IS_NULL);
        return experimentService.getExperimentById(experimentRequest);
    }

    @RequestMapping("/GetExperimentsPageable")
    public List<ExperimentResponse> GetExperimentsPageable(@RequestBody ExperimentRequest experimentRequest) {
        return experimentService.getExperimentsPageable(experimentRequest);
    }

    @RequestMapping("/UpdateExperiment")
    public ExperimentResponse updateExperiment(@RequestBody CreateExperimentRequest createExperimentRequest) {
        Preconditions.checkNotNull(createExperimentRequest.getExperimentId(), ID_IS_NULL);
        return experimentService.updateExperiment(createExperimentRequest);
    }

    @RequestMapping("/StartExperiment")
    public ExperimentTaskResponse executeExperiment(@RequestBody ExperimentRequest experimentRequest) {
        Preconditions.checkNotNull(experimentRequest.getExperimentId(), ID_IS_NULL);
        return experimentService.executeExperiment(experimentRequest);
    }

    @RequestMapping("/EndExperiment")
    public void finishExperiment(@RequestBody ExperimentTaskRequest experimentTaskRequest) {
        Preconditions.checkNotNull(experimentTaskRequest.getTaskId(), ID_IS_NULL);
        experimentService.finishExperiment(experimentTaskRequest);
    }

    @RequestMapping("/GetExperimentTotalStatistics")
    public ExperimentStatisticsResponse getExperimentTotalStatistics() {
        return experimentService.getExperimentTotalStatistics();
    }
}
