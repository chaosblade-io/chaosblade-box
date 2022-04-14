package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class CpuPercentPreCheckContext extends ActivityParamPreChecker {

    String CPU_PERCENT = "cpu-percent";

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String alias = sceneArgumentDefinition.getAlias();
        if (CPU_PERCENT.equals(alias)) {
            ParamAsserts.assertNumberRightIn(sceneArgumentDefinition.getValue(), BigDecimal.valueOf(0),
                BigDecimal.valueOf(100));
        }
        return null;
    }
}
