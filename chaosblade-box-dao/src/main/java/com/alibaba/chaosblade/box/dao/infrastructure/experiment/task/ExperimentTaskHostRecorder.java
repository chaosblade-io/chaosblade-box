package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

import java.util.Set;

/**
 * @author haibin
 *
 *
 */
public interface ExperimentTaskHostRecorder {
    /**
     * 记录hosts
     *
     * @param experimentTaskDO
     * @param hosts
     */
    public void record(ExperimentTaskDO experimentTaskDO, Set<Scope> hosts);
}
