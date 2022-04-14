package com.alibaba.chaosblade.box.dao.scheduler.job;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerJobService;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 全局节点，包括监控以及恢复的定时任务
 *
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentGuardInstanceSchedulerJob extends BaseJob implements Job {

    @Autowired
    private ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private SchedulerJobService schedulerJobService;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerJobDO schedulerJobDO = reloadSchedulerJobDO(context);
        return;
//        if (schedulerJobDO.getEnabled()) {
//            String experimentTaskId = schedulerJobDO.getBusinessId();
//            ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(schedulerJobDO.getBusinessId()).get();
//            List<ExperimentGuardInstanceDO> experimentGuardInstances = experimentGuardInstanceRepository
//                .findByExperimentTaskId(experimentTaskId);
//            if (!experimentTaskDO.isRunning()) {
//                experimentGuardInstances.stream().filter(
//                    experimentGuardInstanceDO -> !experimentGuardInstanceDO.getActionType()
//                        .equals(ExperimentGuardDO.ACTION_TYPE_MONITOR)).forEach(
//                    this::stopGuardInstance);
//            }
//            if (isExperimentStopOverMinutes(experimentTaskDO)) {
//                schedulerJobService.disableSchedulerJob(schedulerJobDO.getJobId());
//                log.info("disable guard instance scheduler job,reason:{},taskId:{},jobId:{}",
//                    "experiment task finished", schedulerJobDO.getBusinessId(), schedulerJobDO.getJobId());
//                experimentGuardInstances.stream().filter(
//                    experimentGuardInstanceDO -> experimentGuardInstanceDO.getActionType()
//                        .equals(ExperimentGuardDO.ACTION_TYPE_MONITOR)).forEach(
//                    this::stopGuardInstance);
//                return;
//            }
//            commandBus.syncRun(ExperimentGuardInstanceExecutionCommand.class,
//                new ExperimentGuardInstanceExecutionRequest(experimentTaskDO, experimentGuardInstances, true));
//        }
    }

    /**
     * 需要判断演练是否停止超时
     *
     * @param experimentTaskDO
     * @return
     */
    private boolean isExperimentStopOverMinutes(ExperimentTaskDO experimentTaskDO) {
        return !experimentTaskDO.isRunning() && experimentTaskDO.getGmtEnd() != null && (
            System.currentTimeMillis() - experimentTaskDO.getGmtEnd().getTime() >= 2 * 60 * 1000);
    }

    private void stopGuardInstance(ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        if (!experimentGuardInstanceDO.isHalted()) {
            experimentGuardInstanceDO.setState(GuardRunState.FINISHED);
            experimentGuardInstanceRepository.updateRunningExperimentGuardInstanceDO(
                experimentGuardInstanceDO);
        }
    }

}
