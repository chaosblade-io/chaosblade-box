package com.alibaba.chaosblade.box.common.app.sdk.utils;

/**
 * @author haibin
 *
 * 
 */

public class BooleanPairValue extends PairValue<Boolean, String> {

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int code;

    public long getRt() {
        return rt;
    }

    public void setRt(long rt) {
        this.rt = rt;
    }

    private long rt;

    public BooleanPairValue(Boolean aBoolean, String s) {
        super(aBoolean, s);
    }
}
