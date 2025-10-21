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

package com.alibaba.chaosblade.box.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @author haibin */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {

  enum ApiOperation {
    /** 新建 */
    CREATE,
    /** 编辑，包括更新，删除 */
    EDIT,

    /** 任意操作都需要 */
    ALL,

    /** 随意 */
    ANY
  }

  /**
   * 当前变量非必须
   *
   * @return
   */
  boolean required() default true;

  /**
   * 表明当前字段在什么时候才需要
   *
   * @return
   */
  ApiOperation operation() default ApiOperation.ALL;
}
