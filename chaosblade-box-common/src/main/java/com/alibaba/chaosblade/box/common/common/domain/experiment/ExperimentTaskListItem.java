package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ExperimentTaskListItem extends ExperimentTask {

    private List<ExperimentTaskListItem> latestTasks;
}
