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

package com.alibaba.chaosblade.box.service.infrastructure.configuration;

import com.alibaba.chaosblade.box.common.common.util.KVPair;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ConfigurationComponent {

  public static String TYPE_INPUT = "input";
  public static String TYPE_RADIO = "radio";
  public static String TYPE_SEARCH = "search";
  public static String TYPE_SELECT = "select";
  public static String TYPE_PASSWORD = "password";

  /** radio,input,search,select */
  String type;

  /** 是否必须 */
  boolean required;

  /** for all types. default value is [KEY] if type is radio or select. */
  String defaultValue;

  /** for radio or select type. KEY is inner unique value.VALUE is display text for user. */
  List<KVPair<String, String>> options;

  /** for search type only. */
  String requestUrl;
}
