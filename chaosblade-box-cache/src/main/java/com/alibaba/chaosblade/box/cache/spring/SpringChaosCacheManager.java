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

package com.alibaba.chaosblade.box.cache.spring;

import com.alibaba.chaosblade.box.cache.ChaosCacheManager;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/8
 */
public class SpringChaosCacheManager extends AbstractCacheManager {

  @SuppressWarnings({"SpringJavaAutowiredMembersInspection"})
  @Autowired
  private ChaosCacheManager cacheManager;

  @Override
  protected Collection<? extends Cache> loadCaches() {
    return Collections.emptyList();
  }

  @Override
  protected Cache getMissingCache(String name) {
    return new SpringChaosCache(name, this.cacheManager);
  }
}
