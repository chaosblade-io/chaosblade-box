package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
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
public class DelayTimeOutMaxValuePreCheckContext extends ActivityParamPreChecker {

    private String TIME = "time";

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String appCode = experimentActivityInfo.getAppCode();
        ChaosBladeAction chaosBladeAction = ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);
        String alias = sceneArgumentDefinition.getAlias();
        if (isDelayAction(chaosBladeAction)) {
            if (alias.equals(TIME)) {
                ParamAsserts.assertNumberRightIn(sceneArgumentDefinition.getValue(), BigDecimal.valueOf(0),
                    BigDecimal.valueOf(270 * 1000));
            }
        }
        return null;
    }

    private boolean isDelayAction(ChaosBladeAction chaosBladeAction) {
        return chaosBladeAction != null && "delay".equals(chaosBladeAction.getAction());
    }

}
