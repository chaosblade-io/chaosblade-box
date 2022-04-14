package com.alibaba.chaosblade.box.common.experiment.task.flow.step;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 * 
 *
 */
@Data
public class StepExecuteResult implements Serializable {

    protected boolean success;

    private Throwable error;

}
