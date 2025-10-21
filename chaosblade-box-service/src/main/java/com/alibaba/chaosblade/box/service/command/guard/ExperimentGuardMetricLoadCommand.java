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

package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.cache.templates.distributed.RedisCacheTemplate;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.alibaba.chaosblade.box.service.ExperimentGuardInstanceService;
import com.alibaba.chaosblade.box.service.impl.ExperimentGuardInstanceServiceImpl;
import java.io.Serializable;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 当全局节点为观察类型的时候,用来加载当前机器指标
 *
 * <p>根据观察节点的指标配置，通过调用指定的小程序来获取到每台机器的指标数据 当前我们的指标并没有单独存储在时序数据库里面,这个其实是有一定风险的,最好就是将指标单独存储
 *
 * @author haibin
 */
@Component
@Slf4j
public class ExperimentGuardMetricLoadCommand
    extends BaseExperimentGuardLoadCommand<ExperimentGuardMonitorMetricResultEntity> {

  @Autowired private MetricMonitorStrategyRepos metricMonitorStrategyRepos;

  @Autowired private ExperimentGuardInstanceService experimentGuardInstanceService;

  @Autowired private RedisCacheTemplate redisTemplate;

  @Override
  public ExperimentGuardMonitorMetricResultEntity internalExecute(
      ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
    // 获取当前的实例
    ExperimentGuardInstanceDO experimentGuardInstanceDO =
        experimentGuardResultLoadRequest.getExperimentGuardInstanceDO();
    Optional<MonitorStrategy> monitorStrategyOptional =
        metricMonitorStrategyRepos.select(experimentGuardResultLoadRequest);
    if (!monitorStrategyOptional.isPresent()) {
      log.warn("not found suitable monitor strategy");
      return new ExperimentGuardMonitorMetricResultEntity();
    }
    MonitorStrategy monitorStrategy = monitorStrategyOptional.get();
    ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity =
        experimentGuardInstanceDO.getValue();

    if (ExperimentGuardDO.ACTION_TYPE_MONITOR.equals(experimentGuardInstanceDO.getActionType())) {
      experimentGuardMonitorMetricResultEntity =
          getExperimentGuardMonitorMetricResultEntityFormRedis(experimentGuardInstanceDO);
      experimentGuardInstanceDO.setValue(experimentGuardMonitorMetricResultEntity);
    }

    if (experimentGuardMonitorMetricResultEntity == null) {
      experimentGuardMonitorMetricResultEntity = new ExperimentGuardMonitorMetricResultEntity();
      experimentGuardInstanceDO.setValue(experimentGuardMonitorMetricResultEntity);
    }
    experimentGuardMonitorMetricResultEntity.setName(experimentGuardInstanceDO.getName());
    experimentGuardMonitorMetricResultEntity.setState(experimentGuardInstanceDO.getState());
    experimentGuardMonitorMetricResultEntity.setGuardId(experimentGuardInstanceDO.getGuardId());
    experimentGuardMonitorMetricResultEntity =
        monitorStrategy.monitor(experimentGuardResultLoadRequest);
    saveExperimentGuardMonitorMetricResultEntity(
        experimentGuardInstanceDO, experimentGuardMonitorMetricResultEntity);
    experimentGuardInstanceDO.setValue(experimentGuardMonitorMetricResultEntity);
    return experimentGuardMonitorMetricResultEntity;
  }

  private void saveExperimentGuardMonitorMetricResultEntity(
      ExperimentGuardInstanceDO experimentGuardInstanceDO,
      ExperimentGuardMonitorMetricResultEntity experimentGuardMonitorMetricResultEntity) {
    experimentGuardInstanceDO.setValue(experimentGuardMonitorMetricResultEntity);
    experimentGuardInstanceService.updateExperimentGuardInstance(experimentGuardInstanceDO);
  }

  private ExperimentGuardMonitorMetricResultEntity
      getExperimentGuardMonitorMetricResultEntityFormRedis(
          ExperimentGuardInstanceDO experimentGuardInstanceDO) {
    Serializable serializable =
        redisTemplate.prefixGet(
            ExperimentGuardInstanceServiceImpl.PRE, experimentGuardInstanceDO.getInstanceId());
    if (null != serializable) {
      return (ExperimentGuardMonitorMetricResultEntity) serializable;
    }
    return new ExperimentGuardMonitorMetricResultEntity();
  }
}
