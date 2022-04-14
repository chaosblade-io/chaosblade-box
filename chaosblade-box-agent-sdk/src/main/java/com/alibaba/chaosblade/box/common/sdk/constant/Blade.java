package com.alibaba.chaosblade.box.common.sdk.constant;

/**
 * @author Changjun Xiao
 */
public interface Blade {
    /**
     * 混沌实验准备命令
     */
    String PREPARE = "prepare";
    /**
     * 混沌实验创建命令
     */
    String CREATE = "create";
    /**
     * 混沌实验销毁命令
     */
    String DESTROY = "destroy";
    /**
     * 混沌实验撤销命令
     */
    String REVOKE = "revoke";
    /**
     * 混沌实验状态命令
     */
    String STATUS = "status";

    /**
     * 查询混沌实验所需参数
     */
    String QUERY = "query";
    /**
     * 混沌实验 检测 命令
     */
    String CHECK = "check";

    String SPEC_FILE = "chaosblade.spec-cloud.yaml";
}
