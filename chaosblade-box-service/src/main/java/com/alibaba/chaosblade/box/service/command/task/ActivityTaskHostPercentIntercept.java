package com.alibaba.chaosblade.box.service.command.task;


import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
public interface ActivityTaskHostPercentIntercept {

    /**
     * 百分比选择机器
     * @return
     */
    List<ExperimentActivityDO> hostPercent(List<ExperimentActivityDO> activityDOS);


}
