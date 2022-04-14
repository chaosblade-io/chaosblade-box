package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.service.model.application.ApplicationBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author haibin
 * 
 * 
 */
@Component
public class ApplicationBasicInfoConvertor extends BaseApplicationConvertor<ApplicationDO, ApplicationBasicInfo> {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentTaskUtil experimentTaskUtil;

    @Autowired
    private ApplicationGroupRepository applicationGroupRepository;

    @Override
    public ApplicationBasicInfo convert(ApplicationDO applicationDO) {
        ApplicationBasicInfo applicationBasicInfo = new ApplicationBasicInfo();
        applicationBasicInfo.setAppId(String.valueOf(applicationDO.getId()));
        applicationBasicInfo.setAppName(applicationDO.getAppName());
        applicationBasicInfo.setAppType(applicationDO.getAppType());
        applicationBasicInfo.setMachineCount(getMachineCount(applicationDO.getId()));
        applicationBasicInfo.setExperimentTaskCount(getExperimentTaskCount(applicationDO.getId()));
        applicationBasicInfo.setAppGroups(
            applicationGroupRepository.findApplicationGroupsByAppId(applicationDO.getId()));
        fillExperimentTaskInfo(applicationBasicInfo, applicationDO.getId());
        return applicationBasicInfo;
    }

    private void fillExperimentTaskInfo(ApplicationBasicInfo applicationBasicInfo, Long appId) {
        ApplicationRelationDO applicationRelationDO = applicationRelationRepository.findLatestExperimentTaskByAppId(
            appId);
        if (applicationRelationDO != null) {
            String experimentTaskId = applicationRelationDO.getOuterId();
            experimentTaskRepository.findById(experimentTaskId).ifPresent(new Consumer<ExperimentTaskDO>() {
                @Override
                public void accept(ExperimentTaskDO experimentTaskDO) {
                    BaseExperimentTask baseExperimentTask = new BaseExperimentTask();
                    experimentTaskUtil.fillBaseInfo(baseExperimentTask, experimentTaskDO, null);
                    applicationBasicInfo.setTask(baseExperimentTask);
                }
            });
        }

    }
}
