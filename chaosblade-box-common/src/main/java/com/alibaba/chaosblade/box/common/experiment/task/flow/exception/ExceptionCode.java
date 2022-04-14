package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/**
 * @author haibin
 *
 *
 */
public enum ExceptionCode {

    /**
     * 创建演练失败
     */
    CREATE_EXPERIMENT_FAILED,
    /**
     * 演练解析失败
     */
    EXPERIMENT_DEFINITION_PARSER_FAILED,
    /**
     * 演练活动执行失败
     */
    ACTIVITY_EXECUTE_FAILED,
    /**
     * 插件调用失败
     */
    PLUGIN_EXECUTE_FAILED,
    /**
     * 无效的演练
     */
    INVALID_EXPERIMENT,
    /**
     * 创建演练活动失败
     */
    CREATE_ACTIVITY_FAILED,
    /**
     * 无效的活动
     */
    INVALID_ACTIVITY,
    ACTION_NOT_ALLOW

}
