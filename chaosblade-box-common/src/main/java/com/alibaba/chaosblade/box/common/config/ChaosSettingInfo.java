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

import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ChaosSettingInfo {

  /** 当前值 */
  private String value;

  private String preValue;

  /** 默认值 */
  private String defaultValue;

  /** 描述信息 */
  private String description;

  /** 名称 */
  private String name;

  /** 默认启用 */
  private boolean enabled = true;

  private String group;

  private String priority;

  private transient OwnerInfo owner;

  /** 不是动态的参数不会更新 */
  private boolean dynamic;

  public boolean valueChanged() {
    return !Objects.equals(value, preValue);
  }

  public void updateOwnerValue() throws IllegalAccessException {
    if (owner == null) {
      return;
    }
    Object object = owner.object;
    Field filed = owner.field;
    if (object == null || filed == null) {
      return;
    }
    Object result = value;
    if (owner.converter != null && value != null) {
      result = owner.converter.convert(value);
    }
    filed.setAccessible(true);
    filed.set(object, result);
  }

  @Data
  public static class OwnerInfo {

    private Object object;

    private Field field;

    private ArgumentTypeConverter converter;
  }
}
