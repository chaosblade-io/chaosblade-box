package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class MiniAppTaskSummary implements Serializable {

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 任务ID
     */
    protected String taskId;

    /**
     * 任务的运行状态
     */
    protected StateEnum state;

    /**
     * 结束时间
     */
    protected Date endTime;

    /**
     * 任务所在的执行机器
     */
    protected String hostIp;

    /**
     * 结果标志
     */
    protected ResultEnum result;

    private String deviceName;


    protected String appConfigurationId;

}
