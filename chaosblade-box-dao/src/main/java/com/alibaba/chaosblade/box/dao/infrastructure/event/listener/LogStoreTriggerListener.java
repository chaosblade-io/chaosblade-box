package com.alibaba.chaosblade.box.dao.infrastructure.event.listener;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.SystemUtils;
import com.alibaba.chaosblade.box.dao.mapper.SchedulerJobTriggerLogMapper;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobTriggerLogDO;
import com.alibaba.chaosblade.box.dao.scheduler.quartz.BaseJob;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class LogStoreTriggerListener extends BaseJob implements TriggerListener {
    @Autowired
    private SchedulerJobTriggerLogMapper schedulerJobTriggerLogMapper;

    private SchedulerJobTriggerLogDO findByFireInstanceId(String fireInstanceId) {
        QueryWrapper<SchedulerJobTriggerLogDO> schedulerJobTriggerLogDOQueryWrapper = new QueryWrapper<>();
        schedulerJobTriggerLogDOQueryWrapper.eq("fire_instance_id", fireInstanceId);
        return schedulerJobTriggerLogMapper.selectOne(schedulerJobTriggerLogDOQueryWrapper);
    }

    @Override
    public String getName() {
        return "LogStoreTriggerListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        SchedulerJobTriggerLogDO schedulerJobTriggerLogDO = new SchedulerJobTriggerLogDO();
        schedulerJobTriggerLogDO.setExecutorAddress(SystemUtils.getLocalAddress());
        schedulerJobTriggerLogDO.setExecutorHandler(context.getJobDetail().getJobClass().getName());
        schedulerJobTriggerLogDO.setFireTime(context.getFireTime());
        SchedulerJobDO schedulerJobDO = reloadSchedulerJobDO(context);
        if (schedulerJobDO != null) {
            schedulerJobTriggerLogDO.setJobId(schedulerJobDO.getJobId());
            schedulerJobTriggerLogDO.setBusinessId(schedulerJobDO.getBusinessId());
        }
        schedulerJobTriggerLogDO.setFireInstanceId(context.getFireInstanceId());
        schedulerJobTriggerLogDO.setJobGroup(context.getJobDetail().getKey().getGroup());
        schedulerJobTriggerLogMapper.insert(schedulerJobTriggerLogDO);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
        CompletedExecutionInstruction triggerInstructionCode) {
        SchedulerJobTriggerLogDO schedulerJobTriggerLogDO = findByFireInstanceId(context.getFireInstanceId());
        if (schedulerJobTriggerLogDO != null) {
            schedulerJobTriggerLogDO.setEndTime(new Date(context.getFireTime().getTime() + context.getJobRunTime()));
            schedulerJobTriggerLogMapper.updateById(schedulerJobTriggerLogDO);
        }
    }
}
