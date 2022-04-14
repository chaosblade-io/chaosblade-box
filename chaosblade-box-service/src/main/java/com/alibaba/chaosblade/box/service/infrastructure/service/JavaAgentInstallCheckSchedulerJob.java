package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import com.alibaba.chaosblade.box.service.command.agent.JavaAgentInstallCheckCommand;
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
public class JavaAgentInstallCheckSchedulerJob extends BaseJob implements Job, InitializingBean {

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOS = buildFirstLevelDispatchTasks();
        experimentMiniAppTaskDOS.parallelStream().forEach(experimentMiniAppTaskDO -> commandBus
            .syncRun(JavaAgentInstallCheckCommand.class, experimentMiniAppTaskDO));
    }

    private List<ExperimentMiniAppTaskDO> buildFirstLevelDispatchTasks() {
        return activityTargetExecutionResultRepository.findRunningTask(MiniAppUtils.AGENT_INSTALL);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String cronExpression = "0 0/1 * * * ?";
        SchedulerJobCreateRequest schedulerJobCreateRequest = new SchedulerJobCreateRequest(cronExpression, 0,
            JavaAgentInstallCheckSchedulerJob.class.getName(),
            SchedulerConstant.BUSINESS_TYPE_JAVA_AGENT, "-1", JavaAgentInstallCheckSchedulerJob.class.getName());
        schedulerJobService.addSchedulerJob(schedulerJobCreateRequest);
    }
}
