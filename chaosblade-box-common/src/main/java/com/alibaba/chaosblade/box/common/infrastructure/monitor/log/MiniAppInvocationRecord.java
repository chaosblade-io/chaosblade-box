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

package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class MiniAppInvocationRecord extends RecordObject {

  /** chaosapp */
  public static Integer MINI_APP_TYPE_CHAOS_APP = 0;

  /** chaosblade */
  public static Integer MINI_APP_TYPE_CHAOSBLADE = 1;

  /** traceId */
  private String traceId;

  private Date create;

  /** 小程序code */
  private String appCode;

  /** 演练任务ID */
  private String experimentTaskId;

  /** 节点任务ID */
  private String activityTaskId;

  /** 节点的任务Id */
  private String miniAppTaskId;

  /** appType类型 */
  private Integer miniAppType;

  /** Ip地址 */
  private String ip;

  /** configurationId */
  private String configurationId;

  /** 是否成功 */
  private boolean success;

  /** 请求 */
  private Object request;

  /** 响应 */
  private Object response;

  private Long cost;
}
