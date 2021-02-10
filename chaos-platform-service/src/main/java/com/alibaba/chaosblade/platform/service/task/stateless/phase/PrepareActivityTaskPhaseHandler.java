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

package com.alibaba.chaosblade.platform.service.task.stateless.phase;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.http.model.reuest.PrepareCommandRequest;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Slf4j
@Component
@TaskLogRecord
@ActivityTaskPhase(ChaosConstant.PHASE_PREPARE)
public class PrepareActivityTaskPhaseHandler extends AbstractActivityTaskPhaseHandler {

    @Override
    public RequestCommand requestCommand(ActivityTask activityTask, DeviceMeta deviceMeta) {
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();

        String sceneCode = activityTaskDTO.getSceneCode();
        String prepareType = SceneCodeParseUtil.getPrepareType(sceneCode);
        PrepareCommandRequest prepareCommandRequest = new PrepareCommandRequest();
        prepareCommandRequest.setType(prepareType);
        prepareCommandRequest.setArguments(activityTaskDTO.getArguments());
        prepareCommandRequest.setTimeout(5 * 60 * 1000L);
        return prepareCommandRequest;
    }
}
