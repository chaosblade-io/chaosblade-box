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
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ExtraFeedbackComponent implements Serializable {

  /** 反馈的组件唯一code */
  private String code;
  /** 组件描述 */
  private String name;

  /** 反馈的组件选项 */
  private List<ExtraFeedbackOption> options = new ArrayList<>();

  /** 反馈之后回调的连接 */
  private String redirectUrl;
}
