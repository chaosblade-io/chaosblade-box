package com.alibaba.chaosblade.box.common.sdk.entity;

/**
 * @author Changjun Xiao
 */
public interface BladeStatus {
    /**
     * 执行错误
     */
    String ERROR = "Error";
    /**
     * 正在挂载中
     */
    String RUNNING = "Running";
    /**
     * 已撤销（已卸载）
     */
    String REVOKED = "Revoked";
    /**
     * 实验执行成功
     */
    String SUCCESS = "Success";
    /**
     * 实验已销毁
     */
    String DESTROYED = "Destroyed";

}
