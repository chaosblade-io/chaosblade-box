package com.alibaba.chaosblade.box.common.app.sdk;

/**
 * @author haibin.lhb
 *
 * 
 */
public enum SupportScope {


    HOST(0),

    K8S(2);

    public int getType() {
        return type;
    }

    int type;

    SupportScope(int i) {
        this.type = i;
    }
}
