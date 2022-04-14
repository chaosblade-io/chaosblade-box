package com.alibaba.chaosblade.box.common.common.enums;

/**
 * APP名称聚合方式
 *
 * @author: xinyuan
 * @create: 2021-04-20 9:50 上午
 */
public enum AppFromType {
    /**
     * k8s标签
     */
    LABEL,
    /**
     * k8s注解，暂时不支持
     */
    ANNOTATION,
    /**
     * 进程命令行
     */
    COMMANDLINE,
    /**
     * 主机安装时基本信息
     */
    HOST_BASE,
}
