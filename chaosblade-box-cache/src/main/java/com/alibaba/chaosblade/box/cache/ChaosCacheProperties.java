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

package com.alibaba.chaosblade.box.cache;

import com.alibaba.chaosblade.box.cache.config.DistributedCacheConfig;
import com.alibaba.chaosblade.box.cache.config.LocalCacheConfig;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/7
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "chaos.cache")
public class ChaosCacheProperties {

  boolean enable;
  boolean autoSync;
  boolean suppressException = true;
  LocalCacheConfig localCache;
  DistributedCacheConfig distributedCache;

  /**
   * Spring cache configuration. Such as enable, etc. e.g Set 'chaos.cache.spring.enable' as 'true'
   * to enable spring cache supported.
   */
  Map<String, String> spring;
}
