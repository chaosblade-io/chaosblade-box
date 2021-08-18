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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.enums.ChaosTools;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.*;
import com.alibaba.chaosblade.box.common.DeviceMeta;
import com.alibaba.chaosblade.box.common.constants.ChaosConstant;
import com.alibaba.chaosblade.box.common.enums.ExperimentDimension;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.common.utils.Preconditions;
import com.alibaba.chaosblade.box.common.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.page.PageUtils;
import com.alibaba.chaosblade.box.service.model.MachineResponse;
import com.alibaba.chaosblade.box.service.model.device.DeviceNodeResponse;
import com.alibaba.chaosblade.box.service.model.device.DevicePodResponse;
import com.alibaba.chaosblade.box.service.model.device.DeviceRequest;
import com.alibaba.chaosblade.box.service.model.experiment.*;
import com.alibaba.chaosblade.box.service.model.experiment.activity.ExperimentActivity;
import com.alibaba.chaosblade.box.service.model.metric.MetricModel;
import com.alibaba.chaosblade.box.service.model.scene.SceneRequest;
import com.alibaba.chaosblade.box.service.model.scene.SceneResponse;
import com.alibaba.chaosblade.box.service.model.scene.param.SceneParamResponse;
import com.alibaba.chaosblade.box.service.model.scene.prepare.JavaAgentPrepare;
import com.alibaba.chaosblade.box.service.task.ActivityTask;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.box.common.constants.ChaosConstant.CHAOS_DEFAULT_NA;
import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.DEVICE_NOT_FOUNT;
import static com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum.EXPERIMENT_DEVICE_IS_NULL;

/**
 * @author yefei
 */
@Service
public class ExperimentServiceImpl implements ExperimentService {

    @Autowired
    private ExperimentMapper experimentMapper;

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

    @Autowired
    private DeviceService deviceService;

    @Override
    @Transactional
    public ExperimentResponse createExperiment(CreateExperimentRequest createExperimentRequest) throws Exception {

        // experiment
        ExperimentDO experimentDO = ExperimentDO.builder()
                .name(createExperimentRequest.getExperimentName())
                .metric(createExperimentRequest.getMetrics())
                .dimension(createExperimentRequest.getDimension())
                .build();
        Long experimentId = experimentRepository.insert(experimentDO);

        List<DeviceMeta> deviceMetas = getDeviceMetas(createExperimentRequest);

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

        createExperimentRequest.setExperimentId(experimentId);
        experimentPhase(createExperimentRequest, flowId);

        return ExperimentResponse.builder()
                .experimentId(experimentDO.getId())
                .build();
    }

    private List<DeviceMeta> getDeviceMetas(CreateExperimentRequest createExperimentRequest) {
        ExperimentDimension dimension = EnumUtil.fromString(ExperimentDimension.class, createExperimentRequest.getDimension().toUpperCase());
        List<DeviceMeta> deviceMetas = new ArrayList<>();
        SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder().scenarioId(createExperimentRequest.getScenarioId()).build());
        String original = SceneCodeParseUtil.getOriginal(scenario.getCode());

        switch (dimension) {
            case HOST:
                Preconditions.checkArgument(CollUtil.isEmpty(createExperimentRequest.getMachines()), EXPERIMENT_DEVICE_IS_NULL);
                // default mini flow group
                List<DeviceRequest> machines = createExperimentRequest.getMachines();
                return machines.stream().map(machine -> deviceRepository.selectById(machine.getDeviceId()).map(deviceDO ->
                                DeviceMeta.builder().deviceId(deviceDO.getId())
                                        .deviceType(deviceDO.getType())
                                        .hostname(deviceDO.getHostname())
                                        .ip(deviceDO.getIp()).build()
                        ).orElseThrow(() -> new BizException(DEVICE_NOT_FOUNT))
                ).collect(Collectors.toList());
            case NODE:
                List<DeviceMeta> nodes = createExperimentRequest.getMachines().stream().map(machine -> {
                            if (machine.getDeviceId() != null) {
                                DeviceNodeResponse node = deviceService.getNodeByDeviceId(machine.getDeviceId());
                                return DeviceMeta.builder().deviceId(node.getDeviceId())
                                        .clusterId(node.getClusterId())
                                        .deviceType(node.getType())
                                        .hostname(node.getHostname())
                                        .nodeName(node.getNodeName())
                                        .ip(node.getNodeIp()).build();
                            } else {
                                return DeviceMeta.builder().deviceId(machine.getDeviceId())
                                        .deviceType(dimension.getDeviceType().getCode())
                                        .nodeName(machine.getNodeName())
                                        .build();
                            }
                        }
                ).collect(Collectors.toList());
                if (original.equals(ChaosTools.CHAOS_BLADE.getName())) {
                    createExperimentRequest.getParameters().put("names", nodes.stream().map(DeviceMeta::getNodeName).collect(Collectors.joining(",")));
                } else if (original.equals(ChaosTools.LITMUS_CHAOS.getName())) {
                    createExperimentRequest.getParameters().put("TARGET_NODES", nodes.stream().map(DeviceMeta::getNodeName).collect(Collectors.joining(",")));
                }
                return nodes;
            case POD:
            case CONTAINER:
                List<DeviceMeta> list = createExperimentRequest.getMachines().stream().map(machine -> {
                            if (machine.getDeviceId() != null) {
                                DevicePodResponse pod = deviceService.getPodByDeviceId(machine.getDeviceId());
                                return DeviceMeta.builder().deviceId(pod.getDeviceId())
                                        .clusterId(pod.getClusterId())
                                        .deviceType(pod.getType())
                                        .hostname(pod.getHostname())
                                        .nodeName(pod.getNodeName())
                                        .namespace(pod.getNamespace())
                                        .podName(pod.getPodName())
                                        .containerName(machine.getContainerName())
                                        .ip(pod.getPodIp()).build();
                            } else {
                                return DeviceMeta.builder().deviceId(machine.getDeviceId())
                                        .deviceType(dimension.getDeviceType().getCode())
                                        .hostname(machine.getHostname())
                                        .nodeName(machine.getNodeName())
                                        .namespace(machine.getNamespace())
                                        .podName(machine.getPodName())
                                        .containerName(machine.getContainerName())
                                        .build();
                            }
                        }
                ).collect(Collectors.toList());
                if (original.equals(ChaosTools.CHAOS_BLADE.getName())) {
                    createExperimentRequest.getParameters().put("names", list.stream().map(DeviceMeta::getPodName).distinct().collect(Collectors.joining(",")));
                    String containerNames = list.stream().map(DeviceMeta::getContainerName).distinct().collect(Collectors.joining(","));
                    if (StrUtil.isNotBlank(containerNames) && !"null".equals(containerNames)) {
                        createExperimentRequest.getParameters().put("container-names", containerNames);
                    }
                    if (createExperimentRequest.getParameters().get("namespace") == null) {
                        createExperimentRequest.getParameters().put("namespace", list.get(0).getNamespace());
                    }
                } else if (original.equals(ChaosTools.LITMUS_CHAOS.getName())) {
                    createExperimentRequest.getParameters().put("TARGET_PODS", list.stream().map(DeviceMeta::getPodName).distinct().collect(Collectors.joining(",")));
                    String containerNames = list.stream().map(DeviceMeta::getContainerName).distinct().collect(Collectors.joining(","));
                    if (StrUtil.isNotBlank(containerNames) && !"null".equals(containerNames)) {
                        createExperimentRequest.getParameters().put("TARGET_CONTAINER", containerNames);
                    }
                }
                return list;
            case APPLICATION:
                // todo
        }
        return deviceMetas;
    }

    private void experimentPhase(CreateExperimentRequest createExperimentRequest, Long flowId) {

        Long experimentId = createExperimentRequest.getExperimentId();

        SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder().scenarioId(createExperimentRequest.getScenarioId()).build());

        // prepare
        if (scenario.getPreScenarioId() != null) {
            SceneResponse pre = sceneService.getScenarioById(SceneRequest.builder().scenarioId(scenario.getPreScenarioId()).build());
            Map<String, String> parameters = createExperimentRequest.getParameters();
            Map<String, String> preParameters = MapUtil.filter(parameters, JavaAgentPrepare.PID, JavaAgentPrepare.PROCESS);

            ActivityTask activityTaskPrepare = ActivityTask.builder()
                    .manualChecked(false)
                    .sceneId(pre.getScenarioId())
                    .sceneCode(pre.getCode())
                    .arguments(preParameters)
                    .build();

            experimentActivityRepository.insert(ExperimentActivityDO.builder()
                    .activityName(pre.getCode())
                    .experimentId(experimentId)
                    .flowId(flowId)
                    .phase(ChaosConstant.PHASE_PREPARE)
                    .sceneCode(pre.getCode())
                    .activityDefinition(JsonUtils.writeValueAsString(activityTaskPrepare))
                    .build());
        }

        List<MetricModel> metricModels = Optional.ofNullable(createExperimentRequest.getMetrics())
                .map(metric -> JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
                }, metric)).orElse(null);

        // attack
        ActivityTask activityTask = ActivityTask.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode())
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        if (CollUtil.isNotEmpty(metricModels)) {
            activityTask.setWaitOfBefore(30000L);
        }

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_ATTACK)
                .sceneCode(scenario.getCode())
                .activityName(scenario.getName())
                .activityDefinition(JsonUtils.writeValueAsString(activityTask))
                .build());

        // recover
        ActivityTask activityTaskRecover = ActivityTask.builder()
                .manualChecked(true)
                .sceneId(scenario.getScenarioId())
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .target(SceneCodeParseUtil.getTarget(scenario.getCode()))
                .action(SceneCodeParseUtil.getAction(scenario.getCode()))
                .arguments(createExperimentRequest.getParameters())
                .build();

        if (CollUtil.isNotEmpty(metricModels)) {
            activityTaskRecover.setWaitOfAfter(30000L);
        }

        experimentActivityRepository.insert(ExperimentActivityDO.builder()
                .experimentId(experimentId)
                .flowId(flowId)
                .phase(ChaosConstant.PHASE_RECOVER)
                .sceneCode(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .activityName(scenario.getCode() + ChaosConstant.CHAOS_DESTROY_SUFFIX)
                .activityDefinition(JsonUtils.writeValueAsString(activityTaskRecover))
                .build());
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

        ExperimentResponse experimentResponse = ExperimentResponse.builder()
                .experimentId(experimentDO.getId())
                .experimentName(experimentDO.getName())
                .dimension(experimentDO.getDimension())
                .createTime(experimentDO.getGmtCreate())
                .modifyTime(experimentDO.getGmtModified())
                .taskCount(experimentTaskRepository.selectByExperimentId(experimentDO.getId()).size())
                .build();

        if (StrUtil.isNotBlank(experimentDO.getMetric())) {
            List<MetricModel> metricModels = JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
            }, experimentDO.getMetric());
            experimentResponse.setMetrics(metricModels);
        }

        experimentTaskRepository.selectById(experimentDO.getTaskId())
                .ifPresent(experimentTaskDO -> {
                    experimentResponse.setLastTaskId(experimentTaskDO.getId());
                    experimentResponse.setLastTaskStartTime(experimentTaskDO.getGmtStart());
                    experimentResponse.setLastTaskEndTime(experimentTaskDO.getGmtEnd());
                    experimentResponse.setLastTaskStatus(experimentTaskDO.getRunStatus());
                    experimentResponse.setLastTaskResult(experimentTaskDO.getResultStatus());
                });

        List<ExperimentMiniFlowGroupDO> groupDOS = experimentMiniFlowGroupRepository.selectByExperiment(experimentDO.getId());

        // todo
        ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO = groupDOS.get(0);
        String hosts = experimentMiniFlowGroupDO.getHosts();

        List<MachineResponse> machineResponses = JsonUtils.readValue(new TypeReference<List<MachineResponse>>() {
        }, hosts);
        experimentResponse.setMachines(machineResponses);

        List<ExperimentActivity> experimentActivities = experimentActivityService.selectAttackByExperimentId(experimentDO.getId());
        experimentActivities.stream().forEach(experimentActivity -> {
            ActivityTask activityTask = JsonUtils.readValue(ActivityTask.class, experimentActivity.getActivityDefinition());
            SceneResponse scenario = sceneService.getScenarioById(SceneRequest.builder().scenarioId(activityTask.getSceneId()).build());
            scenario.setParameters(scenario.getParameters().stream().map(
                    sceneParam -> SceneParamResponse.builder()
                            .parameterId(sceneParam.getParameterId())
                            .paramName(sceneParam.getParamName())
                            .name(sceneParam.getParamName())
                            .alias(sceneParam.getAlias())
                            .description(sceneParam.getDescription())
                            .value(Optional.ofNullable(activityTask.getArguments())
                                    .map(arguments -> arguments.get(sceneParam.getParamName())).orElse(null))
                            .component(sceneParam.getComponent())
                            .build()).collect(Collectors.toList()));
            experimentActivity.setScene(scenario);
        });

        experimentResponse.setActivities(experimentActivities);
        experimentResponse.setScenarios(experimentActivities.stream()
                .filter(experimentActivity ->
                        experimentActivity.getPhase().equals(ChaosConstant.PHASE_ATTACK))
                .map(experimentActivity ->
                        {
                            ActivityTask activityTask = JsonUtils.readValue(ActivityTask.class, experimentActivity.getActivityDefinition());
                            SceneResponse scenario = experimentActivity.getScene();
                            return SceneResponse.builder()
                                    .code(experimentActivity.getSceneCode())
                                    .name(experimentActivity.getActivityName())
                                    .scenarioId(activityTask.getSceneId())
                                    .original(scenario.getOriginal())
                                    .version(scenario.getVersion())
                                    .categories(scenario.getCategories())
                                    .description(scenario.getDescription())
                                    .parameters(scenario.getParameters().stream().map(
                                            sceneParam -> SceneParamResponse.builder()
                                                    .parameterId(sceneParam.getParameterId())
                                                    .description(sceneParam.getDescription())
                                                    .component(sceneParam.getComponent())
                                                    .name(sceneParam.getName())
                                                    .alias(sceneParam.getAlias())
                                                    .value(Optional.ofNullable(activityTask.getArguments())
                                                            .map(arguments -> arguments.get(sceneParam.getParamName())).orElse(null))
                                                    .build()).collect(Collectors.toList()))
                                    .build();
                        }
                ).collect(Collectors.toList()));
        return experimentResponse;
    }

    @Override
    @Transactional
    public ExperimentResponse updateExperiment(CreateExperimentRequest createExperimentRequest) {
        Long experimentId = createExperimentRequest.getExperimentId();

        ExperimentDO experimentDO = ExperimentDO.builder()
                .name(createExperimentRequest.getExperimentName())
                .dimension(createExperimentRequest.getDimension())
                .metric(createExperimentRequest.getMetrics())
                .build();

        experimentRepository.updateByPrimaryKey(experimentId, experimentDO);

        List<DeviceMeta> deviceMetas = getDeviceMetas(createExperimentRequest);

        experimentMiniFlowGroupRepository.updateByExperimentId(experimentId, ExperimentMiniFlowGroupDO.builder()
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

        experimentActivityRepository.deleteExperimentId(experimentId);

        experimentPhase(createExperimentRequest, flowId);

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

        List<ExperimentDO> experimentDOS;
        PageUtils.startPage(experimentRequest);
        if (experimentRequest.getLastTaskStatus() == null && experimentRequest.getLastTaskResult() == null) {
            experimentDOS = experimentRepository.fuzzySelect(
                    ExperimentDO.builder().name(experimentRequest.getExperimentName()).build());
        } else {
            if (experimentRequest.getLastTaskStatus() == -1) {
                experimentRequest.setLastTaskStatus(null);
            }
            if (experimentRequest.getLastTaskResult() == -1) {
                experimentRequest.setLastTaskResult(null);
            }
            experimentDOS = experimentMapper.selectExperimentAndStatus(ExperimentDO.builder()
                    .name(experimentRequest.getExperimentName())
                    .lastTaskStatus(experimentRequest.getLastTaskStatus())
                    .lastTaskResult(experimentRequest.getLastTaskResult())
                    .build());
        }

        if (CollectionUtil.isEmpty(experimentDOS)) {
            return Collections.emptyList();
        }
        return experimentDOS.stream().map(experimentDO -> {

            ExperimentResponse experimentResponse = ExperimentResponse.builder()
                    .experimentId(experimentDO.getId())
                    .experimentName(experimentDO.getName())
                    .createTime(experimentDO.getGmtCreate())
                    .modifyTime(experimentDO.getGmtModified())
                    .taskCount(experimentTaskRepository.selectByExperimentId(experimentDO.getId()).size())
                    .build();

            experimentTaskRepository.selectById(experimentDO.getTaskId())
                    .ifPresent(experimentTaskDO -> {
                        experimentResponse.setLastTaskId(experimentTaskDO.getId());
                        experimentResponse.setLastTaskStartTime(experimentTaskDO.getGmtStart());
                        experimentResponse.setLastTaskEndTime(experimentTaskDO.getGmtEnd());
                        experimentResponse.setLastTaskStatus(experimentTaskDO.getRunStatus());
                        experimentResponse.setLastTaskResult(experimentTaskDO.getResultStatus());
                    });

            List<ExperimentActivity> experimentActivities = experimentActivityService.selectAttackByExperimentId(experimentDO.getId());

            experimentResponse.setScenarios(experimentActivities.stream()
                    .filter(experimentActivity ->
                            experimentActivity.getPhase().equals(ChaosConstant.PHASE_ATTACK))
                    .map(experimentActivity ->
                            {
                                ActivityTask activityTask = JsonUtils.readValue(ActivityTask.class, experimentActivity.getActivityDefinition());
                                return SceneResponse.builder()
                                        .code(experimentActivity.getSceneCode())
                                        .name(experimentActivity.getActivityName())
                                        .scenarioId(activityTask.getSceneId())
                                        .categories(
                                                sceneService.getScenarioById(SceneRequest.builder().scenarioId(activityTask.getSceneId()).build())
                                                        .getCategories()
                                        )
                                        .parameters(Optional.ofNullable(activityTask.getArguments()).map((arguments) ->
                                                arguments.entrySet().stream().map(entry -> SceneParamResponse.builder()
                                                        .name(entry.getKey())
                                                        .value(entry.getValue())
                                                        .build()).collect(Collectors.toList())).orElse(null)
                                        ).build();
                            }
                    ).collect(Collectors.toList()));
            return experimentResponse;
        }).collect(Collectors.toList());
    }

    @Override
    public ExperimentStatisticsResponse getExperimentTotalStatistics() {

        return ExperimentStatisticsResponse.builder()
                .totals(experimentMapper.selectCount(QueryWrapperBuilder.build()))
                .prepares(experimentMapper.selectPreparesCount())
                .success(experimentMapper.selectSuccessCount())
                .failed(experimentMapper.selectFailedCount())
                .running(experimentMapper.selectRunningCount())
                .finished(experimentMapper.selectFinishedCount())
                .build();
    }
}
