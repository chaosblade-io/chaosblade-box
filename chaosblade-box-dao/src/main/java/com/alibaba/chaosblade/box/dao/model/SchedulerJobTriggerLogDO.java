package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin.lhb
 *
 *
 */
@TableName(value = "t_chaos_scheduler_trigger_log")
@Data
public class SchedulerJobTriggerLogDO extends BaseDO {

    @TableId
    protected Long id;

    private String jobGroup;
    private String jobId;
    private String executorAddress;
    private String executorHandler;
    private String executorParam;
    private Date endTime;
    private String businessId;
    private Date fireTime;
    private String fireInstanceId;

}
