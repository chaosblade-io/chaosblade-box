package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

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
public class TimeOutMaxValuePreCheckContext extends ActivityParamPreChecker {

    private static String TIMEOUT = "timeout";

    public static long MAX_TIME_OUT = 260000L;

    @Override
    public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo) {
        return checkArguments(activityParamPreCheckContext, experimentActivityInfo);
    }

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String alias = sceneArgumentDefinition.getAlias();
        if (alias.equals(TIMEOUT)) {
            ParamAsserts.assertNumberRightIn(sceneArgumentDefinition.getValue(), BigDecimal.valueOf(0),
                BigDecimal.valueOf(MAX_TIME_OUT));
        }
        return null;
    }
}
