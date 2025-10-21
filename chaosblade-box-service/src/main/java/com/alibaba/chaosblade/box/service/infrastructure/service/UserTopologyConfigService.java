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

package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.cache.utils.CacheKeyBuilder;
import com.alibaba.chaosblade.box.service.model.cluster.UserAppConfig;
import com.alibaba.chaosblade.box.service.model.cluster.UserTopologyConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author: xinyuan.ymm
 * @create: 2021-04-20 10:02 上午
 */
@Service
public class UserTopologyConfigService {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserTopologyConfigService.class);

  private final Cache<String, UserTopologyConfig> userConfigLocalCache =
      CacheBuilder.newBuilder()
          .initialCapacity(100)
          .maximumSize(1000)
          .expireAfterWrite(3, TimeUnit.MINUTES)
          .build();

  /**
   * 查询配置内容
   *
   * @param userId
   * @param environment
   * @return
   */
  public UserTopologyConfig queryConfigContentWithCache(String userId, String environment) {
    if (StringUtils.isBlank(environment)) {
      // 未设置命名空间，采用默认配置
      UserAppConfig defaultConfig = new UserAppConfig();
      defaultConfig.setUseDefaultAppChain(true);
      return new UserTopologyConfig(defaultConfig);
    }

    String cacheKey = CacheKeyBuilder.buildCacheKey("UserConfig", userId, environment);
    UserTopologyConfig userConfigCache = userConfigLocalCache.getIfPresent(cacheKey);
    if (userConfigCache == null) {

      UserTopologyConfig userTopologyConfig = new UserTopologyConfig();
      UserAppConfig userAppConfig = new UserAppConfig();
      userAppConfig.setUseDefaultAppChain(true);
      userTopologyConfig.setUserAppConfig(userAppConfig);
      userConfigLocalCache.put(cacheKey, userTopologyConfig);

      return userTopologyConfig;
    }

    return userConfigCache;
  }
}
