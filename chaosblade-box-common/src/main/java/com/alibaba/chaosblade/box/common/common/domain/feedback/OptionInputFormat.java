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

package com.alibaba.chaosblade.box.common.common.domain.feedback;

import com.alibaba.chaosblade.box.common.common.util.KVPair;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class OptionInputFormat implements Serializable {

  /** 输入框 */
  public static String TYPE_INPUT = "input";
  /** radio */
  public static String TYPE_RADIO = "radio";

  /** 下拉选择 */
  public static String TYPE_SELECT = "select";

  /** radio,input,search,select */
  String type;

  /** 选项输出,key是唯一标记,value是展示给用户的值 */
  List<KVPair<String, String>> options;

  /** 默认值 */
  String defaultValue;

  /** 如果是字符串的话最大长度 */
  private Integer inputSize;

  /** 是否必须 */
  private boolean required;

  /** 是否可写 */
  private boolean writable;
}
