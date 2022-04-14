package com.alibaba.chaosblade.box.common.experiment.clientobject;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentStat {

    long total;
    long running;
    long finished;
    long exception;
    long failure;

    /**
     * 执行过的演练
     */
    long active;

    /**
     * 成功的演练
     */
    long success;

    /**
     * 未进行过演练的数目
     */
    long idle;

    /**
     * 老数据
     */
    long totalCount;
    long runningCount;
    long finishedCount;
    long exceptionCount;
    long failureCount;
}
