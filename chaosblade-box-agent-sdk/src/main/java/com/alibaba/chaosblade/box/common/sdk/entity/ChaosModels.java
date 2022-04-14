package com.alibaba.chaosblade.box.common.sdk.entity;

import java.util.List;

/**
 * @author Changjun Xiao
 */
public class ChaosModels {
    private String version;
    private String type;
    private String kind;
    private List<ModelSpecBean> items;

    public ChaosModels() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<ModelSpecBean> getItems() {
        return items;
    }

    public void setItems(List<ModelSpecBean> items) {
        this.items = items;
    }
}
