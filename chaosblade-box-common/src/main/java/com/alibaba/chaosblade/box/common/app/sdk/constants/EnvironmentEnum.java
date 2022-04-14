package com.alibaba.chaosblade.box.common.app.sdk.constants;

import lombok.Getter;

/**
 * 环境
 *
 * @author sunju
 */
public enum EnvironmentEnum {

    /**
     * 日常环境
     */
    DAILY(0),
    /**
     * 预发环境
     */
    PREPUB(0),
    /**
     * 生产环境
     */
    ONLINE(0);

    @Getter
    int flag;

    EnvironmentEnum(int flag) {
        this.flag = flag;
    }


    public boolean isDaily() {
        return DAILY.equals(this);
    }

    public static EnvironmentEnum of(String env) {
        if (null == env || env.isEmpty()) {
            return null;
        }
        for (EnvironmentEnum environment : EnvironmentEnum.values()) {
            if (environment.name().equals(env.toUpperCase())) {
                return environment;
            }
        }
        return null;
    }
}
