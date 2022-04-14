package com.alibaba.chaosblade.box.common.app.sdk.scope;

/**
 * @author sunju
 */

public class Host extends Scope {

    public Host() {
        super("host", null, -1);
    }

    public Host(String ip, int port) {
        super("host", ip, port);
    }

    private boolean invalid;

    public boolean isInvalid() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }
}
