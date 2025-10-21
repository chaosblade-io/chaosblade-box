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

package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.function.Supplier;

/** @author haibin */
public interface Settings {

  public boolean containsKey(String key);

  public Float getAsFloat(String key);

  public Float getAsFloat(String key, Float defaultValue);

  public Integer getAsInteger(String key);

  public Integer getAsInteger(String key, Integer defaultValue);

  public String getAsString(String key, String defaultValue);

  public String getAsString(String key);

  public Boolean getAsBoolean(String key, Boolean defaultValue);

  public <T> T getObject(String key, Class<T> tClass, Supplier<T> supplierIfNotExist);

  public Boolean getAsBoolean(String key);

  public <T> T getObject(String key, Class<T> tClass);

  public void add(String key, Object value);

  public void remove(String key);
}
