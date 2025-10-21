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

package com.alibaba.chaosblade.box.cache.templates;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ChaosDistributedCacheTemplate extends ChaosCacheTemplate {

  void prefixPut(String prefixKey, String key, Serializable value) throws ChaosCacheException;

  /**
   * 设置key失效时间 单位秒
   *
   * @param prefixKey
   * @param key
   * @param value
   * @param expire
   * @throws ChaosCacheException
   */
  void prefixPut(String prefixKey, String key, Serializable value, int expire)
      throws ChaosCacheException;

  void prefixPutAll(String prefixKey, Map<String, Serializable> values) throws ChaosCacheException;

  Serializable prefixGet(String prefixKey, String key) throws ChaosCacheException;

  Map<String, Serializable> prefixGetAll(String prefixKey, Collection<String> keys)
      throws ChaosCacheException;

  void prefixInvalid(String prefixKey, String key) throws ChaosCacheException;

  void prefixInvalidAll(String prefixKey, Collection<String> keys) throws ChaosCacheException;
}
