package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author haibin
 *
 * 
 */
@Component
public class ExperimentTaskContextManager {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    /**
     * 获取演练的上下文，这个上下文会持久化保存
     *
     * @param taskId
     * @return
     */
    public ExperimentTaskContext getExperimentTaskContext(String taskId) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(taskId).get();
        if (experimentTaskDO.getExperimentTaskContext() == null) {
            return new ExperimentTaskContext();
        }
        return experimentTaskDO.getExperimentTaskContext();
    }

    public void updateExperimentTaskContext(String taskId, ExperimentTaskContext experimentTaskContext) {
        experimentTaskContext = Objects.requireNonNull(experimentTaskContext);
        experimentTaskRepository.updateContext(taskId, experimentTaskContext);
    }

}
