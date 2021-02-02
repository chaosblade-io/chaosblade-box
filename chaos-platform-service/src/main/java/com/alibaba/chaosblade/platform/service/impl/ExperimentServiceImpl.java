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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.service.model.metric.MetricModel;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.dao.model.*;
import com.alibaba.chaosblade.platform.dao.page.PageUtils;
import com.alibaba.chaosblade.platform.dao.repository.*;
import com.alibaba.chaosblade.platform.service.ExperimentActivityService;
import com.alibaba.chaosblade.platform.service.ExperimentService;
import com.alibaba.chaosblade.platform.service.ExperimentTaskService;
import com.alibaba.chaosblade.platform.service.SceneService;
import com.alibaba.chaosblade.platform.service.model.MachineResponse;
import com.alibaba.chaosblade.platform.service.model.device.DeviceRequest;
import com.alibaba.chaosblade.platform.service.model.experiment.*;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ExperimentActivity;
import com.alibaba.chaosblade.platform.service.model.scene.SceneParamResponse;
import com.alibaba.chaosblade.platform.service.model.scene.SceneRequest;
import com.alibaba.chaosblade.platform.service.model.scene.SceneResponse;
import com.alibaba.chaosblade.platform.service.model.scene.prepare.JavaAgentPrepare;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant.CHAOS_DEFAULT_NA;
import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.DEVICE_NOT_FOUNT;

/**
 * @author yefei
 */
@Service
public class ExperimentServiceImpl implements ExperimentService {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentActivityRepository experimentActivityRepository;

    @Autowired
    private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

    @Autowired
    private ExperimentMiniFlowRepository experimentMiniFlowRepository;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private ExperimentActivityService experimentActivityService;

    @Autowired
    private ExperimentTaskService experimentTaskService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    @Transactional
    public ExperimentResponse createExperiment(CreateExperimentRequest createExperimentRequest) throws Exception {

        // experiment
        ExperimentDO experimentDO = ExperimentDO.builder()
                .name(createExperimentRequest.getExperimentName())
                .metric(createExperimentRequest.getMetrics())
                .build();
        Long experimentId = experimentRepository.insert(experimentDO);

        // default mini flow group
        List<DeviceRequest> machines = createExperimentRequest.getMachines();
        List<DeviceMeta> deviceMetas = machines.stream().map(machine -> deviceRepository.selectById(machine.getDeviceId()).map(deviceDO ->
                        DeviceMeta.builder().deviceId(deviceDO.getId())
                                .deviceType(deviceDO.getType())
                                .hostname(deviceDO.getHostname())
                                .ip(deviceDO.getIp()).build()
                ).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT))
        ).collect(Collectors.toList());

        Long flowGroupId = experimentMiniFlowGroupRepository.insert(ExperimentMiniFlowGroupDO.builder()
                .groupName(CHAOS_DEFAULT_NA)
                .hosts(JsonUtils.writeValueAsString(deviceMetas))
                .experimentId(experimentId)
                .build());

        // default mini flow
        Long flowId = experimentMiniFlowRepository.insert(ExperimentMiniFlowDO.builder()
                .groupId(flowGroupId)
                .experimentId(experimentId)
                .build());

        SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder()
                .scenarioId(createExperimentRequest.getScenarioId()).build());

        if (scenario.getRequiredJavaAgent()) {
            ActivityTaskDTO activityTaskPrepare = ActivityTaskDTO.builder()
                    .manualChecked(false)
                    .sceneId(scenario.getScenarioId())
                    .sceneCode(JavaAgentPrepare.CODE)
                    .arguments(createExperimentRequest.getParameters())
                    .build();

            experimentActivityRepository.insert(ExperimentActivityDO.builder()
                    .experimentId(experimentId)
                    .flowId(flowId)
                    .phase(ChaosConstant.PHASE_RECOVER)
                    .sceneCode(JavaAgentPrepare.CODE)
                    .activityDefinition(JsonUtils.writer().writeValueAsString(activityTaskPrepare))
                    .build());
        }

        ActivityTaskDTO activityTaskDTO = ActivityTaskDTO.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode())
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_ATTACK)
                .sceneCode(scenario.getCode())
                .activityName(scenario.getName())
                .activityDefinition(JsonUtils.writer().writeValueAsString(activityTaskDTO))
                .build());

        ActivityTaskDTO activityTaskDTORecover = ActivityTaskDTO.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_RECOVER)
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .activityName(scenario.getName())
                .activityDefinition(JsonUtils.writer().writeValueAsString(activityTaskDTORecover))
                .build());

        return ExperimentResponse.builder()
                .experimentId(experimentDO.getId())
                .build();
    }

    @Override
    public void deleteExperiment(ExperimentRequest experimentRequest) {
        experimentRepository.deleteById(experimentRequest.getExperimentId());
    }

    @Override
    public ExperimentResponse getExperimentById(ExperimentRequest experimentRequest) {
        ExperimentDO experimentDO = experimentRepository.selectById(experimentRequest.getExperimentId()).orElseThrow(
                () -> new BizException(ExceptionMessageEnum.EXPERIMENT_NOT_FOUNT)
        );
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectLatestByExperimentId(experimentDO.getId());

        ExperimentResponse experimentResponse = ExperimentResponse.builder()
                .experimentId(experimentDO.getId())
                .experimentName(experimentDO.getName())
                .dimension(experimentDO.getDimension())
                .createTime(experimentDO.getGmtCreate())
                .modifyTime(experimentDO.getGmtModified())
                .build();

        if (StrUtil.isNotBlank(experimentDO.getMetric())) {
            List<MetricModel> metricModels = JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
            }, experimentDO.getMetric());
            experimentResponse.setMetrics(metricModels);
        }

        if (experimentTaskDO != null) {
            experimentResponse.setLastTaskId(experimentTaskDO.getId());
            experimentResponse.setLastTaskStartTime(experimentTaskDO.getGmtStart());
            experimentResponse.setLastTaskEndTime(experimentTaskDO.getGmtEnd());
            experimentResponse.setLastTaskStatus(experimentTaskDO.getRunStatus());
            experimentResponse.setLastTaskResult(experimentTaskDO.getResultStatus());
        }
        List<ExperimentMiniFlowGroupDO> groupDOS = experimentMiniFlowGroupRepository.selectByExperiment(experimentDO.getId());

        // todo
        ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO = groupDOS.get(0);
        String hosts = experimentMiniFlowGroupDO.getHosts();

        List<MachineResponse> machineResponses = JsonUtils.readValue(new TypeReference<List<MachineResponse>>() {
        }, hosts);
        experimentResponse.setMachines(machineResponses);

        List<ExperimentActivity> experimentActivities = experimentActivityService.selectAttackByExperimentId(experimentDO.getId());

        experimentResponse.setScenarios(experimentActivities.stream().map(experimentActivity ->
                SceneResponse.builder()
                        .code(experimentActivity.getSceneCode())
                        .name(experimentActivity.getActivityName())
                        .parameters(
                                JsonUtils.readValue(ActivityTaskDTO.class, experimentActivity.getActivityDefinition())
                                        .getArguments()
                                        .entrySet().stream().map(entry -> SceneParamResponse.builder()
                                        .name(entry.getKey())
                                        .value(entry.getValue())
                                        .build()).collect(Collectors.toList())
                        )
                        .build()
        ).collect(Collectors.toList()));
        return experimentResponse;
    }

    @Override
    @Transactional
    public ExperimentResponse updateExperiment(CreateExperimentRequest createExperimentRequest) {
        Long experimentId = createExperimentRequest.getExperimentId();

        ExperimentDO experimentDO = ExperimentDO.builder()
                .name(createExperimentRequest.getExperimentName())
                .metric(createExperimentRequest.getMetrics())
                .build();

       experimentRepository.updateByPrimaryKey(experimentId, experimentDO);

        // default mini flow group
        List<DeviceRequest> machines = createExperimentRequest.getMachines();
        List<DeviceMeta> deviceMetas = machines.stream().map(machine -> deviceRepository.selectById(machine.getDeviceId()).map(deviceDO ->
                        DeviceMeta.builder().deviceId(deviceDO.getId())
                                .deviceType(deviceDO.getType())
                                .hostname(deviceDO.getHostname())
                                .ip(deviceDO.getIp()).build()
                ).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT))
        ).collect(Collectors.toList());


        experimentMiniFlowGroupRepository.updateByPrimaryKey(experimentId, ExperimentMiniFlowGroupDO.builder()
                .hosts(JsonUtils.writeValueAsString(deviceMetas))
                .build());

        experimentMiniFlowRepository.deleteByExperimentId(experimentId);

        List<ExperimentMiniFlowGroupDO> experimentMiniFlowGroup = experimentMiniFlowGroupRepository.selectByExperiment(experimentId);
        Long flowGroupId = experimentMiniFlowGroup.get(0).getId();

        // default mini flow
        Long flowId = experimentMiniFlowRepository.insert(ExperimentMiniFlowDO.builder()
                .groupId(flowGroupId)
                .experimentId(experimentId)
                .build());

        SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder()
                .scenarioId(createExperimentRequest.getScenarioId()).build());

        if (scenario.getRequiredJavaAgent()) {
            ActivityTaskDTO activityTaskPrepare = ActivityTaskDTO.builder()
                    .manualChecked(false)
                    .sceneId(scenario.getScenarioId())
                    .sceneCode(JavaAgentPrepare.CODE)
                    .arguments(createExperimentRequest.getParameters())
                    .build();

            experimentActivityRepository.insert(ExperimentActivityDO.builder()
                    .experimentId(experimentId)
                    .flowId(flowId)
                    .phase(ChaosConstant.PHASE_RECOVER)
                    .sceneCode(JavaAgentPrepare.CODE)
                    .activityDefinition(JsonUtils.writeValueAsString(activityTaskPrepare))
                    .build());
        }

        ActivityTaskDTO activityTaskDTO = ActivityTaskDTO.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode())
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_ATTACK)
                .sceneCode(scenario.getCode())
                .activityName(scenario.getName())
                .activityDefinition(JsonUtils.writeValueAsString(activityTaskDTO))
                .build());

        ActivityTaskDTO activityTaskDTORecover = ActivityTaskDTO.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_RECOVER)
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .activityName(scenario.getName())
                .activityDefinition(JsonUtils.writeValueAsString(activityTaskDTORecover))
                .build());

        return ExperimentResponse.builder()
                .experimentId(experimentDO.getId())
                .build();
    }

    @Override
    public ExperimentTaskResponse executeExperiment(ExperimentRequest experimentRequest) {
        Long experimentId = experimentRequest.getExperimentId();
        return experimentTaskService.createExperimentTask(experimentId);
    }

    @Override
    public void finishExperiment(ExperimentTaskRequest experimentTaskRequest) {
        experimentTaskService.stopExperimentTask(experimentTaskRequest.getTaskId());
    }


    @Override
    public List<ExperimentResponse> getExperimentsPageable(ExperimentRequest experimentRequest) {

        PageUtils.startPage(experimentRequest);
        List<ExperimentDO> experimentDOS = experimentRepository.fuzzySelect(
                ExperimentDO.builder().name(experimentRequest.getExperimentName()).build());

        if (CollectionUtil.isEmpty(experimentDOS)) {
            return Collections.emptyList();
        }
        return experimentDOS.stream().map(experimentDO -> {

            ExperimentResponse experimentResponse = ExperimentResponse.builder()
                    .experimentId(experimentDO.getId())
                    .experimentName(experimentDO.getName())
                    .createTime(experimentDO.getGmtCreate())
                    .modifyTime(experimentDO.getGmtModified())
                    .build();
            ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectLatestByExperimentId(experimentDO.getId());

            if (experimentTaskDO != null) {
                experimentResponse.setLastTaskId(experimentTaskDO.getId());
                experimentResponse.setLastTaskStartTime(experimentTaskDO.getGmtStart());
                experimentResponse.setLastTaskEndTime(experimentTaskDO.getGmtEnd());
                experimentResponse.setLastTaskStatus(experimentTaskDO.getRunStatus());
                experimentResponse.setLastTaskResult(experimentTaskDO.getResultStatus());
            }

            List<ExperimentActivity> experimentActivities = experimentActivityService.selectAttackByExperimentId(experimentDO.getId());

            experimentResponse.setScenarios(experimentActivities.stream().map(experimentActivity ->
                    SceneResponse.builder()
                            .code(experimentActivity.getSceneCode())
                            .name(experimentActivity.getActivityName())
                            .parameters(
                                    JsonUtils.readValue(ActivityTaskDTO.class, experimentActivity.getActivityDefinition())
                                            .getArguments()
                                            .entrySet().stream().map(entry -> SceneParamResponse.builder()
                                            .name(entry.getKey())
                                            .value(entry.getValue())
                                            .build()).collect(Collectors.toList())
                            )
                            .build()
            ).collect(Collectors.toList()));
            return experimentResponse;
        }).collect(Collectors.toList());
    }
}
