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

package com.alibaba.chaosblade.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDeviceStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.dao.model.*;
import com.alibaba.chaosblade.platform.dao.repository.*;
import com.alibaba.chaosblade.platform.service.ExperimentActivityService;
import com.alibaba.chaosblade.platform.service.ExperimentActivityTaskService;
import com.alibaba.chaosblade.platform.service.ExperimentMiniFlowService;
import com.alibaba.chaosblade.platform.service.ExperimentTaskService;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentDevice;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentRequest;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentTaskRequest;
import com.alibaba.chaosblade.platform.service.model.experiment.ExperimentTaskResponse;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ExperimentActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.*;

/**
 * @author yefei
 */
@Service
public class ExperimentTaskServiceImpl implements ExperimentTaskService {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentTaskLogRepository experimentTaskLogRepository;

    @Autowired
    private ExperimentActivityService experimentActivityService;

    @Autowired
    private ExperimentActivityTaskRepository experimentActivityTaskRepository;

    @Autowired
    private ExperimentActivityTaskService experimentActivityTaskService;

    @Autowired
    private ExperimentMiniFlowService experimentMiniFlowService;

    @Autowired
    private ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Override
    @Transactional
    public ExperimentTaskResponse createExperimentTask(Long experimentId) {
        ExperimentDO experimentDO = experimentRepository.selectById(experimentId)
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.EXPERIMENT_NOT_FOUNT));
        Long preTaskId = experimentDO.getTaskId();
        if (preTaskId != null) {
            ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(preTaskId).get();

            if (!(experimentTaskDO.getRunStatus() == RunStatus.FINISHED.getValue())) {
                throw new BizException(EXPERIMENT_PRE_NO_FINISH);
            }
        }

        ExperimentTaskDO experimentTaskDO = ExperimentTaskDO.builder()
                .experimentId(experimentId)
                .taskName(experimentDO.getName())
                .taskType((byte) 0)
                .metric(experimentDO.getMetric())
                .runStatus(RunStatus.READY.getValue())
                .build();
        // ready experiment task
        experimentTaskRepository.insert(experimentTaskDO);

        experimentRepository.updateByPrimaryKey(experimentId, ExperimentDO.builder()
                .taskId(experimentTaskDO.getId())
                .build());

        ExperimentActivity experimentActivity = experimentActivityService.selectByExperimentId(experimentId);
        readyActivityTasks(null, experimentActivity, experimentTaskDO.getId());

        List<ExperimentActivityTaskDO> experimentActivityTasks = experimentActivityTaskRepository
                .selectByTaskId(experimentTaskDO.getId());

        experimentActivityTaskService.executeActivityTasks(experimentActivityTasks, experimentTaskDO);

        if (experimentDO.getDuration() != null) {
            // recover experiment todo
//            timerFactory.getTimer().newTimeout(timeout -> {
//
//
//            }, experimentDO.getDuration(), TimeUnit.SECONDS);
        }
        return ExperimentTaskResponse.builder().taskId(experimentTaskDO.getId()).build();
    }

    private void readyActivityTasks(Long preActivityTaskId, ExperimentActivity experimentActivity, Long experimentTaskId) {
        if (experimentActivity == null) {
            return;
        } else {
            // ready activity task
            ExperimentActivityTaskDO activityTaskDO = ExperimentActivityTaskDO.builder()
                    .flowId(experimentActivity.getFlowId())
                    .activityId(experimentActivity.getActivityId())
                    .activityName(experimentActivity.getActivityName())
                    .sceneCode(experimentActivity.getSceneCode())
                    .experimentTaskId(experimentTaskId)
                    .phase(experimentActivity.getPhase())
                    .runStatus(RunStatus.READY.getValue())
                    .preActivityTaskId(preActivityTaskId)
                    .runParam(experimentActivity.getActivityDefinition())
                    .build();
            experimentActivityTaskRepository.insert(activityTaskDO);
            experimentActivityTaskRepository.updateByPrimaryKey(preActivityTaskId,
                    ExperimentActivityTaskDO.builder().nextActivityTaskId(activityTaskDO.getId()).build());
            readyActivityTasks(activityTaskDO.getId(), experimentActivity.getNextActivity(), experimentTaskId);
        }
    }


    @Override
    public void stopExperimentTask(Long taskId) {

        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(taskId).orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        if (RunStatus.STOPPING.getValue() == experimentTaskDO.getRunStatus()) {
            throw new BizException(EXPERIMENT_TASK_STOPPING);
        }
        if (RunStatus.FINISHED.getValue() == experimentTaskDO.getRunStatus() && ResultStatus.SUCCESS.getValue() == experimentTaskDO.getResultStatus()) {
            throw new BizException(EXPERIMENT_TASK_END);
        }

        experimentTaskRepository.updateByPrimaryKey(taskId, ExperimentTaskDO.builder()
                .runStatus(RunStatus.STOPPING.getValue())
                .build());

        List<ExperimentActivityTaskDO> experimentActivityTasks = experimentActivityTaskRepository.selectByTaskId(taskId);

        experimentActivityTasks.forEach(experimentActivityTaskDO -> {
            Byte runState = experimentActivityTaskDO.getRunStatus();
            // ready -> skip
            if (runState.equals(RunStatus.READY.getValue())) {
                experimentActivityTaskRepository.updateByPrimaryKey(taskId, ExperimentActivityTaskDO.builder()
                        .runStatus(RunStatus.FINISHED.getValue())
                        .resultStatus(ResultStatus.REJECTED.getValue())
                        .build());
            }
        });

        List<ExperimentActivityTaskDO> recovers = experimentActivityTasks.stream()
                .filter(experimentActivityTaskDO -> experimentActivityTaskDO.getPhase().equals(ChaosConstant.PHASE_RECOVER))
                .collect(Collectors.toList());

        experimentActivityTaskService.executeActivityTasks(recovers, experimentTaskDO);
    }

    @Override
    public List<ExperimentTaskResponse> getExperimentById(ExperimentRequest experimentRequest) {
        List<ExperimentTaskDO> experimentTasks = experimentTaskRepository.selectByExperimentId(experimentRequest.getExperimentId());
        return experimentTasks.stream().map(experimentTaskDO -> ExperimentTaskResponse.builder()
                .taskId(experimentTaskDO.getId())
                .endTime(experimentTaskDO.getGmtEnd())
                .startTime(experimentTaskDO.getGmtStart())
                .taskName(experimentTaskDO.getTaskName())
                .status(experimentTaskDO.getRunStatus())
                .resultStatus(experimentTaskDO.getResultStatus())
                .build()).collect(Collectors.toList());
    }

    @Override
    public ExperimentTaskResponse queryTaskInfo(ExperimentTaskRequest experimentRequest) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(experimentRequest.getTaskId())
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));

        List<DeviceMeta> deviceMetas = experimentMiniFlowService.selectExperimentDevice(experimentTaskDO.getExperimentId());

        List<ExperimentActivityTaskRecordDO> taskRecords = experimentActivityTaskRecordRepository.selectExperimentTaskId(experimentTaskDO.getId());
        Map<Long, List<ExperimentActivityTaskRecordDO>> listMap = taskRecords.stream()
                .collect(Collectors.toMap(ExperimentActivityTaskRecordDO::getDeviceId, CollUtil::newArrayList, (u1, u2) -> {
                    u1.addAll(u2);
                    return u1;
                }));

        List<Map<String, List<ExperimentDevice>>> mapList = deviceMetas.stream().map(hostMeta -> {
            Map<String, List<ExperimentDevice>> map = new HashMap<>();
            List<ExperimentActivityTaskRecordDO> experimentActivityTask = listMap.get(hostMeta.getDeviceId());
            if (CollUtil.isNotEmpty(experimentActivityTask)) {
                map = experimentActivityTask.stream().collect(Collectors.toMap(ExperimentActivityTaskRecordDO::getPhase,
                        u -> CollUtil.newArrayList(ExperimentDevice.builder()
                                .status(Optional.ofNullable(u.getSuccess())
                                        .map(r -> r ? ExperimentDeviceStatus.SUCCESS.getValue() : ExperimentDeviceStatus.ERROR.getValue())
                                        .orElse(ExperimentDeviceStatus.RUNNING.getValue()))
                                .deviceId(hostMeta.getDeviceId())
                                .deviceType(hostMeta.getDeviceType())
                                .error(u.getErrorMessage())
                                .build()),
                        (u1, u2) -> {
                            u1.addAll(u2);
                            return u1;
                        }
                ));
            }
            return map;
        }).collect(Collectors.toList());

        return ExperimentTaskResponse.builder()
                .experimentId(experimentTaskDO.getExperimentId())
                .taskId(experimentTaskDO.getId())
                .endTime(experimentTaskDO.getGmtEnd())
                .startTime(experimentTaskDO.getGmtStart())
                .taskName(experimentTaskDO.getTaskName())
                .status(experimentTaskDO.getRunStatus())
                .resultStatus(experimentTaskDO.getResultStatus())
                .error(experimentTaskDO.getErrorMessage())
                .machines(mapList)
                .build();
    }

    @Override
    public List<String> queryTaskLog(ExperimentTaskRequest experimentRequest) {
        List<ExperimentTaskLogDO> experimentTaskLogs = experimentTaskLogRepository.selectByTaskId(experimentRequest.getTaskId());
        return experimentTaskLogs.stream().map(ExperimentTaskLogDO::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public ExperimentTaskResponse failRetryExperiment(ExperimentTaskRequest experimentRequest) {
        Long taskId = experimentRequest.getTask().getTaskId();
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(taskId).orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        if (experimentTaskDO.getRunStatus() == RunStatus.RUNNING.getValue()) {
            List<ExperimentActivityTaskDO> experimentActivityTasks = experimentActivityTaskRepository
                    .selectByTaskId(experimentTaskDO.getId());
            experimentTaskRepository.updateByPrimaryKey(taskId,
                    ExperimentTaskDO.builder().runStatus(RunStatus.READY.getValue()).resultStatus(null).build());

            experimentActivityTasks.forEach(experimentActivityTaskDO -> {
                experimentActivityTaskRepository.updateByPrimaryKey(experimentActivityTaskDO.getId(),
                        ExperimentActivityTaskDO.builder().runStatus(RunStatus.READY.getValue()).build());
            });
            experimentActivityTaskService.executeActivityTasks(experimentActivityTasks, experimentTaskDO);
        } else if (experimentTaskDO.getRunStatus() == RunStatus.FINISHED.getValue()){
            stopExperimentTask(taskId);
        } else {
            throw new BizException("Un support operation");
        }
        return null;
    }

}
