package com.alibaba.chaosblade.box.common.sdk.entity;

import com.alibaba.chaosblade.box.common.sdk.util.CliUtil;

import java.util.List;

/**
 * @author Changjun Xiao
 */
public class K8sResultBean {

    private String uid;
    private boolean success;
    private String error;
    private List<K8sExpStatusBean> statuses;

    public K8sResultBean() {
    }

    public K8sResultBean(String uid,
                         List<K8sExpStatusBean> statuses) {
        this.uid = uid;
        this.statuses = statuses;
    }

    public K8sResultBean(String uid, boolean success, String error,
                         List<K8sExpStatusBean> statuses) {
        this.uid = uid;
        this.success = success;
        this.error = error;
        this.statuses = statuses;
    }

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

    public List<K8sExpStatusBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<K8sExpStatusBean> statuses) {
        this.statuses = statuses;
    }

    /**
     * 判断实验是否完成
     * 如果是销毁，则判断总的状态，如果是失败，就一直返回失败，直到上层超时
     *
     * @param cmd {@link CliUtil#DESTROY} | {@link CliUtil#CREATE}
     * @return
     */
    public boolean completed(String cmd) {
        if (CliUtil.DESTROY.equalsIgnoreCase(cmd)) {
            return success;
        }
        return statuses != null && statuses.size() > 0;
    }

    public static class K8sExpStatusBean {
        private String id;
        private String name;
        private String uid;
        private String state;
        private String kind;
        private boolean success;
        private String error;

        public K8sExpStatusBean() {
        }

        public K8sExpStatusBean(String id, String name, String uid, String state, String kind, boolean success,
                                String error) {
            this.id = id;
            this.name = name;
            this.uid = uid;
            this.state = state;
            this.kind = kind;
            this.success = success;
            this.error = error;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }
    }
}
