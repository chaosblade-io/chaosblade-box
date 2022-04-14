package com.alibaba.chaosblade.box.common.infrastructure.util;

/**
 * @author haibin
 * 
 *
 */
public interface Adpater<F, T> {

    T adapt(F s);
}
