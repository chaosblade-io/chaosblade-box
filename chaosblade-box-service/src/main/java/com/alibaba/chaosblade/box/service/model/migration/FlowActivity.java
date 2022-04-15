package com.alibaba.chaosblade.box.service.model.migration;

import java.util.Map;

/**
 * @author sunpeng
 * @date 2020/7/14
 * @email wb-sp762090@alibaba-inc.com
 */
public class FlowActivity {

    /**
     * 微流程名称
     */
    private String activityName;

    /**
     * 微流程编码
     */
    private String appCode;

    /**
     * 是否需要手动推进
     */
    private Boolean userCheck;

    /**
     * 微流程配置参数
     */
    private Map<String, String> arguments;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Boolean getUserCheck() {
        return userCheck;
    }

    public void setUserCheck(Boolean userCheck) {
        this.userCheck = userCheck;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, String> arguments) {
        this.arguments = arguments;
    }
}
