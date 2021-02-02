/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.http.model;

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
