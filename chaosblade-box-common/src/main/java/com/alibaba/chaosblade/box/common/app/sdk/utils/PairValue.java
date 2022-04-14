package com.alibaba.chaosblade.box.common.app.sdk.utils;

/**
 * @author haibin
 *
 *
 */
public class PairValue<Left, Right> {

    private Left left;

    private Right right;

    public PairValue(Left left, Right right) {
        this.left = left;
        this.right = right;
    }

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

    public void setRight(Right right) {
        this.right = right;
    }
}
