package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ContainerNamesPreChecker extends ActivityParamPreChecker {
    String Param = "container-names";

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String appCode = experimentActivityInfo.getAppCode();
        if (!MiniAppUtils.isK8S(appCode)) { return null; }
        String alias = sceneArgumentDefinition.getAlias();
        if (Param.equals(alias)) {
            String value = sceneArgumentDefinition.getValue();
            if (Strings.isNullOrEmpty(value)) { return null; }
            ParamAsserts.assertStringSplitterByDot(value);
        }
        return null;
    }

}
