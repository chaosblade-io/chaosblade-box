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

import com.alibaba.chaosbox.common.constants.ChaosConstant;
import com.alibaba.chaosbox.dao.model.ExperimentActivityDO;
import com.alibaba.chaosbox.dao.repository.ExperimentActivityRepository;
import com.alibaba.chaosbox.service.ExperimentActivityService;
import com.alibaba.chaosbox.service.model.experiment.activity.ExperimentActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Service
public class ExperimentActivityServiceImpl implements ExperimentActivityService {

    @Autowired
    private ExperimentActivityRepository experimentActivityRepository;

    @Override
    public ExperimentActivity selectByExperimentId(Long ExperimentId) {
        List<ExperimentActivityDO> activityDOS = experimentActivityRepository.selectListByExperiment(ExperimentId);
        int index = activityDOS.size() - 1;
        return setNext(activityDOS, covert(activityDOS.get(index)), index);
    }

    private ExperimentActivity setNext(List<ExperimentActivityDO> activityDOS, ExperimentActivity last, int i) {
        if (i == 0) {
            return last;
        }
        ExperimentActivity experimentActivity = covert(activityDOS.get(--i));
        experimentActivity.setNextActivity(last);
        if (i == 0) {
            return experimentActivity;
        }
        return setNext(activityDOS, experimentActivity, i);
    }

    public ExperimentActivity covert(ExperimentActivityDO experimentActivity) {
        return ExperimentActivity.builder()
                .flowId(experimentActivity.getFlowId())
                .activityId(experimentActivity.getId())
                .sceneCode(experimentActivity.getSceneCode())
                .activityName(experimentActivity.getActivityName())
                .activityDefinition(experimentActivity.getActivityDefinition())
                .phase(experimentActivity.getPhase()).build();
    }

    @Override
    public List<ExperimentActivity> selectAttackByExperimentId(Long ExperimentId) {
        List<ExperimentActivityDO> activityDOS = experimentActivityRepository.selectListByExperiment(ExperimentId);
        return activityDOS.stream().filter(experimentActivityDO ->
                experimentActivityDO.getPhase().equals(ChaosConstant.PHASE_ATTACK))
                .map(this::covert)
                .collect(Collectors.toList());
    }
}
