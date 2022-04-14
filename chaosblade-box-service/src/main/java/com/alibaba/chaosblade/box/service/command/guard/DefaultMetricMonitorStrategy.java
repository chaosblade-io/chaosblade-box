package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.ChaosAppInvoker;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMetricDataItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 兜底的用户查询
 *
 * @author haibin.lhb
 *
 *
 */
@Slf4j
@Component
public class DefaultMetricMonitorStrategy implements MonitorStrategy, GuardExecutableInterface {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    /**
     * metric的采集时间偏移量
     */
    private static Integer METRIC_DATE_COLLECT_TIME_OFFSET = 60 * 2;

    @Override
    public ExperimentGuardMonitorMetricResultEntity monitor(ExperimentGuardResultLoadRequest guardResultLoadRequest) {
        return requireExperimentGuardMonitorMetricResultEntity(guardResultLoadRequest.getExperimentGuardInstanceDO(),
            guardResultLoadRequest);
    }

    @Override
    public boolean support(ExperimentGuardResultLoadRequest guardResultLoadRequest) {
        return true;
    }

    private ExperimentGuardMonitorMetricResultEntity requireExperimentGuardMonitorMetricResultEntity(
        ExperimentGuardInstanceDO experimentGuardInstanceDO,
        ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
        ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity
            = experimentGuardInstanceDO.getValue();
        //调用小程序获取每台机器相对应的指标
        ExperimentTaskDO experimentTaskDO = experimentGuardResultLoadRequest.getExperimentTaskDO();
        Date startTime = experimentGuardInstanceDO.getValue().getLastRecordPoint() == null ? experimentTaskDO
            .getGmtCreate() : new Date(experimentGuardInstanceDO.getValue().getLastRecordPoint());
        Date toTime = experimentTaskDO.getGmtEnd() == null ? new Date() : experimentTaskDO.getGmtEnd();
        Map<Scope, List<ChaosMetricEntity>> metricEntities = invokeMetricMiniApp(
            experimentGuardResultLoadRequest.getExperimentTaskDO(), experimentGuardInstanceDO.getAppCode(),
            experimentGuardInstanceDO,
            experimentGuardInstanceDO.getArgument().getArguments(), startTime, DateUtils
                .addSeconds(toTime,
                    METRIC_DATE_COLLECT_TIME_OFFSET),
            experimentGuardResultLoadRequest.getHosts());
        //结果转换成list
        List<ExperimentGuardMetricDataItem> data = metricEntities.values().stream().flatMap(
            (Function<List<ChaosMetricEntity>, Stream<ExperimentGuardMetricDataItem>>)chaosMetricEntities -> chaosMetricEntities
                .stream().map(
                    chaosMetricEntity -> {
                        ExperimentGuardMetricDataItem experimentGuardMetricDataItem
                            = new ExperimentGuardMetricDataItem();
                        experimentGuardMetricDataItem.setValue(chaosMetricEntity.getValue());
                        experimentGuardMetricDataItem.setTimestamp(chaosMetricEntity.getTimestamp());
                        experimentGuardMetricDataItem.setGroup(chaosMetricEntity.getHost().getIp());
                        experimentGuardMonitorMetricResultEntity.setUnit(chaosMetricEntity.getUnit());
                        return experimentGuardMetricDataItem;
                    })).collect(Collectors.toList());
        fillSubName(experimentGuardMonitorMetricResultEntity, experimentGuardInstanceDO.getArgument().getArguments());
        experimentGuardMonitorMetricResultEntity.getData().addAll(data);
        return experimentGuardMonitorMetricResultEntity;
    }

    private void fillSubName(ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity,
        List<SceneArgumentDefinition> arguments) {
        arguments.stream().filter(
            sceneArgumentDefinition -> "metric".equals(sceneArgumentDefinition.getAlias())).forEach(
            new Consumer<SceneArgumentDefinition>() {
                @Override
                public void accept(SceneArgumentDefinition sceneArgumentDefinition) {
                    experimentGuardMonitorMetricResultEntity.setSubName(sceneArgumentDefinition.getValue());
                }
            });
    }

    @Override
    public ChaosAppResponse internalInvoke(ChaosAppContext chaosAppContext, ChaosAppRequest chaosAppRequest, String appCode,
                                        ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        if (MiniAppUtils.JVM_HIT_COUNT.equals(appCode)) {
            return handleJvmHitCount(chaosAppContext, chaosAppRequest, appCode, experimentGuardInstanceDO);
        } else {
            return handleCommonInternalInvoke(chaosAppContext, chaosAppRequest, appCode, experimentGuardInstanceDO);
        }
    }

    /**
     * jvm的需要特殊处理一下,只有注入成功了才去请求，并且参数增加expId
     *
     * @param chaosAppContext
     * @param chaosAppRequest
     * @param appCode
     * @param experimentGuardInstanceDO
     * @return
     */
    private ChaosAppResponse handleJvmHitCount(ChaosAppContext chaosAppContext, ChaosAppRequest chaosAppRequest, String appCode,
                                            ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByActivityTaskIdAndHost(
            experimentGuardInstanceDO.getActivityTaskId(),
            chaosAppRequest.getScope().getIp());
        if (chaosBladeExpUidDO == null) {
            return null;
        }
        //参数增加expId
        chaosAppRequest.getUserArgs().put(CommonConstant.METRIC_EXP_ID, chaosBladeExpUidDO.getExpUid());
        chaosAppContext.addData(CommonConstant.METRIC_CHAOSBLADE_EXP_OBJECT, chaosBladeExpUidDO);
        return handleCommonInternalInvoke(chaosAppContext, chaosAppRequest, appCode, experimentGuardInstanceDO);
    }

    public ChaosAppResponse handleCommonInternalInvoke(ChaosAppContext chaosAppContext, ChaosAppRequest chaosAppRequest,
                                                    String appCode,
                                                    ExperimentGuardInstanceDO experimentGuardInstanceDO) {
        return ChaosAppInvoker.invokeByGuard(chaosAppContext, chaosAppRequest, appCode, experimentGuardInstanceDO);
    }
}
