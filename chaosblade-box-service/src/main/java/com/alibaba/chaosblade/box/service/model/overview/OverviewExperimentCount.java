package com.alibaba.chaosblade.box.service.model.overview;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunpeng
 *
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverviewExperimentCount {

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



}
