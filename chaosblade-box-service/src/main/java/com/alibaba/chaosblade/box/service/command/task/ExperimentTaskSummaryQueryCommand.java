package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentTaskSummary;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskUtil;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin
 * 
 * 
 */
@Component
public class ExperimentTaskSummaryQueryCommand
    extends SpringBeanCommand<ExperimentTaskQueryRequest, Response<ExperimentTaskSummary>> {

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ExperimentTaskUtil experimentTaskUtil;

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public Response<ExperimentTaskSummary> execute(ExperimentTaskQueryRequest experimentTaskQueryRequest) {
        ExperimentTaskSummary experimentTaskSummary = new ExperimentTaskSummary();
        experimentTaskSummary.setTaskId(experimentTaskQueryRequest.getTaskId());
        Response<ExperimentTaskDO> experimentTaskDOResponse = experimentChecker.assertExperimentTaskExist(
            experimentTaskQueryRequest.getTaskId());
        if (!experimentTaskDOResponse.isSuccess()) {
            return Response.failedWith(experimentTaskDOResponse.getError());
        }
        ExperimentDO experimentDO = experimentRepository.findById(experimentTaskDOResponse.getResult().getExperimentId())
            .orElse(null);
        experimentTaskUtil.fillBaseInfo(experimentTaskSummary, experimentTaskDOResponse.getResult(), experimentDO);
        fillActivities(experimentTaskDOResponse.getResult(), experimentTaskSummary);
        fillIsJvm(experimentTaskSummary);
        return Response.okWithData(experimentTaskSummary);
    }

    private void fillActivities(ExperimentTaskDO experimentTaskDO, ExperimentTaskSummary experimentTaskSummary) {
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByExperimentTaskId(
            experimentTaskSummary.getTaskId());
        List<ActivityTask> activityTasks = activityTaskDOS.stream().map(activityTaskDO -> {
            ActivityTaskQueryRequest activityTaskQueryRequest = new ActivityTaskQueryRequest(activityTaskDO);
            activityTaskQueryRequest.setReturnMiniAppTasks(false);
            activityTaskQueryRequest.setExperimentTaskDO(experimentTaskDO);
            return commandBus.syncRun(ActivityTaskQueryCommand.class, activityTaskQueryRequest);
        }).sorted(Comparator.comparingInt(ActivityTask::getOrder)).collect(Collectors.toList());
        experimentTaskSummary.setActivities(activityTasks);
    }

    private void fillIsJvm(ExperimentTaskSummary experimentTaskSummary) {
        if (CollectionUtils.isEmpty(experimentTaskSummary.getActivities())) {
            experimentTaskSummary.setIsJvm(false);
            return;
        }
        experimentTaskSummary.setIsJvm(experimentTaskSummary.getActivities().stream()
                .anyMatch(activityTask -> MiniAppUtils.isJvm(activityTask.getExecutableAppCode())));
    }

}
