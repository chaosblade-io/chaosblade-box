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

package com.alibaba.chaosblade.platform.service.task.stateless;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskRecordDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.SceneRepository;
import com.alibaba.chaosblade.platform.cmmon.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
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
@ActivityTaskHandlerType(ChaosConstant.PHASE_ATTACK)
public class AttackActivityTaskHandler extends DefaultActivityTaskPhaseHandler {

    @Autowired
    private SceneRepository sceneRepository;

    @Override
    public boolean preHandle(ActivityTask activityTask) {
        boolean b = super.preHandle(activityTask);
        if (b) {
            sceneRepository.incrementUseCount(activityTask.getSceneId());
        }
        return b;
    }

    @Override
    public void postHandle(ActivityTask activityTask, Throwable e) {

        List<ExperimentActivityTaskRecordDO> records = experimentActivityTaskRecordRepository.selectExperimentTaskId(activityTask.getExperimentTaskId());
        long count = records.stream().filter(r ->
                r.getPhase().equals(ChaosConstant.PHASE_ATTACK)
                        && Optional.ofNullable(r.getSuccess()).orElse(false)).count();

        // update activity task
        experimentActivityTaskRepository.updateByPrimaryKey(activityTask.getActivityTaskId(),
                ExperimentActivityTaskDO.builder()
                        .runStatus(RunStatus.FINISHED.getValue())
                        .resultStatus(count > 0 ? ResultStatus.SUCCESS.getValue() : ResultStatus.FAILED.getValue())
                        .errorMessage(e != null ? e.getMessage() : StrUtil.EMPTY)
                        .gmtEnd(DateUtil.date())
                        .build());

        if (count > 0) {
            experimentTaskRepository.updateByPrimaryKey(activityTask.getExperimentTaskId(), ExperimentTaskDO.builder()
                    .resultStatus(ResultStatus.SUCCESS.getValue())
                    .build());
        }

        super.postHandle(activityTask, e);
    }

}
