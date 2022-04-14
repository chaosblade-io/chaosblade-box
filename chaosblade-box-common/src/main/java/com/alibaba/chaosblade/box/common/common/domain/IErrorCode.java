package com.alibaba.chaosblade.box.common.common.domain;

/**
 * @author haibin
 *
 *
 */
public interface IErrorCode {

    /**
     * 状态码
     *
     * @return
     */
    public Integer status();

    /**
     * codeName
     *
     * @return
     */
    public String name();

    public String getReadableMessage();

    public boolean logWhenThrowable();

}
