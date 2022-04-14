package com.alibaba.chaosblade.box.common.infrastructure.constant;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

/**
 * @author haibin
 *
 * 
 */
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

    public static final String DINGTALK_APP_CODE_ROBOT_MESSAGE = "chaosapp.dingtalk.send-robot-message";

    public static final ChaosUser UNKNOWN_USER;

    public static final String METRIC_CHAOSBLADE_EXP_OBJECT = "expObject";

    public static final String METRIC_EXP_ID = "expId";

    static {
        UNKNOWN_USER = new ChaosUser();
        UNKNOWN_USER.setUserId("-1");
    }

}
