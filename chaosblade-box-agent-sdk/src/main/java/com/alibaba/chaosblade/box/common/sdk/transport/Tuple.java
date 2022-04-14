package com.alibaba.chaosblade.box.common.sdk.transport;

/**
 * @author changjun.xcj
 */
public class Tuple<L, R> {

    private L l;
    private R r;

    public Tuple(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getL() {
        return l;
    }

    public void setL(L l) {
        this.l = l;
    }

    public R getR() {
        return r;
    }

    public void setR(R r) {
        this.r = r;
    }
}
