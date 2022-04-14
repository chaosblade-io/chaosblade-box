package com.alibaba.chaosblade.box.dao.scheduler.job;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async.ActivityAsyncCheckCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async.AsyncCallBackContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Slf4j
@DisallowConcurrentExecution
@Component
public class ActivityAsyncCheckSchedulerJob extends BaseJob implements Job, InitializingBean {

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOS = buildFirstLevelDispatchTasks();
        experimentMiniAppTaskDOS.parallelStream().forEach(experimentMiniAppTaskDO -> {
            AsyncCallBackContext asyncCallBackContext = new AsyncCallBackContext();
            asyncCallBackContext.setExperimentMiniAppTaskDO(experimentMiniAppTaskDO);
            commandBus.syncRun(ActivityAsyncCheckCommand.class, asyncCallBackContext);
        });
    }

    private List<ExperimentMiniAppTaskDO> buildFirstLevelDispatchTasks() {
        return activityTargetExecutionResultRepository.findAsyncRunningTask();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cronExpression = "0 0/1 * * * ?";
        SchedulerJobCreateRequest schedulerJobCreateRequest = new SchedulerJobCreateRequest(cronExpression, 0,
                ActivityAsyncCheckSchedulerJob.class.getName(),
                SchedulerConstant.BUSINESS_TYPE_ACTIVITY_ASYNC, "-1", ActivityAsyncCheckSchedulerJob.class.getName());
        schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
    }
}
