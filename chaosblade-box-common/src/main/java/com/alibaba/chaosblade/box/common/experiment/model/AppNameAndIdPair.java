package com.alibaba.chaosblade.box.common.experiment.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author haibin
 *
 *
 */
@Data
public class AppNameAndIdPair implements Serializable {

    @JSONField(name = "app_name")
    private String appName;

    public AppNameAndIdPair(String appName, String appId) {
        this.appName = appName;
        this.appId = appId;
    }

    public AppNameAndIdPair(String appId) {
        this.appId = appId;
    }

    @JSONField(name = "app_id")
    private String appId;

    @JSONField(name = "app_type")
    private Integer appType;

    @JSONField(name = "scope_type")
    private Integer scopeType;

    @JSONField(name = "os_type")
    private Integer osType;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof AppNameAndIdPair)) { return false; }
        AppNameAndIdPair that = (AppNameAndIdPair)o;
        return Objects.equals(getAppName(), that.getAppName()) &&
            Objects.equals(getAppId(), that.getAppId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAppName(), getAppId());
    }
}
