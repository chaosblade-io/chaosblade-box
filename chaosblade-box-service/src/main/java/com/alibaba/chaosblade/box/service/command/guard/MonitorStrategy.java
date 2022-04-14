package com.alibaba.chaosblade.box.service.command.guard;


import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;

/**
 * @author haibin.lhb
 *
 *
 */
public interface MonitorStrategy {

    public ExperimentGuardMonitorMetricResultEntity monitor(ExperimentGuardResultLoadRequest guardResultLoadRequest);

    public boolean support(ExperimentGuardResultLoadRequest guardResultLoadRequest);
}
