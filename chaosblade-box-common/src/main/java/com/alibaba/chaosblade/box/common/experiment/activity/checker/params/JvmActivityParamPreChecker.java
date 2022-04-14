package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
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
public class JvmActivityParamPreChecker extends ActivityParamPreChecker {
    private static Set<String> fullPackageRequiredParams = new HashSet<>();

    static {
        fullPackageRequiredParams.add("exception");
    }

    @Override
    public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo) {
        if (MiniAppUtils.isJvmExcludeAgent(experimentActivityInfo.getAppCode())) {
            return checkArguments(activityParamPreCheckContext, experimentActivityInfo);
        }
        return null;
    }

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String value = sceneArgumentDefinition.getValue();
        String alias = sceneArgumentDefinition.getAlias();
        if (fullPackageRequiredParams.contains(alias)) {
            ParamAsserts.assertPackage(value);
        }
        return new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
    }
}
