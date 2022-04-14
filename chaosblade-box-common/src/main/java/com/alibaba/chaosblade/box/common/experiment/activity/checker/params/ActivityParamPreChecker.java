package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;


import com.alibaba.chaosblade.box.common.infrastructure.SceneArgumentGradeConverter;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
public abstract class ActivityParamPreChecker {

    @Autowired
    private SceneArgumentGradeConverter sceneArgumentGradeConverter;

    /**
     * activity参数预校验
     *
     * @param activityParamPreCheckContext
     * @param experimentActivityInfo       当前节点信息
     * @return
     */
    public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
        ActivityParamPreCheckContext activityParamPreCheckContext,
        ExperimentActivityInfo experimentActivityInfo) {
        return checkArguments(activityParamPreCheckContext, experimentActivityInfo);
    }

    protected List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> checkArguments(
            ActivityParamPreCheckContext activityParamPreCheckContext,
            ExperimentActivityInfo experimentActivityInfo) {
        return sceneArgumentGradeConverter.tranToArgumentsList(experimentActivityInfo.getArguments()).stream().map(
                new Function<SceneArgumentDefinition, ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult>() {
                    @Override
                    public ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult apply(SceneArgumentDefinition sceneArgumentDefinition) {
                        try {
                            return checkArgument(activityParamPreCheckContext, experimentActivityInfo,
                                    sceneArgumentDefinition);
                        } catch (NumberFormatException numberFormatException) {
                            ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult activityGroupDefinitionParamCheckResult
                                    = new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
                            activityGroupDefinitionParamCheckResult.setAlias(sceneArgumentDefinition.getAlias());
                            activityGroupDefinitionParamCheckResult.setError("数字不合法");
                            return activityGroupDefinitionParamCheckResult;
                        } catch (IllegalArgumentException illegalArgumentException) {
                            ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult activityGroupDefinitionParamCheckResult
                                    = new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
                            activityGroupDefinitionParamCheckResult.setAlias(sceneArgumentDefinition.getAlias());
                            activityGroupDefinitionParamCheckResult.setError(illegalArgumentException.getMessage());
                            return activityGroupDefinitionParamCheckResult;
                        }
                    }
                }).filter(new Predicate<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult>() {
            @Override
            public boolean test(ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult activityGroupDefinitionParamCheckResult) {
                return activityGroupDefinitionParamCheckResult != null && !Strings.isNullOrEmpty(
                        activityGroupDefinitionParamCheckResult.getError());
            }
        }).collect(Collectors.toList());
    }

    protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult checkArgument(
            ActivityParamPreCheckContext activityParamPreCheckContext,
            ExperimentActivityInfo experimentActivityInfo, SceneArgumentDefinition sceneArgumentDefinition) {
        String value = sceneArgumentDefinition.getValue();
        String alias = sceneArgumentDefinition.getAlias();
        ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult activityGroupDefinitionParamCheckResult
                = new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
        activityGroupDefinitionParamCheckResult.setAlias(alias);
        //非必填参数可以跳过检验
        if (Strings.isNullOrEmpty(value) && !sceneArgumentDefinition.getComponent().isRequired()) {
            return activityGroupDefinitionParamCheckResult;
        }
        activityGroupDefinitionParamCheckResult = internalCheckArgument(activityParamPreCheckContext,
                experimentActivityInfo, sceneArgumentDefinition);
        if (activityGroupDefinitionParamCheckResult == null) {
            activityGroupDefinitionParamCheckResult = new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
        }
        activityGroupDefinitionParamCheckResult.setAlias(alias);
        return activityGroupDefinitionParamCheckResult;
    }

    abstract protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult internalCheckArgument(
        ActivityParamPreCheckContext activityParamPreCheckContext,
        ExperimentActivityInfo experimentActivityInfo, SceneArgumentDefinition sceneArgumentDefinition);

}
