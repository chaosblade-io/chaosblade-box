package com.alibaba.chaosblade.box.common.sdk.entity;

import com.alibaba.chaosblade.box.common.sdk.util.StringUtil;

/**
 * @author Changjun Xiao
 */
public class AliCdnResultBean {
    private String uid;
    private boolean success;
    private String error;
    private String[] output;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String[] getOutput() {
        return output;
    }

    public void setOutput(String[] output) {
        this.output = output;
    }

    public boolean completed(String cmd) {
        return !StringUtil.isBlank(uid);
    }
}
