package com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ChaosMetricDO;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Slf4j
public class JvmHitCountMetricProvider implements MetricProvider {

    @Autowired
    private MetricManager metricManager;

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Override
    public List<ChaosMetricEntity> provide(MetricDefinition metricDefinition, MetricRequest metricRequest) {
        if (!HIT_COUNT_CATEGORY.equalsIgnoreCase(metricDefinition.getCategory())) {
            return new ArrayList<>();
        }
        String expId = metricRequest.getExpId();
        if (Strings.isNullOrEmpty(expId)) {
            return new ArrayList<>();
        }
        Host host = metricRequest.getHost();
        if (host == null) {
            return new ArrayList<>();
        }
        //查询一下最新的
        ChaosBladeExpUidDO chaosBladeExpUidDO = (ChaosBladeExpUidDO)metricRequest.getAttribute(
            CommonConstant.METRIC_CHAOSBLADE_EXP_OBJECT);
        if (chaosBladeExpUidDO == null) {
            log.warn("chaosblade expObject is null,expId:" + expId);
            return new ArrayList<>();
        }
        Response<Long> response = chaosBladeInvoker.getHitCountInJvm(
            new ChaosBladeExpQueryRequest(host, expId, MiniAppUtils.isK8S(chaosBladeExpUidDO.getAppCode())));
        if (response.isSuccess()) {
            ChaosMetricDO chaosMetricDO = new ChaosMetricDO();
            chaosMetricDO.setSource(MetricSource.ChaosBlade.getValue());
            chaosMetricDO.setConfigurationId(host.getDeviceConfigurationId());
            chaosMetricDO.setHost(host.getIp());
            chaosMetricDO.setConfigurationId(host.getDeviceConfigurationId());
            chaosMetricDO.setMetric(metricDefinition.getKey());
            chaosMetricDO.setValue(response.getResult() == null ? 0f : Float.parseFloat(response.getResult() + ""));
            chaosMetricDO.setUnit(metricDefinition.getUnit());
            chaosMetricDO.setExpId(expId);
            metricManager.addChaosMetricDO(chaosMetricDO);
        }
        return metricManager.findMetricsByConfigurationIdAndExpId(host.getDeviceConfigurationId(),
            metricRequest.getFrom(),
            metricRequest.getTo(), metricDefinition.getKey(), expId).stream().map(
                chaosMetricDO -> {
                ChaosMetricEntity chaosMetricEntity = new ChaosMetricEntity();
                chaosMetricEntity.setUnit(metricDefinition.getUnit());
                chaosMetricEntity.setHost(host);
                chaosMetricEntity.setTimestamp(chaosMetricDO.getTimestamp());
                chaosMetricEntity.setValue(chaosMetricDO.getValue());
                chaosMetricEntity.setMetric(chaosMetricDO.getMetric());
                return chaosMetricEntity;
            }).collect(Collectors.toList());
    }

    @Override
    public Map<Host, List<ChaosMetricEntity>> batchProvider(List<Host> hosts, MetricDefinition metricDefinition,
        MetricRequest metricRequest) {
        return null;
    }

}
