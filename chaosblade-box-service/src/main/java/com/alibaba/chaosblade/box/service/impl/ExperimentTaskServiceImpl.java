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

package com.alibaba.chaosblade.box.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ResultStatus;
import com.alibaba.chaosblade.box.common.enums.RunStatus;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentTaskMapper;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.*;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentTaskRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentTaskResponse;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentTaskStatistics;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivity;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivityTask;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivityTaskRecord;
import com.alibaba.chaosblade.box.service.model.scene.SceneRequest;
import com.alibaba.chaosblade.box.service.model.scene.SceneResponse;
import com.alibaba.chaosblade.box.service.task.ActivityTask;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.EXPERIMENT_TASK_END;
import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

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
    private ExperimentActivityTaskRecordRepository experimentActivityTaskRecordRepository;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private ExperimentMiniFlowService experimentMiniFlowService;

    @Autowired
    private ExperimentTaskMapper experimentTaskMapper;

    @Override
    public ExperimentTaskResponse createExperimentTask(Long experimentId) {
        ExperimentDO experimentDO = experimentRepository.selectById(experimentId)
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.EXPERIMENT_NOT_FOUNT));
        Long preTaskId = experimentDO.getTaskId();
        if (preTaskId != null) {
            ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(preTaskId).get();

            if (!(experimentTaskDO.getRunStatus() == RunStatus.FINISHED.getValue())) {
                throw new BizException(ExceptionMessageEnum.EXPERIMENT_PRE_NO_FINISH);
            }
        }

        List<DeviceMeta> deviceMetas = experimentMiniFlowService.selectExperimentDevice(experimentId);

        ExperimentTaskDO experimentTaskDO = ExperimentTaskDO.builder()
                .experimentId(experimentId)
                .taskName(experimentDO.getName())
                .taskType((byte) 0)
                .hosts(JsonUtils.writeValueAsString(deviceMetas))
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
            throw new BizException(ExceptionMessageEnum.EXPERIMENT_TASK_STOPPING);
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

        List<ExperimentActivityTask> experimentActivityTasks = experimentActivityTaskService.selectExperimentActivityTask(experimentRequest.getTaskId());
        //List<ExperimentActivityTaskRecordDO> taskRecords = experimentActivityTaskRecordRepository.selectExperimentTaskId(experimentTaskDO.getId());

        return ExperimentTaskResponse.builder()
                .experimentId(experimentTaskDO.getExperimentId())
                .taskId(experimentTaskDO.getId())
                .endTime(experimentTaskDO.getGmtEnd())
                .startTime(experimentTaskDO.getGmtStart())
                .taskName(experimentTaskDO.getTaskName())
                .status(experimentTaskDO.getRunStatus())
                .resultStatus(experimentTaskDO.getResultStatus())
                .error(experimentTaskDO.getErrorMessage())
                .activityTasks(experimentActivityTasks.stream().map(experimentActivityTask -> {
                    ActivityTask activityTask = JsonUtils.readValue(ActivityTask.class, experimentActivityTask.getRunParam());
                    SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder().scenarioId(activityTask.getSceneId()).build());
                    experimentActivityTask.setScene(scenario);
                    return experimentActivityTask;
                }).collect(Collectors.toList()))
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
        } else if (experimentTaskDO.getRunStatus() == RunStatus.FINISHED.getValue()) {
            stopExperimentTask(taskId);
        } else {
            throw new BizException("Un support operation");
        }
        return null;
    }

    @Override
    public List<ExperimentActivityTaskRecord> queryTaskRecord(ExperimentTaskRequest experimentRequest) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(experimentRequest.getTaskId())
                .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
        if (StrUtil.isBlank(experimentTaskDO.getHosts())) {
            return Collections.emptyList();
        }

        List<DeviceMeta> deviceMetas = JsonUtils.readValue(new TypeReference<List<DeviceMeta>>() {
        }, experimentTaskDO.getHosts());

        Map<String, ExperimentActivityTaskRecordDO> map = experimentActivityTaskRecordRepository
                .selectActivityTaskId(experimentRequest.getActivityTaskId())
                .stream().collect(Collectors.toMap(ExperimentActivityTaskRecordDO::getIp, v -> v));

        return deviceMetas.stream().map(deviceMeta -> {
            ExperimentActivityTaskRecordDO experimentActivityTaskRecordDO = map.get(deviceMeta.getIp());
            if (experimentActivityTaskRecordDO == null) {
                return ExperimentActivityTaskRecord.builder()
                        .ip(deviceMeta.getIp())
                        .build();
            } else {
                ExperimentActivityTaskRecord experimentActivityTask = new ExperimentActivityTaskRecord();
                BeanUtil.copyProperties(experimentActivityTaskRecordDO, experimentActivityTask);
                return experimentActivityTask;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<ExperimentTaskStatistics> queryTaskStatistics() {
        List<Map<Integer, Object>> maps = experimentTaskMapper.queryTaskStatistics();
        return maps.stream().map(map -> ExperimentTaskStatistics.builder()
                .date(String.valueOf(map.get("date")))
                .taskCount(String.valueOf(map.get("taskCount")))
                .build()).collect(Collectors.toList());
    }
}
