package com.alibaba.chaosblade.box.common.sdk.entity;

import java.util.List;

/**
 * @author Changjun Xiao
 */
public class K8sHitCountBean {
    /**
     * k8s experiment id
     */
    private String uid;
    /**
     * execution success or not
     */
    private boolean success;
    /**
     * error message if failed
     */
    private String error;
    /**
     * count metrics list
     */
    private List<HitCountBean> metrics;

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

    public List<HitCountBean> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<HitCountBean> metrics) {
        this.metrics = metrics;
    }
}
