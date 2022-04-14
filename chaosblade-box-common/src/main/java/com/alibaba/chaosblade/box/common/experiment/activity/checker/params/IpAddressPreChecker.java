package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author haibin.lhb
 * 
 *
 */
@Component
public class IpAddressPreChecker extends ActivityParamPreChecker {

    private static Set<String> ipAlias = new HashSet<>();

    static {
        ipAlias.add("destination-ip");
        ipAlias.add("exclude-ip");
    }

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String alias = sceneArgumentDefinition.getAlias();
        if (Strings.isNullOrEmpty(sceneArgumentDefinition.getValue())) { return null; }
        if (ipAlias.contains(alias)) {
            List<String> values = Splitter.on(",").splitToList(sceneArgumentDefinition.getValue());
            for (String item : values) {
                ParamAsserts.assertValidIp(item);
            }
        }
        return null;
    }
}
