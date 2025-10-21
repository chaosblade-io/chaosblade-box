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

public interface ChaosLocalCacheTemplate extends ChaosCacheTemplate {

  void put(String key, Serializable value) throws ChaosCacheException;

  void putAll(Map<String, Serializable> values) throws ChaosCacheException;

  Serializable get(String key) throws ChaosCacheException;

  Map<String, Serializable> getAll(Collection<String> keys) throws ChaosCacheException;

  void invalid(String key) throws ChaosCacheException;

  void invalidAll(Collection<String> keys) throws ChaosCacheException;

  void clear(String prefixKey) throws ChaosCacheException;
}
