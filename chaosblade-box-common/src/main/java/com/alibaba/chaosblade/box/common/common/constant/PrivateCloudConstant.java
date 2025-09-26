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
