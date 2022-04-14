package com.alibaba.chaosblade.box.dao.scheduler;

import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.alibaba.chaosblade.box.dao.repository.SchedulerJobRepository;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobCreateRequest;
import com.alibaba.chaosblade.box.dao.scheduler.domain.SchedulerJobUpdateRequest;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public abstract class BaseSchedulerService implements SchedulerJobService {

    @Autowired
    protected SchedulerJobRepository schedulerJobRepository;

    @Override
    public String addSchedulerJob(
        SchedulerJobCreateRequest schedulerJobCreateRequest) {
        Preconditions.checkArgument(schedulerJobCreateRequest.getBusinessId() != null, "businessId Required");
        Preconditions.checkArgument(schedulerJobCreateRequest.getBusinessType() != null, "businessType Required");
        Preconditions.checkArgument(schedulerJobCreateRequest.getClassName() != null, "className Required");
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(
            schedulerJobCreateRequest.getBusinessType(), schedulerJobCreateRequest.getBusinessId());
        try {
            if (schedulerJobDO != null) {
                log.warn("exist scheduler task by businessType:{},businessId:{},taskId:{},class:{}",
                    schedulerJobCreateRequest.getBusinessType(), schedulerJobCreateRequest.getBusinessId(),
                    schedulerJobDO.getJobId(), schedulerJobCreateRequest.getClassName());
                fillSchedulerJobDO(schedulerJobDO, schedulerJobCreateRequest);
                schedulerJobRepository.update(schedulerJobDO);
                rescheduleCronJob(schedulerJobDO);
                log.info("update scheduler job success,taskId:{},schedulerXJobId:{}", schedulerJobDO.getJobId(),
                    schedulerJobDO.getJobId());
                return schedulerJobDO.getJobId();
            } else {
                schedulerJobDO = new SchedulerJobDO();
                schedulerJobDO.setJobId(IdWorker.getIdStr());
                fillSchedulerJobDO(schedulerJobDO, schedulerJobCreateRequest);
                schedulerJobRepository.add(schedulerJobDO);
                internalAddSchedulerJob(schedulerJobDO);
                log.info("create scheduler job success,taskId:{},schedulerXJobId:{}", schedulerJobDO.getJobId(),
                    schedulerJobDO.getJobId());
                return schedulerJobDO.getJobId();
            }
        } catch (Exception e) {
            if (schedulerJobDO != null) {
                log.error("add scheduler failed,jobId:" + schedulerJobDO.getJobId(), e);
            }
        }
        return schedulerJobDO.getJobId();
    }

    private void fillSchedulerJobDO(SchedulerJobDO schedulerJobDO,
        SchedulerJobCreateRequest schedulerJobCreateRequest) {
        schedulerJobDO.setBusinessId(schedulerJobCreateRequest.getBusinessId());
        schedulerJobDO.setName(schedulerJobCreateRequest.getName());
        schedulerJobDO.setBusinessType(schedulerJobCreateRequest.getBusinessType());
        schedulerJobDO.setCronExpression(schedulerJobCreateRequest.getCron());
        schedulerJobDO.setEnabled(true);
        schedulerJobDO.setUserId(schedulerJobCreateRequest.getUserId());
        schedulerJobDO.setStartTime(schedulerJobCreateRequest.getStartTime());
        schedulerJobDO.setSchedulerBeanClass(schedulerJobCreateRequest.getClassName());
    }

    protected abstract void internalAddSchedulerJob(SchedulerJobDO schedulerJob) throws SchedulerException;

    @Override
    public String disableSchedulerJobByBusinessId(Integer businessType, String businessId) {
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(businessType,
            businessId);
        if (schedulerJobDO == null) { return null; }
        return disableSchedulerJob(schedulerJobDO);
    }

    @Override
    public boolean enableSchedulerJob(String jobId) {
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findById(jobId).orElse(null);
        if (schedulerJobDO == null) {
            return true;
        }
        if (!schedulerJobDO.getEnabled()) {
            schedulerJobDO.setEnabled(true);
            schedulerJobRepository.update(schedulerJobDO);
            try {
                internalAddSchedulerJob(schedulerJobDO);
            } catch (SchedulerException e) {
                log.error("enable scheduler failed,jobId:" + schedulerJobDO.getJobId(), e);
            }
        }
        return true;
    }

    private String disableSchedulerJob(SchedulerJobDO schedulerJobDO) {
        if (schedulerJobDO.getEnabled()) {
            schedulerJobDO.setEnabled(false);
            schedulerJobRepository.update(schedulerJobDO);
            try {
                internalDeleteSchedulerJob(schedulerJobDO);
            } catch (SchedulerException e) {
                log.error("disable scheduler failed,jobId:" + schedulerJobDO.getJobId(), e);
            }
        }
        return schedulerJobDO.getJobId();
    }

    @Override
    public String disableSchedulerJob(String jobId) {
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findById(jobId).orElse(null);
        if (schedulerJobDO == null) {
            return null;
        }
        return disableSchedulerJob(schedulerJobDO);
    }

    protected abstract void internalDeleteSchedulerJob(SchedulerJobDO schedulerJob) throws SchedulerException;

    @Override
    public boolean deleteSchedulerJob(String jobId) {
        return true;
    }

    @Override
    public boolean updateSchedulerJob(SchedulerJobUpdateRequest schedulerJobUpdateRequest) {
        Preconditions.checkArgument(schedulerJobUpdateRequest.getBusinessId() != null, "businessId Required");
        Preconditions.checkArgument(schedulerJobUpdateRequest.getBusinessType() != null, "businessType Required");
        SchedulerJobDO schedulerJobDO = schedulerJobRepository.findByBusinessIdAndBusinessType(
            schedulerJobUpdateRequest.getBusinessType(), schedulerJobUpdateRequest.getBusinessId());
        if (schedulerJobDO == null) { return false; }
        internalUpdateSchedulerJob(schedulerJobUpdateRequest, schedulerJobDO);
        return true;
    }

    private void internalUpdateSchedulerJob(SchedulerJobUpdateRequest schedulerJobUpdateRequest,
                                            SchedulerJobDO schedulerJobDO) {
        schedulerJobDO.setCronExpression(schedulerJobUpdateRequest.getCronExpression());
        schedulerJobDO.setStartTime(schedulerJobUpdateRequest.getStartTime());
        schedulerJobDO.setEnabled(true);
        schedulerJobRepository.update(schedulerJobDO);
        if (schedulerJobDO.getEnabled()) {
            try {
                rescheduleCronJob(schedulerJobDO);
            } catch (Exception e) {
                log.error("update scheduler failed,jobId:" + schedulerJobDO.getJobId(), e);
            }
        }
    }

    @Override
    public boolean triggerSchedulerJob(String taskId, Map<String, String> params) {
        return false;
    }

    @Override
    public String stopSchedulerJob(String jobId) {
        return null;
    }

    protected abstract void rescheduleCronJob(SchedulerJobDO schedulerJobDO) throws Exception;

}
