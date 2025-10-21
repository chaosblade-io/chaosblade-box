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

package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/** @author haibin */
public final class CommonConstant {

  public static final String VERTICAL_LINE = "|";

  public static final String UNDER_LINE = "_";

  public static final String COMMA = ",";

  public static String BLANK = "";

  public static final String DOT = ".";

  public static final String CHAOS_BLADE_SCENE_NAME = "chaos_blade";

  public static final String CHAOS_BLADE_SCENE_CODE = "chaos_blade";

  public static final String REJECT_REASON_PRE_ACTIVITY_FAILED = "PRE_ACTIVITY_FAILED";

  public static final String CHAOS_SCENE_OWNER = "1031021604463468";

  public static final String UNKNOWN_USER_ID = "-1";

  public static final String METRIC_FIELD_HOST = "metric_host";

  public static final String METRIC_FIELD_START = "metric_start_time";

  public static final String METRIC_FIELD_END = "metric_end_time";

  public static final String APP_CODE_JAVA_AGENT_UNINSTALL = "chaos.jvm.uninstall";

  public static final String DINGTALK_APP_CODE_ROBOT_MESSAGE =
      "chaosapp.dingtalk.send-robot-message";

  public static final ChaosUser UNKNOWN_USER;

  public static final String METRIC_CHAOSBLADE_EXP_OBJECT = "expObject";

  public static final String METRIC_EXP_ID = "expId";

  static {
    UNKNOWN_USER = new ChaosUser();
    UNKNOWN_USER.setUserId("-1");
  }
}
