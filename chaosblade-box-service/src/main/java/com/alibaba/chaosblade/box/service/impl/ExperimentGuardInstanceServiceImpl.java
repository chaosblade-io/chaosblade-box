package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.cache.templates.distributed.RedisCacheTemplate;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import com.alibaba.chaosblade.box.service.ExperimentGuardInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ExperimentGuardInstanceServiceImpl implements ExperimentGuardInstanceService {

    public static String PRE = "guard:monitor";

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Autowired
    private RedisCacheTemplate redisTemplate;

    @Override
    public boolean addExperimentGuardInstance(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        if(null != experimentGuardInstanceDO && ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(experimentGuardInstanceDO.getActionType())) {
            ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity =  experimentGuardInstanceDO.getValue();
            experimentGuardInstanceDO.setValue(null);
            experimentGuardInstanceRepository.add(experimentGuardInstanceDO);
            redisTemplate.prefixPut(PRE,experimentGuardInstanceDO.getInstanceId(),experimentGuardMonitorMetricResultEntity);
            return true;
        }
        return experimentGuardInstanceRepository.add(experimentGuardInstanceDO);
    }

    @Override
    public boolean updateExperimentGuardInstance(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        if(null != experimentGuardInstanceDO && ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(experimentGuardInstanceDO.getActionType())) {
            ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity =  experimentGuardInstanceDO.getValue();
            experimentGuardInstanceDO.setValue(null);
            experimentGuardInstanceRepository.update(experimentGuardInstanceDO);
            redisTemplate.prefixPut(PRE,experimentGuardInstanceDO.getInstanceId(),experimentGuardMonitorMetricResultEntity);
            return true;
        }
        return experimentGuardInstanceRepository.updateByInstanceId(experimentGuardInstanceDO);
    }

}
