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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.SceneRepository;
import com.alibaba.chaosblade.platform.http.model.reuest.ModelRequest;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author yefei
 */
@Slf4j
@TaskLogRecord
@Component
@ActivityTaskPhase(ChaosConstant.PHASE_ATTACK)
public class AttackActivityTaskPhaseHandler extends AbstractActivityTaskPhaseHandler {

    @Autowired
    private SceneRepository sceneRepository;

    @Override
    public boolean preHandle(ActivityTaskExecuteContext context, ActivityTask activityTask) {
        boolean b = super.preHandle(context, activityTask);
        if (b) {
            sceneRepository.incrementUseCount(activityTask.activityTaskDTO().getSceneId());
        }
        return b;
    }

    @Override
    public void postHandle(ActivityTaskExecuteContext context, ActivityTask activityTask, Throwable e) {
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectExperimentTaskId(activityTaskDTO.getExperimentTaskId());
        long count = records.stream().filter(r ->
                r.getPhase().equals(ChaosConstant.PHASE_ATTACK)
                        && Optional.ofNullable(r.getSuccess()).orElse(false)).count();

        // update activity task
        experimentActivityTaskRepository.updateByPrimaryKey(activityTaskDTO.getActivityTaskId(),
                ExperimentActivityTaskDO.builder()
                        .runStatus(RunStatus.FINISHED.getValue())
                        .resultStatus(count > 0 ? ResultStatus.SUCCESS.getValue() : ResultStatus.FAILED.getValue())
                        .errorMessage(e != null ? e.getMessage() : StrUtil.EMPTY)
                        .gmtEnd(DateUtil.date())
                        .build());

        if (count > 0) {
            experimentTaskRepository.updateByPrimaryKey(activityTaskDTO.getExperimentTaskId(), ExperimentTaskDO.builder()
                    .resultStatus(ResultStatus.SUCCESS.getValue())
                    .build());
        }
        if (e == null) {
            log.info("子任务运行完成，任务ID: {}，阶段：{}, 子任务ID：{} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId()
            );
        } else {
            log.error("子任务运行失败，任务ID: {}，阶段：{}, 子任务ID: {}, 失败原因: {} ",
                    activityTaskDTO.getExperimentTaskId(),
                    activityTaskDTO.getPhase(),
                    activityTaskDTO.getActivityTaskId(),
                    e.getMessage()
            );
        }
        activityTask.future().complete(null);
    }

    @Override
    public ModelRequest requestCommand(ActivityTask activityTask, DeviceMeta deviceMeta) {
        final ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
        ModelRequest modelRequest = new ModelRequest();
        modelRequest.setTarget(activityTaskDTO.getTarget());
        modelRequest.setAction(activityTaskDTO.getAction());
        modelRequest.setArguments(activityTaskDTO.getArguments());
        return modelRequest;
    }

}
