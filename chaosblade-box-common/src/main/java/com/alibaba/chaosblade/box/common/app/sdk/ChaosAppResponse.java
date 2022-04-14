package com.alibaba.chaosblade.box.common.app.sdk;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.common.domain.IErrorCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * chaos app小程序返回值
 *
 * @author sunju
 */
@Data
public class ChaosAppResponse {

    boolean success = false;

    /**
     * 小程序返回值，页面以Pretty JSON String形式展示
     */

    Map<String, Object> data;

    String errorMessage;

    String errorCode;

    Scope scope;

    String originErrorMessage;


    public void withErrorCode(IErrorCode iErrorCode) {
        this.success = false;
        this.errorCode = iErrorCode.name();
        this.errorMessage = iErrorCode.getReadableMessage();
    }

    public ChaosAppResponse(boolean success, Map<String, Object> data, String errorMessage, Scope scope) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.scope = scope;
    }

    public ChaosAppResponse(boolean success, Map<String, Object> data, Scope scope) {
        this.success = success;
        this.data = data;
        this.scope = scope;
    }

    public void addResponseData(String name, Object value) {
        if (null == data) {
            data = new HashMap<>(1);
        }
        data.put(name, value);
    }

    public ChaosAppResponse() {}

    public ChaosAppResponse(boolean success) {
        this.success = success;
    }
}
