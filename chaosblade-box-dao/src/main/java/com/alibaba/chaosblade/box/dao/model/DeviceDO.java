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

package com.alibaba.chaosblade.box.dao.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunju */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_device")
public class DeviceDO extends com.alibaba.chaosblade.box.dao.model.base.DeviceDO {

  public static int STATUS_ENABLED = 2;

  /** */
  Boolean experimentStatus;

  /** 最近一次演练时间 */
  Date lastExperimentTime;

  private String deviceRole;

  public boolean isAlive() {
    return STATUS_ENABLED == getStatus();
  }
}
