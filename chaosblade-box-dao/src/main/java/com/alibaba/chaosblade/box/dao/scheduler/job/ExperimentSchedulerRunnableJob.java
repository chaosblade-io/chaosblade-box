package com.alibaba.chaosblade.box.dao.scheduler.job;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRunResult;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.command.ExperimentExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.Trackers;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin
 * 
 *
 */
@Component
public class ExperimentSchedulerRunnableJob extends BaseJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(ExperimentSchedulerRunnableJob.class);

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private Trackers trackers;

    static Integer FAILED_TASK_TOLERANCE = 2;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerJobDO schedulerJobDO = reloadSchedulerJobDO(context);
        if (schedulerJobDO.getEnabled()) {
            ExperimentRunRequest experimentRunRequest = new ExperimentRunRequest();
            experimentRunRequest.setExperimentId(schedulerJobDO.getBusinessId());
            ExperimentDO experimentDO = experimentRepository.findById(schedulerJobDO.getBusinessId()).orElse(null);
            if (experimentDO == null) { return; }
            //判断下最近的一次演练是否运行
            ChaosUser chaosUser = new ChaosUser();
            chaosUser.setUserId(experimentDO.getUserId());
            Stream.of(experimentDO).filter(experimentDO1 -> {
                boolean isLastExperimentFailed = isLastExperimentTaskFailedFrequently(experimentDO1);
                if (isLastExperimentFailed) {
                    trackers(experimentDO1, chaosUser);
                    schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
                }
                return !isLastExperimentFailed;
            }).forEach(experimentDO12 -> {
                experimentRunRequest.setUser(chaosUser);
                experimentRunRequest.setTriggeredByScheduler(true);
                Response<ExperimentRunResult> resultResponse = commandBus.syncRun(ExperimentExecutionCommand.class,
                    experimentRunRequest);
                if (!resultResponse.isSuccess()) {
                    logger.warn("run experiment failed:experimentId:{},{}", schedulerJobDO.getBusinessId(),
                        resultResponse.getError());
                }
            });

        } else {
            schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
        }
    }

    private void trackers(ExperimentDO experimentDO, ChaosUser chaosUser) {
        trackers.trackExperimentOperation(ChangelogTypes.ChangeActionType.RUN,
            ChangelogTypes.ChangeOperatorType.SCHEDULER, chaosUser,
            experimentDO.getExperimentId(),
            "运行失败,原因:最近" + FAILED_TASK_TOLERANCE + "次任务所有机器注入失败,定时任务暂停,待最近任务手动运行成功后即可恢复");
    }

    private boolean isLastExperimentTaskFailedFrequently(ExperimentDO experimentDO) {
        if (experimentDO.getExperimentTaskId() == null) { return false; }
        List<String> taskIds = experimentTaskRepository.findByExperimentTasksOrderByExperimentDesc(
            experimentDO.getExperimentId(), FAILED_TASK_TOLERANCE).stream().map(ExperimentTaskDO::getTaskId).collect(
            Collectors.toList());
        if (taskIds.size() != FAILED_TASK_TOLERANCE) {
            return false;
        }
        return activityTargetExecutionResultRepository.isAllAttackTaskFailed(taskIds);
    }

}