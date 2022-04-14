package com.alibaba.chaosblade.box.common.sdk;

/**
 * @author haibin.lhb
 *
 *
 */
public interface RequestIdProvider {

    /**
     * 生成requestId
     * @return
     */
    public String provide();
}
