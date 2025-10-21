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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow;

import com.alibaba.chaosblade.box.common.app.sdk.constants.EnvironmentEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.SpringUtils;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppLoaderConfig;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppProcessor;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeListener;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosListener;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker.MiniAppInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseActivityInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.strategy.ActivityTargetsRunnableStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** @author haibin */
@Slf4j
public class FlowEngineFactoryBean implements FactoryBean<FlowEngine>, ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Value("${chaos.env}")
  private String env;

  private ChaosFlowConfig chaosFlowConfig;

  private static ChaosFlowEngine chaosFlowEngine;

  public FlowEngineFactoryBean() {
    this.chaosFlowConfig = new ChaosFlowConfig();
  }

  @Override
  public FlowEngine getObject() throws Exception {
    if (chaosFlowEngine == null) {
      chaosFlowEngine = new ChaosFlowEngine(chaosFlowConfig);
      initEngine();
    }
    return chaosFlowEngine;
  }

  @Override
  public Class<?> getObjectType() {
    return FlowEngine.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  public void initEngine() {
    EnvironmentEnum environmentEnum = EnvironmentEnum.of(env);
    if (environmentEnum == null) {
      throw new IllegalArgumentException("chaos.env is illegal");
    }
    chaosFlowConfig.setChaosBladeListener(
        SpringUtils.getBean(applicationContext, ChaosBladeListener.class));
    chaosFlowConfig.setLitmusChaosListener(
        SpringUtils.getBean(applicationContext, LitmusChaosListener.class));

    chaosFlowConfig.setActivityTargetsRunnableStrategies(
        SpringUtils.getBeans(applicationContext, ActivityTargetsRunnableStrategy.class));
    chaosFlowConfig.setChaosAppLoaderConfig(initChaosAppLoaderConfig());
    chaosFlowEngine
        .getFlowEngineContext()
        .setAppInvokeInterceptors(
            SpringUtils.getBeans(applicationContext, BaseMiniAppInvokeInterceptor.class));
    chaosFlowEngine
        .getFlowEngineContext()
        .setActivityInvokeInterceptors(
            SpringUtils.getBeans(applicationContext, BaseActivityInvokeInterceptor.class));
    chaosFlowEngine.getFlowEngineContext().setEnvironment(environmentEnum);
    chaosFlowConfig.setMiniAppInvokers(
        SpringUtils.getBeans(applicationContext, MiniAppInvoker.class));
    chaosFlowConfig.setChaosBladeInvoker(
        SpringUtils.getBean(applicationContext, ChaosBladeInvoker.class));
    chaosFlowConfig.setLitmusChaosInvoker(
        SpringUtils.getBean(applicationContext, LitmusChaosInvoker.class));
    try {
      chaosFlowEngine.init();
    } catch (Exception e) {
      throw new BeanCreationException("init chaos-flow failed", e);
    }
  }

  private ChaosAppLoaderConfig initChaosAppLoaderConfig() {
    ChaosAppLoaderConfig chaosAppLoaderConfig = new ChaosAppLoaderConfig();
    chaosAppLoaderConfig.setMiniAppProcessors(
        SpringUtils.getBeans(applicationContext, MiniAppProcessor.class));
    return chaosAppLoaderConfig;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
