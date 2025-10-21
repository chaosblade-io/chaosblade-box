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

import java.io.Serializable;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ExtraFeedbackOption implements Serializable {

  /** 每一个选项输入的格式 */
  private OptionInputFormat format;

  /** 选项的唯一key */
  private String key;

  /** 选项的描述信息 */
  private String description;

  /** 选项的值,如果没有填过为空 */
  private String value;
}
