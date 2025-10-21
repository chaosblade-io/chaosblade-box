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
import java.lang.annotation.*;

/** @author haibin.lhb */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChaosSettingDescriptor {

  enum SettingPriority {
    P1("非常重要"),
    P2("重要"),
    P3("一般");

    SettingPriority(String desc) {}
  }

  String name();

  String group();

  /**
   * 描述信息
   *
   * @return
   */
  public String description();

  boolean enabled() default true;

  /**
   * 是否动态
   *
   * @return
   */
  boolean dynamic() default true;

  SettingPriority priority() default SettingPriority.P2;

  Class<? extends ArgumentTypeConverter>[] converters() default {};
}
