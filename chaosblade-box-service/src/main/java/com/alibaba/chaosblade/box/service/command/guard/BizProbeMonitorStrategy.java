package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosAppInvoker;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMetricDataItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class BizProbeMonitorStrategy implements MonitorStrategy {

    @Override
    public ExperimentGuardMonitorMetricResultEntity monitor(ExperimentGuardResultLoadRequest guardResultLoadRequest) {
        log.info("enter BizProbeMonitorStrategy....");

        ExperimentGuardInstanceDO experimentGuardInstanceDO = guardResultLoadRequest.getExperimentGuardInstanceDO();
        ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity = experimentGuardInstanceDO
            .getValue();
        List<ExperimentGuardMetricDataItem>
            experimentGuardMetricDataItemList = getExperimentGuardMetricDataItems(experimentGuardInstanceDO,
            experimentGuardMonitorMetricResultEntity);
        experimentGuardMonitorMetricResultEntity.getData().addAll(experimentGuardMetricDataItemList);
        log.info("finish BizProbeMonitorStrategy....");
        return experimentGuardMonitorMetricResultEntity;
    }

    private List<ExperimentGuardMetricDataItem> getExperimentGuardMetricDataItems(
        ExperimentGuardInstanceDO experimentGuardInstanceDO,
        ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity) {
        ChaosAppContext chaosAppContext = new ChaosAppContext();
        ChaosAppRequest chaosAppRequest = new ChaosAppRequest();
        Map<String, String> userArgs = new HashMap<>();
        for (SceneArgumentDefinition sceneArgumentDefinition : experimentGuardInstanceDO.getArgument().getArguments()) {
            userArgs.put(sceneArgumentDefinition.getAlias(), sceneArgumentDefinition.getValue());
        }
        chaosAppRequest.setUserArgs(userArgs);
        ChaosAppResponse chaosAppResponse = ChaosAppInvoker.invokeByGuard(chaosAppContext, chaosAppRequest,
            experimentGuardInstanceDO.getAppCode(), experimentGuardInstanceDO);
        List<ExperimentGuardMetricDataItem> experimentGuardMetricDataItemList = new ArrayList<>();
        if (chaosAppResponse != null && chaosAppResponse.isSuccess()) {
            List<ChaosMetricEntity> chaosMetricEntities = (List<ChaosMetricEntity>)chaosAppResponse.getData().get("response");
            experimentGuardMetricDataItemList = chaosMetricEntities.stream().map(
                chaosMetricEntity -> {
                    experimentGuardMonitorMetricResultEntity.setUnit(chaosMetricEntity.getUnit());
                    ExperimentGuardMetricDataItem experimentGuardMetricDataItem
                        = new ExperimentGuardMetricDataItem();
                    experimentGuardMetricDataItem.setValue(chaosMetricEntity.getValue());
                    experimentGuardMetricDataItem.setTimestamp(chaosMetricEntity.getTimestamp());
                    return experimentGuardMetricDataItem;
                }).collect(
                Collectors.toList());
        }
        return experimentGuardMetricDataItemList;
    }

    @Override
    public boolean support(ExperimentGuardResultLoadRequest guardResultLoadRequest) {
        String appCode = guardResultLoadRequest.getExperimentGuardInstanceDO().getAppCode();
        return appCode.startsWith("chaosapp.probe.biz");
    }
}
