package com.alibaba.chaosblade.box.common.experiment.clientobject;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskStats implements Serializable {

    private List<ExperimentTaskStat> items;

}
