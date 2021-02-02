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

package com.alibaba.chaosblade.platform.service.task;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.http.model.reuest.PrepareCommandRequest;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Slf4j
@Component(ChaosConstant.PHASE_PREPARE)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@TaskLogRecord
public final class PrepareActivityTask extends AbstractActivityTask<PrepareCommandRequest> implements PreviousPhaseActivityTaskListener {

    public PrepareActivityTask(ActivityTaskDTO activityTaskDTO) {
        super(activityTaskDTO);
    }

    @Override
    public void complete(ActivityTaskExecuteContext activityTaskExecuteContext) {
        execute(activityTaskExecuteContext);
    }

    @Override
    public PrepareCommandRequest requestCommand(DeviceMeta deviceMeta) {
        String sceneCode = activityTaskDTO.getSceneCode();
        String prepareType = SceneCodeParseUtil.getPrepareType(sceneCode);
        PrepareCommandRequest prepareCommandRequest = new PrepareCommandRequest();
        prepareCommandRequest.setType(prepareType);
        return prepareCommandRequest;
    }
}
