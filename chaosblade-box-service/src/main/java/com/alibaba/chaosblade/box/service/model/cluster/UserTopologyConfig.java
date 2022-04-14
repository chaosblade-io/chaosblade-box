package com.alibaba.chaosblade.box.service.model.cluster;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTopologyConfig implements Serializable {
    private static final long serialVersionUID = -1;

    private UserAppConfig userAppConfig;

    public UserTopologyConfig() {
    }

    public UserTopologyConfig(UserAppConfig userAppConfig) {
        this.userAppConfig = userAppConfig;
    }
}
