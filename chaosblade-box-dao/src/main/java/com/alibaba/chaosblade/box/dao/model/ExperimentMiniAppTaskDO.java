package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@TableName(value = "t_chaos_app_execute_result")
@Data
public class ExperimentMiniAppTaskDO extends BaseTaskDO {

    /**
     * 小程序返回值，页面以Pretty JSON String形式展示
     */
    @TableField
    private String data;

    /**
     * 活动节点任务ID
     */
    @TableField(value = "activity_task_id")
    private String activityTaskId;

    /**
     * 小程序code
     */
    @TableField(value = "app_code")
    private String appCode;

    /**
     * node_group info,
     */
    @TableField(value = "nodegroup")
    private String nodegroup;

    /**
     * chaosblade注入ID,只有注入阶段才有
     * {@link ChaosBladeExpUidDO}
     */
    @TableField(value = "chaos_blade_exp_uid")
    private String chaosBladeExpUid;

    /**
     * 设备ID
     *
     */
    @TableField(value = "device_id")
    private String deviceId;

    /**
     * 宿主机的配置ID
     * 1.如果注入对象是主机,那么就是主机ConfigurationId
     * 2.如果注入对象是K8S的Container,Pods，那么就是所在Node的ConfigurationId
     *
     */
    @TableField(value = "device_configuration_id")
    private String deviceConfigurationId;

    /**
     * 应用节点ID
     * 1.如果注入对象是主机,那么就是主机ConfigurationId
     * 2.如果注入对象是K8S的Container,Pods，那么就是所在Pod的ConfigurationId
     */
    @TableField(value = "app_configuration_id")
    private String appConfigurationId;

    /**
     * 设备名字
     */
    @TableField(value = "device_name")
    private String deviceName;

    @TableField(value = "experiment_task_id")
    private String experimentTaskId;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 如果是裂变小程序，executableAppCode为父code,否则就等于小程序的code
     */
    @TableField(value = "executable_app_code")
    private String executableAppCode;

    /**
     * 当前阶段
     */
    @TableField(value = "phase")
    private PhaseType phase;

    @TableField("error_code")
    private String errorCode;

    /**
     * agent返回的直接code
     */
    @TableField("agent_code")
    private Integer agentCode;

    /**
     * 是否是异步执行
     */
    @TableField("async")
    private Boolean async;

    /**
     * 原始请求
     */
    private String originRequest;

    /**
     * 原始响应
     */
    private String originResponse;
    /**
     * 主账号
     */
    @TableField("user_id")
    private String userId;

    public String getExecutableAppCode() {
        return executableAppCode == null ? appCode : executableAppCode;
    }

    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getValue().equals(result);
    }
}
