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
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.platform.service.model.metric.MetricModel;
import com.alibaba.chaosblade.platform.service.model.metric.MetricParam;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.platform.dao.model.MetricCategoryDO;
import com.alibaba.chaosblade.platform.dao.model.MetricTaskDO;
import com.alibaba.chaosblade.platform.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.platform.dao.repository.MetricCategoryRepository;
import com.alibaba.chaosblade.platform.dao.repository.MetricTaskRepository;
import com.alibaba.chaosblade.platform.service.ExperimentMiniFlowService;
import com.alibaba.chaosblade.platform.service.MetricTaskService;
import com.alibaba.chaosblade.platform.service.model.metric.MetricCategoryResponse;
import com.alibaba.chaosblade.platform.service.model.metric.MetricTask;
import com.alibaba.chaosblade.platform.service.model.metric.MetricTaskRequest;
import com.alibaba.chaosblade.platform.service.model.metric.MetricTaskResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Service
public class MetricTaskServiceImpl implements MetricTaskService {

    @Autowired
    private MetricTaskRepository metricTaskRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentMiniFlowService experimentMiniFlowService;

    @Autowired
    private MetricCategoryRepository metricCategoryRepository;

    @Override
    public List<MetricTaskResponse> selectByTaskId(MetricTaskRequest metricTaskRequest) {

        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.selectById(metricTaskRequest.getTaskId())
                .orElseThrow(() -> new BizException(ExceptionMessageEnum.EXPERIMENT_TASK_NOT_FOUNT));

        String metric = experimentTaskDO.getMetric();
        List<MetricModel> metricModels = JsonUtils.readValue(new TypeReference<List<MetricModel>>() {
        }, metric);

        List<DeviceMeta> deviceMetas = experimentMiniFlowService.selectExperimentDevice(experimentTaskDO.getExperimentId());

        return metricModels.stream().flatMap(metricModel ->
                deviceMetas.stream().map(deviceMeta -> {
                            List<MetricTaskDO> metricTaskDOS = metricTaskRepository
                                    .selectByTaskIdAndCategory(metricTaskRequest.getTaskId(),
                                            deviceMeta.getDeviceId(),
                                            metricModel.getCode(),
                                            metricTaskRequest.getStartTime(),
                                            metricTaskRequest.getEndTime());

                            return MetricTaskResponse.builder().category(
                                    MetricCategoryResponse.builder()
                                            .categoryId(metricModel.getCategoryId())
                                            .name(metricModel.getName())
                                            .code(metricModel.getCode())
                                            .params(metricModel.getParams().keySet().stream().map(
                                                    k -> MetricParam.builder()
                                                            .name(k)
                                                            .value(metricModel.getParams().get(k))
                                                            .build()
                                            ).collect(Collectors.toList()))
                                            .build()
                            )
                                    .hostname(deviceMeta.getHostname())
                                    .ip(deviceMeta.getIp())
                                    .taskId(metricTaskRequest.getTaskId())
                                    .metrics(metricTaskDOS.stream().map(metricTaskDO -> MetricTask.builder()
                                            .date(metricTaskDO.getDate())
                                            .value(metricTaskDO.getValue())
                                            .build()).collect(Collectors.toList()))
                                    .build();
                        }
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<MetricCategoryResponse> queryMetricCategory() {

        List<MetricCategoryDO> metricCategories = metricCategoryRepository.selectAll();

        List<MetricCategoryResponse> list = metricCategories.stream().map(
                metricCategory ->
                        MetricCategoryResponse.builder()
                                .categoryId(metricCategory.getId())
                                .parentId(metricCategory.getParentId())
                                .level(metricCategory.getLevel())
                                .name(metricCategory.getName())
                                .code(metricCategory.getCode())
                                .params(Optional.ofNullable(metricCategory.getParams()).map(v -> JsonUtils.readValue(new TypeReference<List<MetricParam>>() {
                                }, v)).orElse(Collections.emptyList()))
                                .build()
        ).collect(Collectors.toList());

        List<MetricCategoryResponse> parents = list.stream().filter(metricCategory -> metricCategory.getParentId() == null)
                .collect(Collectors.toList());

        fill(list, parents);
        return parents;
    }

    private void fill(List<MetricCategoryResponse> list, List<MetricCategoryResponse> parents) {

        List<MetricCategoryResponse> newParents = CollUtil.newArrayList();
        for (MetricCategoryResponse parent : parents) {
            for (MetricCategoryResponse category : list) {
                if (parent.getCategoryId().equals(category.getParentId())) {
                    List<MetricCategoryResponse> children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parent.setChildren(children);
                    }
                    children.add(category);
                    newParents.add(category);
                }
            }
        }

        if (CollUtil.isNotEmpty(parents)) {
            fill(list, newParents);
        }
    }
}
