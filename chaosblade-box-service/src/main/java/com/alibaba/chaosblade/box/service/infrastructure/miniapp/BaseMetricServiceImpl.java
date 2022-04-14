package com.alibaba.chaosblade.box.service.infrastructure.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.metric.MetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.metric.ChaosMetricEntity;
import com.alibaba.chaosblade.box.common.infrastructure.metric.UserDefinitionMetricRequest;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.metric.miniapp.MetricDefinitionRepository;
import com.alibaba.chaosblade.box.common.infrastructure.util.HttpClient;
import com.alibaba.chaosblade.box.common.infrastructure.util.HttpResponseFuture;
import com.alibaba.chaosblade.box.dao.infrastructure.service.MetricService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Slf4j
public abstract class BaseMetricServiceImpl implements MetricService {

    @Autowired
    private MetricDefinitionRepository metricDefinitionRepository;

    /**
     * 因为appCode的格式是chaosapp.metric.cpu,类目是cpu
     *
     * @param appCode
     * @return
     */
    protected String parserCategoryFromAppCode(String appCode) {
        if (appCode == null) { return null; }
        String[] splits = appCode.split("\\.");
        if (splits.length >= 2) {
            return splits[2];
        } else {
            return appCode;
        }
    }

    public String splitKey(String requestKey) {
        if (Strings.isNullOrEmpty(requestKey)) { return null; }
        return requestKey.split("\\(")[0];
    }

    /**
     * 展现给用户是可读的数据，code(name)这样的格式
     *
     * @param internalMetricDefinition
     * @return
     */
    private String convertReadableKey(MetricDefinition internalMetricDefinition) {
        return internalMetricDefinition.getKey() + "(" + internalMetricDefinition.getName() + ")";
    }

    @Override
    public Response<List<ChaosMetricEntity>> getTargetMetric(MetricRequest metricRequest) {
        String metricKey = splitKey(metricRequest.getKey());
        MetricDefinition metricDefinition = metricDefinitionRepository.findByMetric(metricKey).orElse(null);
        if (metricDefinition == null) {
            log.info("not found metric definition in metric.json by key:" + metricKey);
            metricDefinition = new MetricDefinition();
            metricDefinition.setKey(metricKey);
        }
        return Response.okWithData(getMetrics(metricDefinition, metricRequest));
    }

    protected abstract List<ChaosMetricEntity> getMetrics(MetricDefinition metricDefinition, MetricRequest metricRequest);

    @Override
    public Response<Map<String, List<ChaosMetricEntity>>> batchGetTargetsMetric(List<Host> hosts,
        MetricRequest metricRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response<List<String>> getMetricKeysByAppCode(String metricAppCode) {
        String category = parserCategoryFromAppCode(metricAppCode);
        List<MetricDefinition> metricDefinitions = metricDefinitionRepository.findByCategory(category);
        return Response.okWithData(metricDefinitions.stream().map(new Function<MetricDefinition, String>() {
            @Override
            public String apply(MetricDefinition metricDefinition) {
                return convertReadableKey(metricDefinition);
            }
        }).collect(Collectors.toList()));
    }

    @Override
    public Response<List<ChaosMetricEntity>> getUserDefinitionMetric(
        UserDefinitionMetricRequest userDefinitionMetricRequest) {
        String url = userDefinitionMetricRequest.getUrl();
        HttpResponseFuture<ArrayList<ChaosMetricEntity>> httpResponseFuture = new HttpResponseFuture<>();
        Response<List<ChaosMetricEntity>> response = new Response<>();
        String jsonBody = userDefinitionMetricRequest.getJsonBody();
        Map<String, String> postBody = new HashMap<>();
        if (jsonBody != null) {
            postBody = JSON.parseObject(jsonBody, new TypeReference<HashMap<String, String>>() {});
        }
        if (userDefinitionMetricRequest.getHost() != null) {
            postBody.put(CommonConstant.METRIC_FIELD_HOST, userDefinitionMetricRequest.getHost().getIp());
        }
        postBody.put(CommonConstant.METRIC_FIELD_START, userDefinitionMetricRequest.getFrom() + "");
        postBody.put(CommonConstant.METRIC_FIELD_END, userDefinitionMetricRequest.getTo() + "");
        httpResponseFuture = HttpClient.postJsonString(url, JSON.toJSONString(postBody),
            new TypeReference<ArrayList<ChaosMetricEntity>>() {});
        httpResponseFuture.onThrowable(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                response.setError(
                    new ChaosError(CommonErrorCode.S_REQUEST_OUTER_SYSTEM_FAILED, throwable.getMessage()));
                response.setSuccess(false);
            }
        }).onResponse(new Consumer<ArrayList<ChaosMetricEntity>>() {
            @Override
            public void accept(ArrayList<ChaosMetricEntity> chaosMetricEntities) {
                chaosMetricEntities.forEach(new Consumer<ChaosMetricEntity>() {
                    @Override
                    public void accept(ChaosMetricEntity chaosMetricEntity) {
                        chaosMetricEntity.setHost(userDefinitionMetricRequest.getHost());
                    }
                });
                response.setResult(chaosMetricEntities);
                response.setSuccess(true);
            }
        });
        return response;
    }
}
