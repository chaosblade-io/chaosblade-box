package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class CpuFullLoadActivityParamPreChecker extends ActivityParamPreChecker {
    @Override
    public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo) {
        if (MiniAppUtils.isCpuFullLoad(experimentActivityInfo.getAppCode())) {
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
        if ("cpu-percent".equals(alias)) {
            ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(0), BigDecimal.valueOf(100));
        }
        return new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
    }
}
