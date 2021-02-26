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

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.enums.ExperimentDimension;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.dao.model.*;
import com.alibaba.chaosblade.platform.dao.repository.*;
import com.alibaba.chaosblade.platform.metric.MetricChartLineRequest;
import com.alibaba.chaosblade.platform.metric.MetricService;
import com.alibaba.chaosblade.platform.service.ExperimentActivityTaskService;
import com.alibaba.chaosblade.platform.service.ExperimentMiniFlowService;
import com.alibaba.chaosblade.platform.service.model.metric.MetricModel;
import com.alibaba.chaosblade.platform.service.task.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.alibaba.chaosblade.platform.cmmon.enums.RunStatus.FINISHED;
import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@Service
public class ExperimentActivityTaskServiceImpl implements ExperimentActivityTaskService {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentMiniFlowService experimentMiniFlowService;

    @Autowired
    private MetricService metricService;

    @Autowired
    private MetricTaskRepository metricTaskRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceNodeRepository deviceNodeRepository;

    @Autowired
    private DevicePodRepository devicePodRepository;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ActivityTaskExecuteContext activityTaskExecuteContext;

    @Override
    public void manualChecked(Long activityTaskId) {

    }

    @Override
    public void executeActivityTasks(List<ExperimentActivityTaskDO> experimentActivityTasks, ExperimentTaskDO experimentTaskDO) {
        ActivityTaskExecutePipeline pipeline = new ActivityTaskExecutePipeline();
        ExperimentDO experimentDO = experimentRepository.selectById(experimentTaskDO.getExperimentId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.EXPERIMENT_NOT_FOUNT));

        for (ExperimentActivityTaskDO experimentActivityTask : experimentActivityTasks) {

            List<DeviceMeta> deviceMetas = experimentMiniFlowService.selectExperimentDeviceByFlowId(experimentActivityTask.getFlowId());
            String activityDefinition = experimentActivityTask.getRunParam();

            ActivityTask activityTask = JsonUtils.readValue(ActivityTask.class, activityDefinition);

            activityTask.setDeviceMetas(deviceMetas);
            activityTask.setFlowId(experimentActivityTask.getFlowId());
            activityTask.setExperimentTaskId(experimentActivityTask.getExperimentTaskId());
            activityTask.setActivityId(experimentActivityTask.getActivityId());
            activityTask.setActivityTaskId(experimentActivityTask.getId());
            activityTask.setPreActivityTaskId(experimentActivityTask.getPreActivityTaskId());
            activityTask.setNextActivityTaskId(experimentActivityTask.getNextActivityTaskId());
            activityTask.setPhase(experimentActivityTask.getPhase());
            activityTask.setExperimentDimension(EnumUtil.fromString(ExperimentDimension.class, experimentDO.getDimension().toUpperCase()));

            pipeline.addLast(activityTask);
            if (activityTask.getManualChecked()) {
                // todo
                break;
            }
        }

        // experiment before notify
        activityTaskExecuteContext.addExperimentTaskStartListener(pipeline, (context, activityTask) -> {

            Logger logger = context.getContextLogger();
            ExperimentTaskDO experimentTask = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                    .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));

            if (experimentTask.getRunStatus().equals(RunStatus.READY.getValue())) {
                logger.info("开始执行演练, 任务ID：{}", activityTask.getExperimentTaskId());
                experimentTaskRepository.updateByPrimaryKey(experimentTaskDO.getId(), ExperimentTaskDO.builder()
                        .gmtStart(DateUtil.date())
                        .runStatus(RunStatus.RUNNING.getValue())
                        .build());

                for (DeviceMeta deviceMeta : activityTask.getDeviceMetas()) {
                    // update device last experiment
                    deviceRepository.updateByPrimaryKey(deviceMeta.getDeviceId(),
                            DeviceDO.builder().lastExperimentTime(DateUtil.date())
                                    .isExperimented(true)
                                    .lastTaskId(activityTask.getExperimentTaskId())
                                    .build()
                    );
                }

                String metric = experimentTaskDO.getMetric();
                if (StrUtil.isNotBlank(metric)) {
                    logger.info("开始接入监控, 任务ID：{}", activityTask.getExperimentTaskId());
                    List<MetricModel> metricModels = JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
                    }, metric);
                    metricModels.forEach(metricModel -> metric(context, metricModel, activityTask));
                } else {
                    logger.info("未接入监控, 任务ID：{}", activityTask.getExperimentTaskId());
                }
            }
        });

        // experiment after notify
        activityTaskExecuteContext.addExperimentTaskCompleteListener(pipeline, (context, activityTask, e) -> {
            Logger logger = context.getContextLogger();
            if (activityTask.isAttackPhase()) {
                ExperimentTaskDO experimentTask = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                        .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
                if (experimentTask.getResultStatus() == null) {
                    experimentTaskRepository.updateByPrimaryKey(activityTask.getExperimentTaskId(), ExperimentTaskDO.builder()
                            .resultStatus(ResultStatus.FAILED.getValue())
                            .errorMessage(e.getMessage())
                            .build());
                }
            }
            if (activityTask.isRecoverPhase()) {
                ExperimentTaskDO taskDO = ExperimentTaskDO.builder()
                        .runStatus(FINISHED.getValue())
                        .gmtEnd(DateUtil.date())
                        .build();
                if (e != null) {
                    logger.error("演练结束，任务ID：{}, 恢复失败: {}", activityTask.getExperimentTaskId(), e.getMessage());
                    log.error(e.getMessage(), e);
                    taskDO.setResultStatus(ResultStatus.FAILED.getValue());
                    taskDO.setErrorMessage(e.getMessage());
                } else {
                    logger.info("演练结束，任务ID：{}, 恢复成功", activityTask.getExperimentTaskId());
                    taskDO.setResultStatus(ResultStatus.SUCCESS.getValue());
                }
                experimentTaskRepository.updateByPrimaryKey(activityTask.getExperimentTaskId(), taskDO);

                for (DeviceMeta deviceMeta : activityTask.getDeviceMetas()) {
                    if (deviceMeta.getDeviceType() == null) {
                        break;
                    } else {
                        DeviceType deviceType = DeviceType.transByCode(deviceMeta.getDeviceType());
                        switch (deviceType) {
                            case HOST:
                                // update device last experiment
                                deviceRepository.updateByPrimaryKey(deviceMeta.getDeviceId(),
                                        DeviceDO.builder()
                                                .isExperimented(true)
                                                .lastExperimentTime(DateUtil.date())
                                                .lastTaskId(activityTask.getExperimentTaskId())
                                                .lastTaskStatus(taskDO.getResultStatus())
                                                .build()
                                );
                                break;
                            case NODE:
                                deviceNodeRepository.selectByNodeName(deviceMeta.getNodeName()).ifPresent(node ->
                                        deviceRepository.updateByPrimaryKey(node.getDeviceId(),
                                                DeviceDO.builder().lastExperimentTime(DateUtil.date())
                                                        .isExperimented(true)
                                                        .lastTaskId(activityTask.getExperimentTaskId())
                                                        .lastTaskStatus(taskDO.getResultStatus())
                                                        .build()
                                        ));
                                break;
                            case POD:
                                devicePodRepository.selectByNameAndNamespace(deviceMeta.getNamespace(), deviceMeta.getPodName())
                                        .ifPresent(pod ->
                                                deviceRepository.updateByPrimaryKey(pod.getDeviceId(),
                                                        DeviceDO.builder().lastExperimentTime(DateUtil.date())
                                                                .isExperimented(true)
                                                                .lastTaskId(activityTask.getExperimentTaskId())
                                                                .lastTaskStatus(taskDO.getResultStatus())
                                                                .build()
                                                )
                                        );
                                break;

                        }
                    }

                }
            }
        });

        // fire experiment
        activityTaskExecuteContext.fireExecute(pipeline);
    }

    private void metric(ActivityTaskExecuteContext context, MetricModel metricModel, ActivityTask activityTask) {
        Logger logger = context.getContextLogger();
        context.timer().newTimeout(timeout -> {

            metricService.selectChartLine(MetricChartLineRequest.builder()
                    .devices(activityTask.getDeviceMetas())
                    .startTime(DateUtil.date())
                    .endTime(DateUtil.date().offset(DateField.SECOND, +10))
                    .categoryCode(metricModel.getCode())
                    .params(metricModel.getParams())
                    .build())
                    .handleAsync((r, e) -> {
                        if (e != null) {
                            logger.error("获取监控数据失败, 任务ID：{}, 机器信息：{}, 异常: {}",
                                    activityTask.getExperimentTaskId(),
                                    JsonUtils.writeValueAsString(activityTask.getDeviceMetas()),
                                    e.getMessage());
                        } else {
                            metricTaskRepository.saveBatch(
                                    r.stream().flatMap(v -> v.getMetricChartLines().stream().map(metricChartLine ->
                                            MetricTaskDO.builder()
                                                    .ip(v.getDeviceMeta().getIp())
                                                    .deviceId(v.getDeviceMeta().getDeviceId())
                                                    .hostname(v.getDeviceMeta().getHostname())
                                                    .categoryId(metricModel.getCategoryId())
                                                    .categoryCode(metricModel.getCode())
                                                    .date(metricChartLine.getTime())
                                                    .value(metricChartLine.getValue())
                                                    .taskId(activityTask.getExperimentTaskId())
                                                    .metric(v.getMetric())
                                                    .build()
                                    )).collect(Collectors.toList()));
                        }
                        return null;
                    }, context.executor());


            Byte status = experimentTaskRepository.selectById(activityTask.getExperimentTaskId())
                    .map(ExperimentTaskDO::getRunStatus)
                    .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
            RunStatus runStatus = RunStatus.parse(status);
            if (runStatus != FINISHED) {
                metric(context, metricModel, activityTask);
            }
        }, 10, TimeUnit.SECONDS);
    }

}
