/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.metric;

import com.alibaba.chaosblade.platform.cmmon.utils.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static com.alibaba.chaosblade.platform.cmmon.exception.ExceptionMessageEnum.METRIC_NO_SERVICE;

/**
 * @author yefei
 */
@Service("metricService")
public class MetricStrategyContext implements MetricService, BeanPostProcessor {

    private Map<MetricSource, MetricService> strategies = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<MetricChartLineResponse> selectChartLine(MetricChartLineRequest metricChartLineRequest) {

        String category = metricChartLineRequest.getCategoryCode();
        String[] split = category.split("[.]");
        MetricService metricService = strategies.get(MetricSource.parse(split[1]));

        Preconditions.checkNotNull(category, METRIC_NO_SERVICE);

        return metricService.selectChartLine(metricChartLineRequest);
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof MetricService) {
            MetricStrategy strategy = o.getClass().getAnnotation(MetricStrategy.class);
            strategies.put(strategy.value(), (MetricService) o);
        }
        return o;
    }

}
