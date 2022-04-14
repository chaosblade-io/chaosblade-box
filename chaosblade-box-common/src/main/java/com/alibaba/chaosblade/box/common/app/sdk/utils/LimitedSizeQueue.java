package com.alibaba.chaosblade.box.common.app.sdk.utils;

import java.util.LinkedList;

/**
 * @author haibin.lhb
 *
 * 
 */
public class LimitedSizeQueue<E> extends LinkedList<E> {

    private static final long serialVersionUID = 1L;
    private int limit;

    public LimitedSizeQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
