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

package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.chaosblade.box.common.common.util.KVPair;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * <p>Date: 2019/1/21
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SceneFunctionParameterComponent {

  public static String TYPE_INPUT = "input";
  public static String TYPE_RADIO = "radio";
  public static String TYPE_SEARCH = "search";
  public static String TYPE_SELECT = "select";
  public static String TYPE_CODE = "code";
  public static String TYPE_PASSWORD = "password";

  public static String TYPE_SELECT_REMOTE = "select_remote";

  /** 没有操作限制 */
  public static Integer NO_LIMITATION_OPERATION_LEVEL = 0;

  /** 没有权限操作 */
  public static Integer LIMITATION_OPERATION_LEVEL = -1;

  /** radio,input,search,select */
  String type;

  boolean required;

  /** for all types. default value is [KEY] if type is radio or select. */
  String defaultValue;

  /** the symbol of unit for input value. */
  String unit;

  /** for radio or select type. KEY is inner unique value.VALUE is display text for user. */
  List<KVPair<String, String>> options;

  /** for search type only. */
  String requestUrl;

  SceneFunctionParameterLinkage linkage;

  SceneFunctionParameterConstraint constraint;

  /** 操作等级, 0代表能操作,1代表不可操作 */
  int opLevel = 0;

  /** 密码的话只是存，读出来的时候就不要了 */
  @JSONField(deserialize = false)
  String plainText;

  String cipherText;
}
