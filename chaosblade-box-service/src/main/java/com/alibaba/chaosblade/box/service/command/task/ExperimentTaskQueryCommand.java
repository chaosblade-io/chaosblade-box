package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.experiment.PhaseInfo;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ActivityTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityRepository;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskQueryCommand
    extends SpringBeanCommand<ExperimentTaskQueryRequest, Response<ExperimentTask>> {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Autowired
    private ActivityTaskManager activityTaskManager;

    @Autowired
    private ExperimentChecker experimentChecker;
    @Autowired
    private ExperimentTaskUtil experimentTaskUtil;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public Response<ExperimentTask> execute(ExperimentTaskQueryRequest experimentTaskQueryRequest) {
        String experimentTaskId = experimentTaskQueryRequest.getTaskId();
        Response<ExperimentTaskDO> experimentTaskDOResponse = experimentChecker.assertExperimentTaskExist(
            experimentTaskId);
        if (!experimentTaskDOResponse.isSuccess()) {
            return Response.failedWith(experimentTaskDOResponse.getError());
        }
        ExperimentTask experimentTask = new ExperimentTask();
        ExperimentDO experimentDO = experimentRepository.findById(experimentTaskDOResponse.getResult().getExperimentId())
            .orElse(null);
        experimentTaskUtil.fillBaseInfo(experimentTask, experimentTaskDOResponse.getResult(), experimentDO);
        fillPhaseInfo(experimentTask);
        return Response.okWithData(experimentTask);
    }

    private void fillPhaseInfo(ExperimentTask experimentTask) {
        List<ActivityTaskDO> activityTaskDOS = activityTaskRepository.findByExperimentTaskId(
            experimentTask.getTaskId());
        Map<String, List<ActivityTaskDO>> activityIdToTasks = activityTaskDOS.stream().collect(
            Collectors.groupingBy(ActivityTaskDO::getActivityId));
        List<ActivityTask> activityTasks = activityIdToTasks.values().stream().map(
            taskDOS -> {
                ActivityTaskDO activityTaskDO = taskDOS.stream().max(
                    Comparator.comparingLong(o -> o.getGmtCreate().getTime())).orElse(null);
                if (activityTaskDO == null) { return null; }
                return activityTaskManager.querySimpleActivityTaskInfo(activityTaskDO).getResult();
            }).filter(Objects::nonNull).collect(Collectors.toList());
        long progressPercent = experimentTaskUtil.calculateProgressPercent( activityTaskDOS);
        experimentTask.setProgressPercent(progressPercent);
        List<PhaseInfo> phaseInfos = activityTasks.stream().collect(
            Collectors.groupingBy(ActivityTask::getPhase)).entrySet().stream().map(
            phaseTypeListEntry -> new PhaseInfo(phaseTypeListEntry.getKey(), phaseTypeListEntry.getValue())).sorted(
            Comparator.comparingInt(o -> o.getPhase().getType()))
            .collect(Collectors.toList());
        experimentTask.setPhases(phaseInfos);
    }

  

}
