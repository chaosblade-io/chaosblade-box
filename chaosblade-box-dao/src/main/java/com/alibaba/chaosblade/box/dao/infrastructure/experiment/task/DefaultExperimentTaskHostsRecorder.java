package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
public class DefaultExperimentTaskHostsRecorder implements ExperimentTaskHostRecorder {

    @Autowired
    private ExperimentHostRelationRepository experimentHostRelationRepository;

    @Override
    public void record(ExperimentTaskDO experimentTaskDO, Set<Scope> hosts) {
        List<ExperimentHostRelationDO> experimentHostRelationDOS = hosts.stream().map(
            scope -> {
                ExperimentHostRelationDO experimentHostRelationDO = ExperimentHostRelationDO.build(scope,
                    experimentTaskDO);
                if (scope.isK8s() && !Objects.equals(scope.getDeviceConfigurationId(), scope.getAppConfigurationId())) {
                    experimentHostRelationDO.setInjectionTargetType(ExperimentHostRelationDO.INJECTION_TYPE_POD);
                } else {
                    experimentHostRelationDO.setInjectionTargetType(ExperimentHostRelationDO.INJECTION__TYPE_HOST);
                }
                experimentHostRelationDO.setInjectionTargetName(scope.getDeviceName());
                return experimentHostRelationDO;
            }).collect(Collectors.toList());
        experimentHostRelationRepository.saveBatch(experimentHostRelationDOS);
    }
}
