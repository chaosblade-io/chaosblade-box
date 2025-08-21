package com.alibaba.chaosblade.box.dao.scheduler.job;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChaosTraceUtil;
import com.alibaba.chaosblade.box.dao.command.task.ActivityTaskExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.ExperimentTaskRunnableSettings;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.activity.ExperimentExecuteContext;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 延迟演练任务调度器
 * 用于在压测启动后延迟指定时间再开始演练任务的故障注入
 *
 * @author ZhengMingZhuo
 */
@Slf4j
@Component
public class DelayedExperimentJob extends BaseJob implements Job {

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ActivityTaskRepository activityTaskRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String activityTaskId = context.getJobDetail().getJobDataMap().getString("activityTaskId");
        String userId = context.getJobDetail().getJobDataMap().getString("userId");
        String syncStr = context.getJobDetail().getJobDataMap().getString("sync");
        boolean sync = Boolean.parseBoolean(syncStr);
        
        log.info("开始执行延迟的演练任务: activityTaskId={}", activityTaskId);
        
        try {
            // 查询活动任务
            Optional<ActivityTaskDO> activityTaskOptional = activityTaskRepository.findById(activityTaskId);
            if (!activityTaskOptional.isPresent()) {
                log.warn("活动任务不存在: activityTaskId={}", activityTaskId);
                return;
            }

            ActivityTaskDO activityTaskDO = activityTaskOptional.get();
            log.info("延迟执行获取到活动任务: activityTaskId={}, phase={}, activityName={}, state={}, result={}, nextTaskId={}",
                    activityTaskDO.getTaskId(),
                    activityTaskDO.getPhase(),
                    activityTaskDO.getActivityName(),
                    activityTaskDO.getState(),
                    activityTaskDO.getResult(),
                    activityTaskDO.getNextActivityTaskId());
            
            // 创建用户对象
            ChaosUser chaosUser = new ChaosUser();
            chaosUser.setUserId(userId);
            
            // 创建演练执行上下文
            ExperimentExecuteContext experimentExecuteContext = new ExperimentExecuteContext(
                    ChaosTraceUtil.generateTraceId(), 
                    chaosUser,
                    activityTaskDO,
                    new ExperimentTaskRunnableSettings()
            );
            
            // 执行活动任务
            log.info("开始执行故障注入: activityTaskId={}", activityTaskId);
            
            if (sync) {
                commandBus.syncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
            } else {
                commandBus.asyncRun(new ActivityTaskExecutionCommand(experimentExecuteContext));
            }
            
            log.info("延迟演练任务执行完成: activityTaskId={}", activityTaskId);
            
        } catch (Exception e) {
            log.error("延迟演练任务执行失败: activityTaskId={}", activityTaskId, e);
            throw new JobExecutionException("延迟演练任务执行失败", e);
        }
    }
}
