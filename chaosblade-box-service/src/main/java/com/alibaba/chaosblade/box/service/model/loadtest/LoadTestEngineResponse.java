package com.alibaba.chaosblade.box.service.model.loadtest;

import lombok.Data;

/**
 * 压测引擎响应包装类
 *
 * @author ZhengMingZhuo
 */
@Data
public class LoadTestEngineResponse<T> {

    /**
     * 响应数据
     */
    private T data;

    /**
     * 错误码（可选）
     */
    private String code;

    /**
     * 错误消息（可选）
     */
    private String message;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 检查响应是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success != null && success && data != null;
    }
}
