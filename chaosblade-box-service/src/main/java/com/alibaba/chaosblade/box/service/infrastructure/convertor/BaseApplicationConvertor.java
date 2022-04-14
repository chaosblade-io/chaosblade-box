package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ApplicationRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseApplicationConvertor<From, To> {

    @Autowired
    protected ApplicationRelationRepository applicationRelationRepository;

    @Autowired
    protected ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    /**
     * 类型转换
     *
     * @param from
     * @return
     */
    public abstract To convert(From from);

    protected Integer getMachineCount(Long appId) {
        return applicationDeviceRepository.countByAppId(appId);
    }

    protected Integer getExperimentTaskCount(Long appId) {
        return applicationRelationRepository.countByAppIdAndRelationType(appId,
            ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
    }

    protected Integer getSceneFunctionCount(Long appId) {
        return activityTaskRepository.countAppCodesCountByAppId(appId);
    }

}
