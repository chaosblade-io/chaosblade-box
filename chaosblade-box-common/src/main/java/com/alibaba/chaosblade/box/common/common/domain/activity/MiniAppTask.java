package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Data
public class MiniAppTask {

    /**
     * 小程序返回值，页面以Pretty JSON String形式展示
     */
    private String data;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 任务ID
     */
    protected String taskId;

    /**
     * 任务的运行状态
     */
    protected StateEnum state;

    /**
     * 结束时间
     */
    protected Date endTime;

    /**
     * 失败原因
     */
    protected String errorMessage;

    /**
     * 任务所在的执行机器
     */
    protected String hostIp;

    /**
     * 结果标志
     */
    protected ResultEnum result;

    protected String expId;

    /**
     * 解决方式
     */
    private String solution;

    private Map<String, String> extraInfo = new HashMap<>();
    private String deviceName;

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }
}
