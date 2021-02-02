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
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.cmmon.enums.ResultStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.RunStatus;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.executor.ExecutorFactory;
import com.alibaba.chaosblade.platform.cmmon.executor.ThreadPoolExecutorFactory;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentActivityTaskDO;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.model.MetricTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.MetricTaskRepository;
import com.alibaba.chaosblade.platform.metric.MetricChartLine;
import com.alibaba.chaosblade.platform.metric.MetricChartLineRequest;
import com.alibaba.chaosblade.platform.metric.MetricService;
import com.alibaba.chaosblade.platform.service.ExperimentActivityTaskService;
import com.alibaba.chaosblade.platform.service.ExperimentMiniFlowService;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.model.metric.MetricModel;
import com.alibaba.chaosblade.platform.service.task.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.alibaba.chaosblade.platform.cmmon.enums.RunStatus.FINISHED;
import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT;

/**
 * @author yefei
 */
@Slf4j
@Service
public class ExperimentActivityTaskServiceImpl implements ExperimentActivityTaskService, InitializingBean {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ExperimentMiniFlowService experimentMiniFlowService;

    @Autowired
    private TimerFactory timerFactory;

    @Autowired
    private MetricService metricService;

    @Autowired
    private MetricTaskRepository metricTaskRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    private ExecutorService executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorFactory executorFactory = new ThreadPoolExecutorFactory();
        executorService = executorFactory.createExecutorService(new ThreadFactory() {

            AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setName("EXPERIMENT-TASK-THREAD-" + atomicInteger.getAndIncrement());
                return thread;
            }
        });
    }

    @Override
    public void manualChecked(Long activityTaskId) {

    }

    @Override
    public void executeActivityTasks(List<ExperimentActivityTaskDO> experimentActivityTasks, ExperimentTaskDO experimentTaskDO) {
        DefaultActivityTaskExecutePipeline pipeline = applicationContext.getBean(DefaultActivityTaskExecutePipeline.class);

        for (ExperimentActivityTaskDO experimentActivityTask : experimentActivityTasks) {

            List<DeviceMeta> deviceMetas = experimentMiniFlowService.selectExperimentDeviceByFlowId(experimentActivityTask.getFlowId());
            String activityDefinition = experimentActivityTask.getRunParam();

            ActivityTaskDTO activityTaskDTO = JsonUtils.readValue(ActivityTaskDTO.class, activityDefinition);

            activityTaskDTO.setDeviceMetas(deviceMetas);
            activityTaskDTO.setFlowId(experimentActivityTask.getFlowId());
            activityTaskDTO.setExperimentTaskId(experimentActivityTask.getExperimentTaskId());
            activityTaskDTO.setSceneCode(experimentActivityTask.getSceneCode());
            activityTaskDTO.setActivityId(experimentActivityTask.getActivityId());
            activityTaskDTO.setActivityTaskId(experimentActivityTask.getId());
            activityTaskDTO.setPreActivityTaskId(experimentActivityTask.getPreActivityTaskId());
            activityTaskDTO.setNextActivityTaskId(experimentActivityTask.getNextActivityTaskId());
            activityTaskDTO.setPhase(experimentActivityTask.getPhase());

            pipeline.addList((ActivityTask) applicationContext.getBean(experimentActivityTask.getPhase(), activityTaskDTO));
            if (activityTaskDTO.getManualChecked()) {
                break;
            }
        }
        DefaultActivityTaskExecuteContext executeContext = applicationContext.getBean(DefaultActivityTaskExecuteContext.class, pipeline, executorService);

        executeContext.addExperimentTaskStartListener((context, activityTaskDTO) -> {

            Logger logger = context.getContextLogger();
            ExperimentTaskDO experimentTask = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                    .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));

            if (experimentTask.getRunStatus().equals(RunStatus.READY.getValue())) {
                logger.info("开始执行演练, 任务ID：{}", activityTaskDTO.getExperimentTaskId());
                experimentTaskRepository.updateByPrimaryKey(experimentTaskDO.getId(), ExperimentTaskDO.builder()
                        .gmtStart(DateUtil.date())
                        .runStatus(RunStatus.RUNNING.getValue())
                        .build());

                for (DeviceMeta deviceMeta : activityTaskDTO.getDeviceMetas()) {
                    // update device last experiment
                    deviceRepository.updateByPrimaryKey(deviceMeta.getDeviceId(),
                            DeviceDO.builder().lastExperimentTime(DateUtil.date())
                                    .isExperimented(true)
                                    .lastTaskId(activityTaskDTO.getExperimentTaskId())
                                    .build()
                    );
                }

                String metric = experimentTaskDO.getMetric();
                if (StrUtil.isNotBlank(metric)) {
                    logger.info("开始接入监控, 任务ID：{}", activityTaskDTO.getExperimentTaskId());
                    List<MetricModel> metricModels = JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
                    }, metric);
                    metricModels.forEach(metricModel -> metric(context, metricModel, activityTaskDTO));
                } else {
                    logger.info("未接入监控, 任务ID：{}", activityTaskDTO.getExperimentTaskId());
                }
            }
        });

        executeContext.addExperimentTaskCompleteListener((context, activityTaskDTO, e) -> {
            Logger logger = context.getContextLogger();
            if (activityTaskDTO.isAttackPhase()) {
                ExperimentTaskDO experimentTask = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                        .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
                if (experimentTask.getResultStatus() == null) {
                    experimentTaskRepository.updateByPrimaryKey(activityTaskDTO.getExperimentTaskId(), ExperimentTaskDO.builder()
                            .resultStatus(ResultStatus.FAILED.getValue())
                            .errorMessage(e.getMessage())
                            .build());
                }
            }
            if (activityTaskDTO.isRecoverPhase()) {
                ExperimentTaskDO taskDO = ExperimentTaskDO.builder()
                        .runStatus(FINISHED.getValue())
                        .gmtEnd(DateUtil.date())
                        .build();
                if (e != null) {
                    logger.error("演练结束，任务ID：{}, 恢复失败: {}", activityTaskDTO.getExperimentTaskId(), e.getMessage());
                    taskDO.setResultStatus(ResultStatus.FAILED.getValue());
                    taskDO.setErrorMessage(e.getMessage());
                } else {
                    logger.info("演练结束，任务ID：{}, 恢复成功", activityTaskDTO.getExperimentTaskId());
                    taskDO.setResultStatus(ResultStatus.SUCCESS.getValue());
                }
                experimentTaskRepository.updateByPrimaryKey(activityTaskDTO.getExperimentTaskId(), taskDO);

                for (DeviceMeta deviceMeta : activityTaskDTO.getDeviceMetas()) {
                    // update device last experiment
                    deviceRepository.updateByPrimaryKey(deviceMeta.getDeviceId(),
                            DeviceDO.builder().lastExperimentTime(DateUtil.date())
                                    .lastTaskId(activityTaskDTO.getExperimentTaskId())
                                    .lastTaskStatus(taskDO.getResultStatus())
                                    .build()
                    );
                }
            }
        });

        executeContext.fireExecute();
    }

    private void metric(ActivityTaskExecuteContext context, MetricModel metricModel, ActivityTaskDTO activityTaskDTO) {
        Logger logger = context.getContextLogger();
        timerFactory.getTimer().newTimeout(timeout -> {

            for (DeviceMeta deviceMeta : activityTaskDTO.getDeviceMetas()) {
                metricService.selectChartLine(MetricChartLineRequest.builder()
                        .startTime(DateUtil.date().offset(DateField.SECOND, -10))
                        .endTime(DateUtil.date().offset(DateField.SECOND, -5))
                        .instance(deviceMeta.getIp())
                        .hostName(deviceMeta.getHostname())
                        .categoryCode(metricModel.getCode())
                        .params(metricModel.getParams())
                        .build())
                        .handleAsync((r, e) -> {
                            if (e != null) {
                                logger.error("获取监控数据失败, 任务ID：{}, 机器：{}, 异常: {}",
                                        activityTaskDTO.getExperimentTaskId(),
                                        deviceMeta.getDeviceId(),
                                        e.getMessage());
                            } else {
                                for (MetricChartLine metricChartLine : r.getMetricChartLines()) {
                                    metricTaskRepository.insert(MetricTaskDO.builder()
                                            .ip(deviceMeta.getIp())
                                            .deviceId(deviceMeta.getDeviceId())
                                            .categoryId(metricModel.getCategoryId())
                                            .hostname(deviceMeta.getHostname())
                                            .categoryCode(metricModel.getCode())
                                            .date(metricChartLine.getTime())
                                            .value(metricChartLine.getValue())
                                            .taskId(activityTaskDTO.getExperimentTaskId())
                                            .build());
                                }
                            }
                            return null;
                        }, context.executor());

            }

            Byte status = experimentTaskRepository.selectById(activityTaskDTO.getExperimentTaskId())
                    .map(ExperimentTaskDO::getRunStatus)
                    .orElseThrow(() -> new BizException(EXPERIMENT_TASK_NOT_FOUNT));
            RunStatus runStatus = RunStatus.parse(status);
            if (runStatus != FINISHED) {
                metric(context, metricModel, activityTaskDTO);
            }
        }, 10, TimeUnit.SECONDS);
    }

}
