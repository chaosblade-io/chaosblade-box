package com.alibaba.chaosblade.box.dao.scheduler.quartz;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.SchedulerConstant;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseJob {

    @Autowired
    protected SchedulerJobRepository schedulerJobRepository;

    public BaseJob() {
        ChaosQuartzClassLoadHelper.addJobClass(this.getClass());
    }

    public SchedulerJobDO reloadSchedulerJobDO(JobExecutionContext jobExecutionContext) {
        String jobId = null;
        if (jobExecutionContext.getMergedJobDataMap().containsKey(SchedulerConstant.SCHEDULER_PARAM_KET_JOB_ID)) {
            jobId = jobExecutionContext.getMergedJobDataMap().getString(SchedulerConstant.SCHEDULER_PARAM_KET_JOB_ID);
        } else if (jobExecutionContext.getMergedJobDataMap().containsKey(SchedulerConstant.SCHEDULER_PARAM_KET_JOB)) {
            jobId = ((SchedulerJobDO)jobExecutionContext.getMergedJobDataMap().get(
                SchedulerConstant.SCHEDULER_PARAM_KET_JOB)).getJobId();
        }
        if (jobId == null) { return null; }
        return schedulerJobRepository.findById(jobId).orElse(null);
    }

}
