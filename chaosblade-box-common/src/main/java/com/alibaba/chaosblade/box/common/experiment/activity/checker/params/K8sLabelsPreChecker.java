package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class K8sLabelsPreChecker extends ActivityParamPreChecker {

    String Labels = "labels";
    String name="";

    private static Pattern regrex = Pattern.compile(
        "(\\S+=\\S+,)*(\\S+=\\S+)$");

    @Override
    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo,
        SceneArgumentDefinition sceneArgumentDefinition) {
        String appCode = experimentActivityInfo.getAppCode();
        if (!MiniAppUtils.isK8S(appCode)) { return null; }
        String alias = sceneArgumentDefinition.getAlias();
        if (Labels.equals(alias)) {
            String value = sceneArgumentDefinition.getValue();
            if (Strings.isNullOrEmpty(value)) { return null; }
            if (!regrex.matcher(value).find()) {
                throw new IllegalArgumentException("标签(Labels)输入不合法");
            }
        }
        return null;
    }

}
