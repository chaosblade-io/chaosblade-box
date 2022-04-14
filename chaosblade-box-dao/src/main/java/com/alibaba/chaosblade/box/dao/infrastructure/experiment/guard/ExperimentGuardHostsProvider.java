package com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface ExperimentGuardHostsProvider {

    /**
     * 提供hosts
     *
     * @param experimentGuardInstanceExecutionRequest
     * @return
     */
    public List<Host> provide(ExperimentGuardInstanceExecutionRequest experimentGuardInstanceExecutionRequest);
}
