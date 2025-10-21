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

package com.alibaba.chaosblade.box.common.config;

import com.alibaba.chaosblade.box.common.config.converter.StringToIntegerArgumentConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class DefaultChaosProperties implements InitializingBean {

  @Autowired private ChaosSettings chaosSettings;

  public Integer getMiniAppMaxBatchSize() {
    return miniAppMaxBatchSize;
  }

  @ChaosSettingDescriptor(
      group = "common",
      description = "小程序并发执行的最大数目",
      name = "chaos.miniapp.execution.batch.size",
      converters = StringToIntegerArgumentConverter.class)
  private Integer miniAppMaxBatchSize = Runtime.getRuntime().availableProcessors() * 7;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.chaosSettings.registerSettings(this);
  }
}
