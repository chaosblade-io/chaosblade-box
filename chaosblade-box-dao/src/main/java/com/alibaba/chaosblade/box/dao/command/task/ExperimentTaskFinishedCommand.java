package com.alibaba.chaosblade.box.dao.command.task;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskFinishedInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentTaskFinishedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author haibin
 * 
 *
 */
@Component
public class ExperimentTaskFinishedCommand extends SpringBeanCommand<String, Boolean> {

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;
    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private ActivityTaskManager activityTaskManager;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private List<ExperimentTaskFinishedInterceptor> experimentTaskFinishedInterceptors;

    @Override
    public Boolean execute(String experimentTaskId) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(experimentTaskId).orElse(null);
        if (experimentTaskDO == null || experimentTaskDO.isFinished()) {
            return false;
        }
        for (ExperimentTaskFinishedInterceptor experimentTaskFinishedInterceptor : experimentTaskFinishedInterceptors) {
            experimentTaskFinishedInterceptor.beforeTurnToFinishedState(experimentTaskDO);
        }
        rejectLeftActivityTasks(experimentTaskId);
        ResultEnum resultEnum = determineExperimentTaskResult(experimentTaskDO);
        experimentTaskDO.setGmtEnd(new Date());
        experimentTaskDO.setResult(resultEnum.getValue());
        experimentTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentTaskRepository.update(experimentTaskDO);
        experimentRepository.findById(experimentTaskDO.getExperimentId()).ifPresent(experimentDO -> {
            experimentDO.setState(ExperimentStateEnum.READY.getValue());
            experimentDO.setResult(resultEnum.getValue());
            experimentRepository.update(experimentDO);
            ChaosUser user = new ChaosUser();
            user.setUserId(experimentDO.getUserId());
            chaosEventDispatcher.fireEvent(new ExperimentTaskFinishedEvent(user, experimentTaskDO));
        });
        return true;
    }

    private void rejectLeftActivityTasks(String experimentTaskId) {
        activityTaskManager.rejectActivityTasksByExperimentTaskId(
            experimentTaskId, "演练结束");
    }

    /**
     * 判断演练状态
     *
     * @param experimentTaskDO
     * @return
     */
    private ResultEnum determineExperimentTaskResult(ExperimentTaskDO experimentTaskDO) {
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByExperimentTaskId(
            experimentTaskDO.getTaskId());
        ResultEnum resultEnum = ResultEnum.ERROR;
        for (ActivityTaskDO activityTaskDO : activityTaskDOS) {
            if (activityTaskDO.isSuccess() || activityTaskDO.isRejcted()) {
                resultEnum = ResultEnum.SUCCESS;
            } else {
                if (activityTaskDO.isError()) {
                    resultEnum = ResultEnum.ERROR;
                } else {
                    resultEnum = ResultEnum.FAILED;
                }
                break;
            }
        }
        return resultEnum;
    }
}
