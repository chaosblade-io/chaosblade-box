package com.alibaba.chaosblade.box.common.experiment.clientobject;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentTaskStats implements Serializable {

  private List<ExperimentTaskStat> items;
}
