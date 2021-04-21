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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.service.model.experiment.ExperimentTaskRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentTaskResponse;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivityTask;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivityTaskRecord;

import java.util.List;

/**
 * @author yefei
 */
public interface ExperimentTaskService {

    /**
     *
     * @param experimentId
     * @return
     */
    ExperimentTaskResponse createExperimentTask(Long experimentId);

    /**
     *
     * @return
     */
    void stopExperimentTask(Long taskId) ;

    /**
     *
     * @param experimentRequest
     * @return
     */
    List<ExperimentTaskResponse> getExperimentById(ExperimentRequest experimentRequest);

    /**
     *
     * @param experimentRequest
     * @return
     */
    ExperimentTaskResponse queryTaskInfo(ExperimentTaskRequest experimentRequest);

    /**
     *
     * @param experimentRequest
     * @return
     */
    List<String> queryTaskLog(ExperimentTaskRequest experimentRequest);

    /**
     *
     * @param experimentRequest
     * @return
     */
    ExperimentTaskResponse failRetryExperiment(ExperimentTaskRequest experimentRequest);

    /**
     *
     * @param experimentRequest
     * @return
     */
    List<ExperimentActivityTaskRecord> queryTaskRecord(ExperimentTaskRequest experimentRequest);
}
