package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author sunpeng
 * 
 *
 */
@Data
@TableName(value = "t_chaos_application_device_tag")
public class ApplicationDeviceTagDO extends BaseDO {

    /**
     * 标签ID
     */
    @TableField(value = "tag_id")
    private String tagId;

    /**
     * 机器ip
     */
    @TableField(value = "configuration_id")
    private String configurationId;

    /**
     * 标签名
     */
    @TableField(value = "tag_name")
    private String tagName;

    /**
     * 应用ID
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 应用分组
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * userId
     */
    @TableField(value = "user_id")
    private String userId;


}
