package com.alibaba.chaosblade.box.common.common.domain.task;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskStopOption implements Serializable {

    /**
     * 是否同步返回停止结果
     */
    private boolean sync;

    private String experimentId;

}
