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
public class ChaosAppProbeParamPreChecker extends ActivityParamPreChecker {

    Integer maxTime = 20;

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String appcode = experimentActivityInfo.getAppCode();
        if (!appcode.startsWith("chaosapp.probe")) { return null; }
        String value = sceneArgumentDefinition.getValue();
        switch (sceneArgumentDefinition.getAlias()) {
            case "successThreshold":
                ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(0), BigDecimal.valueOf(maxTime));
                break;
            case "timeoutSeconds":
                ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(1), BigDecimal.valueOf(maxTime));
                break;
            case "failureThreshold":
                ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(1), BigDecimal.valueOf(maxTime));
                break;
            case "periodSeconds":
                ParamAsserts.assertNumberIn(value, BigDecimal.valueOf(1), BigDecimal.valueOf(maxTime));
                break;
            case "port":
                ParamAsserts.assertPort(value);
                break;
            default:
                break;
        }
        return new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
    }
}
