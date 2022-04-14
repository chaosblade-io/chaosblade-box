package com.alibaba.chaosblade.box.common.common.domain.experiment;

import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentSchedulerConfig {

    /**
     * 定时任务表达式
     */
    private String cronExpression;

    /**
     * 指定一次时间
     */
    private Date fixedTime;
}
