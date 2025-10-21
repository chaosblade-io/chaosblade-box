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

package com.alibaba.chaosblade.box.common.common.constant;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/** @author haibin */
public final class PrivateCloudConstant {

  public static final String PRIVATE_CONFIG_PREFIX = "chaos.private.cloud";

  public static final String AGENT_OPERATION_UPGRADE = "upgrade";

  public static final String PARAM_CONFIGURATION = "cid";

  public static final String PARAM_DEVICE_CONFIGURATION = "did";

  public static final String PARAM_AK = "ak";

  public static final String PARAM_SK = "sk";

  public static final String PARAM_USERID = "uid";

  public static final String PARAM_REQUEST_ID = "rid";

  public static final String PARAM_CHAOSBLADE_VERSION = "cbv";

  public static final String PARAM_CHAOSBLADE_MD5 = "cbvmd5";

  public static final String PARAM_METRIC_MSGS = "msgs";

  public static final String PARAM_CHAOS_RESULT_STATUS = "status";

  public static final String PARAM_CHAOS_RESULT_ERROR_MSG = "error";

  public static final String PARAM_CHAOS_TOOL_TYPE = "ToolType";

  // agent
  public static final String PARAM_AGENT_IP = "ip";

  public static final String PARAM_AGENT_PORT = "port";

  public static final String PARAM_AGENT_PROCESS_ID = "pid";

  public static final String PARAM_VERSION = "v";

  public static String GLOBAL_VPC_ID = "1111";

  public static String DEFAULT_PAY_STATUS = "paid";

  public static String DEFAULT_RAM_STATUS = "applied";

  public static ChaosUser GlobalUser = null;
}
