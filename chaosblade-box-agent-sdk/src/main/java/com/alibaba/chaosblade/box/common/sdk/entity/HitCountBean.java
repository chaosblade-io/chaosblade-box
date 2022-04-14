package com.alibaba.chaosblade.box.common.sdk.entity;

/**
 * @author Changjun Xiao
 */
public class HitCountBean {
    /**
     * experiment id in container, different with k8s experiment id contains many container experiment id
     */
    private String id;
    /**
     * jvm or others
     */
    private String type;
    /**
     * matched count
     */
    private Long count;
    private boolean success;
    private String error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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
}
