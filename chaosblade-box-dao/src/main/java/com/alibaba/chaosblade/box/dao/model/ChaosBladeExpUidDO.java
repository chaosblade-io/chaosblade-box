package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

/**
 * @author haibin
 * 
 *
 */
@TableName(value = "t_chaos_blade_exp_uid")
@Data
public class ChaosBladeExpUidDO extends BaseDO {

    public static String ATTRIBUTE_PID = "pid";

    public static String ATTRIBUTE_PORT = "port";

    public static String ATTRIBUTE_PROCESS_NAME = "processName";

    public static String ATTRIBUTE_ACTIVITY_NAME = "activity_name";

    public static String ATTRIBUTE_DEVICE_CONFIGURATION_ID = "configuration_id";

    public static String ATTRIBUTE_DEVICE_IP = "device_ip";

    private String host;

    @TableField(value = "app_code")
    private String appCode;

    @TableField(value = "experiment_task_id")
    private String experimentTaskId;

    @TableField(value = "exp_uid")
    private String expUid;

    @TableField(value = "app_execution_id")
    private String appExecutionId;

    @TableField(value = "activity_task_id")
    private String activityTaskId;

    @TableField(value = "expired")
    private Boolean expired;

    @TableField(value = "expired_time")
    private Date expiredTime;

    @TableField(value = "configuration_id")
    private String configurationId;

    /**
     * 容器场景会有，chaos blade 内层expId
     */
    @TableField(value = "sub_exp_uid")
    private String subExpUid;

    /**
     * 请求的一些信息
     */
    @TableField(value = "attributes")
    private HashMap<String, String> attributes = new HashMap<>();

    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    public void deleteAttribute(String key) {
        if (attributes == null) {return;}
        attributes.remove(key);
    }

    public String getAttribute(String key) {
        if (attributes == null) {return null;}
        return attributes.get(key);
    }

    public String getDeviceConfigurationId() {
        if (getAttributes().containsKey(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_CONFIGURATION_ID)) {
            return getAttribute(ATTRIBUTE_DEVICE_CONFIGURATION_ID);
        } else {
            return getConfigurationId();
        }
    }

    public String getTargetIp() {
        if (getAttributes().containsKey(ChaosBladeExpUidDO.ATTRIBUTE_DEVICE_IP)) {
            return getAttribute(ATTRIBUTE_DEVICE_IP);
        } else {
            return getHost();
        }
    }
}
