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

package com.alibaba.chaosbox.service.impl;

import com.alibaba.chaosbox.common.DeviceMeta;
import com.alibaba.chaosbox.common.exception.BizException;
import com.alibaba.chaosbox.common.utils.JsonUtils;
import com.alibaba.chaosbox.dao.model.ExperimentMiniFlowGroupDO;
import com.alibaba.chaosbox.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosbox.dao.repository.ExperimentMiniFlowRepository;
import com.alibaba.chaosbox.service.ExperimentMiniFlowService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alibaba.chaosbox.common.exception.ExceptionMessageEnum.FLOW_GROUP_NOT_EXISTS;

/**
 * @author yefei
 */
@Service
public class ExperimentMiniFlowServiceImpl implements ExperimentMiniFlowService {

    @Autowired
    private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

    @Autowired
    private ExperimentMiniFlowRepository experimentMiniFlowRepository;

    @Override
    public ExperimentMiniFlowGroupDO selectByFlowId(Long flowId) {
        return experimentMiniFlowRepository.selectById(flowId)
                .map(experimentMiniFlowDO ->
                        experimentMiniFlowGroupRepository.selectById(experimentMiniFlowDO.getGroupId()).orElseThrow(
                                () -> new BizException(FLOW_GROUP_NOT_EXISTS)))
                .orElse(null);
    }

    @Override
    public List<DeviceMeta> selectExperimentDeviceByFlowId(Long flowId) {
        ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO = experimentMiniFlowRepository.selectById(flowId)
                .map(experimentMiniFlowDO ->
                        experimentMiniFlowGroupRepository.selectById(experimentMiniFlowDO.getGroupId()).orElseThrow(
                                () -> new BizException(FLOW_GROUP_NOT_EXISTS)))
                .orElse(null);

        if (experimentMiniFlowGroupDO == null) {
            return Collections.emptyList();
        }
        String host = experimentMiniFlowGroupDO.getHosts();
        return JsonUtils.readValue(new TypeReference<List<DeviceMeta>>() {
        }, host);
    }

    @Override
    public List<DeviceMeta> selectExperimentDevice(Long experimentId) {

        // TODO
        ExperimentMiniFlowGroupDO experimentMiniFlowGroupDO = experimentMiniFlowGroupRepository
                .selectByExperiment(experimentId).get(0);
        String host = experimentMiniFlowGroupDO.getHosts();

        return JsonUtils.readValue(new TypeReference<List<DeviceMeta>>() {
        }, host);
    }
}
