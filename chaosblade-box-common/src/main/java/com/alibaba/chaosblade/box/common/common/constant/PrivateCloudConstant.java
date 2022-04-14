package com.alibaba.chaosblade.box.common.common.constant;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/**
 * @author haibin
 *
 *
 */
public final class PrivateCloudConstant {

    public final static String PRIVATE_CONFIG_PREFIX = "chaos.private.cloud";

    public final static String AGENT_OPERATION_UPGRADE = "upgrade";

    public final static String PARAM_CONFIGURATION = "cid";

    public final static String PARAM_DEVICE_CONFIGURATION = "did";

    public final static String PARAM_AK = "ak";

    public final static String PARAM_SK = "sk";

    public final static String PARAM_USERID = "uid";

    public final static String PARAM_REQUEST_ID = "rid";

    public final static String PARAM_CHAOSBLADE_VERSION = "cbv";

    public final static String PARAM_CHAOSBLADE_MD5 = "cbvmd5";

    public final static String PARAM_METRIC_MSGS = "msgs";

    public final static String PARAM_CHAOS_RESULT_STATUS = "status";

    public final static String PARAM_CHAOS_RESULT_ERROR_MSG = "error";

    public final static String PARAM_CHAOS_TOOL_TYPE = "ToolType";

    // agent
    public final static String PARAM_AGENT_IP = "ip";

    public final static String PARAM_AGENT_PORT = "port";

    public final static String PARAM_AGENT_PROCESS_ID = "pid";

    public final static String PARAM_VERSION = "v";

    public static String GLOBAL_VPC_ID = "1111";

    public static String DEFAULT_PAY_STATUS = "paid";

    public static String DEFAULT_RAM_STATUS = "applied";

    public static ChaosUser GlobalUser = null;

}
