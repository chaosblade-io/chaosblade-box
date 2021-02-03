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
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentActivityTaskRecordRepository;
import com.alibaba.chaosblade.platform.http.model.reuest.DestroyCommandRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.DestroyWithoutUidRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.http.model.reuest.RevokeCommandRequest;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@Component(ChaosConstant.PHASE_RECOVER)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@TaskLogRecord
public class DestroyActivityTask extends AbstractActivityTask<HttpChannelRequest> implements ActivityTask, PreviousPhaseActivityTaskListener {

    @Autowired
    private ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    public DestroyActivityTask(ActivityTaskDTO activityTaskDTO) {
        super(activityTaskDTO);
    }

    @Override
    public boolean preHandler(ActivityTaskExecuteContext context) {
        // check status
        Byte status = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                .map(ExperimentTaskDO::getRunStatus)
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        RunStatus runStatus = RunStatus.parse(status);

        log.info("恢复任务阶段, 检查任务状态，任务ID: {}，任务状态: {} ", activityTaskDTO.getExperimentTaskId(), runStatus.name());
        if (runStatus != RunStatus.STOPPING) {
            log.info("恢复任务阶段不可执行，状态不为 STOPPING 的任务，任务ID: {}", activityTaskDTO.getExperimentTaskId());
            return false;
        }
        return true;
    }

    @Override
    public void complete(ActivityTaskExecuteContext context, Throwable throwable) {
        execute(context);
    }

    @Override
    public HttpChannelRequest requestCommand(DeviceMeta deviceMeta) {
        String sceneCode = activityTaskDTO.getSceneCode();
        if (SceneCodeParseUtil.isRevoke(sceneCode)) {
            ExperimentActivityTaskRecordDO experimentActivityTaskRecord = experimentActivityTaskRecordRepository.selectOneByPhase(
                    activityTaskDTO.getExperimentTaskId(),
                    deviceMeta.getIp(),
                    ChaosConstant.PHASE_PREPARE
            ).get();
            if (experimentActivityTaskRecord.getSuccess()) {
                //
                RevokeCommandRequest revokeCommandRequest = new RevokeCommandRequest();
                revokeCommandRequest.setUid(experimentActivityTaskRecord.getResult());
                return revokeCommandRequest;
            } else {
                // prepare fail, nothing to do
                return null;
            }
        } else {
            ExperimentActivityTaskRecordDO experimentActivityTaskRecord = experimentActivityTaskRecordRepository.selectOneByPhase(
                    activityTaskDTO.getExperimentTaskId(),
                    deviceMeta.getIp(),
                    ChaosConstant.PHASE_ATTACK
            ).get();

            if (experimentActivityTaskRecord.getSuccess()) {
                DestroyCommandRequest destroyCommandRequest = new DestroyCommandRequest();
                destroyCommandRequest.setArguments(activityTaskDTO.getArguments());
                destroyCommandRequest.setUid(experimentActivityTaskRecord.getResult());
                return destroyCommandRequest;
            } else {
                DestroyWithoutUidRequest destroyWithoutUidRequest = new DestroyWithoutUidRequest();
                destroyWithoutUidRequest.setTarget(activityTaskDTO.getTarget());
                destroyWithoutUidRequest.setAction(activityTaskDTO.getAction());
                destroyWithoutUidRequest.setArguments(activityTaskDTO.getArguments());
                return destroyWithoutUidRequest;
            }
        }
    }
}
