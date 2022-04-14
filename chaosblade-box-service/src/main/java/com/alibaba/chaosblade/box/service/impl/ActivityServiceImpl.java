package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.experiment.activity.checker.params.ActivityParamPreCheckContext;
import com.alibaba.chaosblade.box.common.experiment.activity.checker.params.ActivityParamPreChecker;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckResponse;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MethodInvokeUtil;
import com.alibaba.chaosblade.box.service.ActivityService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private List<ActivityParamPreChecker> activityParamPreCheckers;

    @Override
    public Response<ActivityGroupDefinitionCheckResponse> checkActivityGroupDefinition(
        ActivityGroupDefinitionCheckRequest activityGroupDefinitionCheckRequest) {
        ActivityGroupDefinitionCheckResponse activityGroupDefinitionCheckResponse
            = new ActivityGroupDefinitionCheckResponse();
        return MethodInvokeUtil.invoke(() -> {
            Map<String, List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult>> idToResults = new HashMap<>();
            if (!CollectionUtil.isNullOrEmpty(activityGroupDefinitionCheckRequest.getFlows())) {
                List<MiniFlow> miniFlows = activityGroupDefinitionCheckRequest.getFlows();
                ActivityParamPreCheckContext activityParamPreCheckContext = new ActivityParamPreCheckContext();
                activityParamPreCheckContext.setAppId(activityGroupDefinitionCheckRequest.getAppId());
                activityParamPreCheckContext.setHosts(activityGroupDefinitionCheckRequest.getHosts());
                for (MiniFlow miniFlow : miniFlows) {
                    miniFlow.getPrepare().forEach(
                        experimentActivityInfo -> idToResults.computeIfAbsent(experimentActivityInfo.getFontId(),
                            s -> new ArrayList<>()).addAll(
                            doCheck(activityParamPreCheckContext, experimentActivityInfo)));
                    miniFlow.getAttack().forEach(
                        experimentActivityInfo -> idToResults.computeIfAbsent(experimentActivityInfo.getFontId(),
                            s -> new ArrayList<>()).addAll(
                            doCheck(activityParamPreCheckContext, experimentActivityInfo)));
                    miniFlow.getCheck().forEach(
                        experimentActivityInfo -> idToResults.computeIfAbsent(experimentActivityInfo.getFontId(),
                            s -> new ArrayList<>()).addAll(
                            doCheck(activityParamPreCheckContext, experimentActivityInfo)));
                    miniFlow.getRecover().forEach(
                        experimentActivityInfo -> idToResults.computeIfAbsent(experimentActivityInfo.getFontId(),
                            s -> new ArrayList<>()).addAll(
                            doCheck(activityParamPreCheckContext, experimentActivityInfo)));
                }
            }
            List<ActivityGroupDefinitionCheckItem> activityGroupDefinitionCheckItems = idToResults.entrySet()
                .stream()
                .filter(
                    stringListEntry -> !stringListEntry.getValue().isEmpty()).map(
                    stringListEntry -> {
                        ActivityGroupDefinitionCheckItem activityGroupDefinitionCheckItem
                            = new ActivityGroupDefinitionCheckItem();
                        activityGroupDefinitionCheckItem.setFrontId(stringListEntry.getKey());
                        activityGroupDefinitionCheckItem.setParams(stringListEntry.getValue());
                        return activityGroupDefinitionCheckItem;
                    }).collect(Collectors.toList());
            activityGroupDefinitionCheckResponse.setDetails(activityGroupDefinitionCheckItems);
            activityGroupDefinitionCheckResponse.setPassed(activityGroupDefinitionCheckItems.stream().flatMap(
                (Function<ActivityGroupDefinitionCheckItem, Stream<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult>>) activityGroupDefinitionCheckItem -> activityGroupDefinitionCheckItem
                    .getParams().stream()).allMatch(
                activityGroupDefinitionParamCheckResult -> Strings
                    .isNullOrEmpty(activityGroupDefinitionParamCheckResult.getError())));
            return Response.okWithData(activityGroupDefinitionCheckResponse);
        }, throwable -> {
            log.error("check param error", throwable);
            activityGroupDefinitionCheckResponse.setPassed(true);
            return Response.okWithData(activityGroupDefinitionCheckResponse);
        });
    }

    private List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> doCheck(
        ActivityParamPreCheckContext activityParamPreCheckContext, ExperimentActivityInfo experimentActivityInfo) {
        return activityParamPreCheckers.stream().flatMap(
            new Function<ActivityParamPreChecker, Stream<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult>>() {
                @Override
                public Stream<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> apply(
                    ActivityParamPreChecker activityParamPreChecker) {
                    return Optional.ofNullable(
                        activityParamPreChecker.preCheck(activityParamPreCheckContext, experimentActivityInfo)).orElse(
                        new ArrayList<>()).stream();
                }
            }).collect(Collectors.toList());
    }
}
