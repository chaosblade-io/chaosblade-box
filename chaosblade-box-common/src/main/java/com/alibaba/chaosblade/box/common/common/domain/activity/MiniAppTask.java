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

package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class MiniAppTask {

  /** 小程序返回值，页面以Pretty JSON String形式展示 */
  private String data;

  /** 开始时间 */
  private Date startTime;

  /** 任务ID */
  protected String taskId;

  /** 任务的运行状态 */
  protected StateEnum state;

  /** 结束时间 */
  protected Date endTime;

  /** 失败原因 */
  protected String errorMessage;

  /** 任务所在的执行机器 */
  protected String hostIp;

  /** 结果标志 */
  protected ResultEnum result;

  protected String expId;

  /** 解决方式 */
  private String solution;

  private Map<String, String> extraInfo = new HashMap<>();
  private String deviceName;

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceName() {
    return deviceName;
  }
}
