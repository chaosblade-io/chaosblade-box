package com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.repository.ChaosToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunpeng
 *
 *
 */
@Component
@Order(value = 1200)
public class LitmusChaosInstallChecker implements ActivityDefinitionChecker {

    @Autowired
    private ChaosToolsRepository chaosToolsRepository;

    @Override
    public void check(ExperimentActivityDefinition experimentActivityDefinition, boolean fromApi)
            throws ActivityDefinitionIllegalException {
        String appCode = experimentActivityDefinition.getAppCode();
        if(MiniAppUtils.isFromLitmusChaos(appCode)) {
            List<Host> hostList = experimentActivityDefinition.getScope();
            Set<String> clusterIds = hostList.stream().map(Host::getClusterId).distinct().collect(Collectors.toSet());
            for(String clusterId : clusterIds) {
                chaosToolsRepository.selectByClusterId(clusterId).orElseThrow(
                        () -> new ChaosException(CommonErrorCode.B_LITMUS_CHAOS_NOT_INSTALL_ERROR)
                );
            }
        }
    }
}
