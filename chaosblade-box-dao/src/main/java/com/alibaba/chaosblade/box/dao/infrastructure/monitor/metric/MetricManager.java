/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric;

import com.alibaba.chaosblade.box.dao.mapper.base.MetricMapper;
import com.alibaba.chaosblade.box.dao.model.ChaosMetricDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class MetricManager {

  @Autowired private MetricMapper metricMapper;

  /**
   * 查找metricDO
   *
   * @param host
   * @param start
   * @param from
   * @param metric
   * @return
   */
  public List<ChaosMetricDO> findMetricsByHostIp(
      String host, Long start, Long from, String metric) {
    QueryWrapper<ChaosMetricDO> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq("host", host)
        .between("timestamp", start, from)
        .eq("metric", metric)
        .orderByAsc("timestamp");
    return metricMapper.selectList(queryWrapper);
  }

  public List<ChaosMetricDO> findMetricsByConfigurationIdAndExpId(
      String configurationId, Long start, Long from, String metric, String expId) {
    QueryWrapper<ChaosMetricDO> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq("configuration_id", configurationId)
        .between("timestamp", start, from)
        .eq("metric", metric)
        .orderByAsc("timestamp")
        .eq("exp_id", expId);
    return metricMapper.selectList(queryWrapper);
  }

  public ChaosMetricDO findMetricsByConfigurationIdAndExpIdOrderByTimeStampDescLimit1(
      String configurationId, Long start, Long from, String metric, String expId) {
    QueryWrapper<ChaosMetricDO> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .eq("configuration_id", configurationId)
        .between("timestamp", start, from)
        .eq("metric", metric)
        .eq("exp_id", expId)
        .orderByDesc("timestamp")
        .last("limit 1");
    return metricMapper.selectOne(queryWrapper);
  }

  public void addChaosMetricDO(ChaosMetricDO chaosMetricDO) {
    ChaosMetricDO newChaosMetricDO = new ChaosMetricDO();
    newChaosMetricDO.setHost(chaosMetricDO.getHost());
    newChaosMetricDO.setConfigurationId(chaosMetricDO.getConfigurationId());
    newChaosMetricDO.setMetric(chaosMetricDO.getMetric());
    newChaosMetricDO.setTimestamp(System.currentTimeMillis());
    newChaosMetricDO.setSource(chaosMetricDO.getSource());
    newChaosMetricDO.setUnit(chaosMetricDO.getUnit());
    newChaosMetricDO.setValue(chaosMetricDO.getValue());
    newChaosMetricDO.setExpId(chaosMetricDO.getExpId());
    metricMapper.insert(newChaosMetricDO);
  }
}
