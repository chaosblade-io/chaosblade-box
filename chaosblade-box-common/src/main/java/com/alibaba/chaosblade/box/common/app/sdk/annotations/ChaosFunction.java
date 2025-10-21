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

package com.alibaba.chaosblade.box.common.app.sdk.annotations;

import com.alibaba.chaosblade.box.common.app.sdk.InvokeMode;
import com.alibaba.chaosblade.box.common.app.sdk.SupportScope;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import java.lang.annotation.*;

/** @author sunju */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ChaosFunction {

  String code();

  String name();

  String description() default "";

  boolean agentRequired() default false;

  InvokeMode mode() default InvokeMode.EACH;

  /**
   * 小程序的前后依赖
   *
   * @return
   */
  ChaosDependency[] dependencies() default {};

  /**
   * 支持的阶段
   *
   * @return
   */
  PhaseType[] phases() default {};

  String[] categories() default {};

  SupportScope[] scopes() default {};
}
