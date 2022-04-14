package com.alibaba.chaosblade.box.service.infrastructure.convertor;


import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskUtil;
import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationBaseExperimentTaskConvertor
    extends BaseApplicationConvertor<ApplicationRelationDO, BaseExperimentTask> {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ExperimentTaskUtil experimentTaskUtil;

    @Override
    public BaseExperimentTask convert(ApplicationRelationDO applicationRelationDO) {
        BaseExperimentTask baseExperimentTask = new BaseExperimentTask();
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(applicationRelationDO.getOuterId())
            .orElse(null);
        if (experimentTaskDO == null) { return null; }
        experimentTaskUtil.fillBaseInfo(baseExperimentTask, experimentTaskDO, null);
        return baseExperimentTask;
    }
}
