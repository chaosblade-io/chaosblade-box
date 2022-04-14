package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class MiniAppInvocationRecord extends RecordObject {

    /**
     * chaosapp
     */
    public static Integer MINI_APP_TYPE_CHAOS_APP = 0;

    /**
     * chaosblade
     */
    public static Integer MINI_APP_TYPE_CHAOSBLADE = 1;

    /**
     * traceId
     */
    private String traceId;


    private Date create;

    /**
     * 小程序code
     */
    private String appCode;

    /**
     * 演练任务ID
     */
    private String experimentTaskId;

    /**
     * 节点任务ID
     */
    private String activityTaskId;

    /**
     * 节点的任务Id
     */
    private String miniAppTaskId;

    /**
     * appType类型
     */
    private Integer miniAppType;

    /**
     * Ip地址
     */
    private String ip;

    /**
     * configurationId
     */
    private String configurationId;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 请求
     */
    private Object request;

    /**
     * 响应
     */
    private Object response;

    private Long cost;

}
