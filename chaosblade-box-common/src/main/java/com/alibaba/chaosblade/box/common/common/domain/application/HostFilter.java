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

package com.alibaba.chaosblade.box.common.common.domain.application;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class HostFilter {

  /** 全部返回 */
  public static String TYPE_ALL = "all";

  /** 任意返回一台 */
  public static String TYPE_ANY = "any";

  /** 按百分比返回 */
  public static String TYPE_PERCENT = "percent";

  /** 暂时没用 */
  public static String TYPE_SPECIFIED = "specified";

  /** 过滤的类型，必须要填 */
  private String type;

  /** 需要返回的百分比，请保证type是:TYPE_PERCENT */
  private Integer percent;

  /** 用户指定的IP地址列表，需要过滤掉不合法的IP */
  private List<String> hosts;
}
