package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用配置项目
 *
 * @author haibin.lhb
 * 
 * 
 */
@TableName("t_chaos_application_configuration")
@Data
public class ApplicationConfigurationDO extends BaseDO {

    public static Integer PRIORITY_HIGH = 0;

    public static Integer PRIORITY_MIDDLE = 1;

    public static Integer PRIORITY_LOW = 2;

    /**
     * alias
     */
    @TableField(value = "alias")
    private String alias;

    /**
     * 显示值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 作用范围
     */
    @TableField(value = "scope")
    private ConfigurationScope scope;

    /**
     * 应用ID
     */
    @TableField(value = "app_id")
    @JSONField(name = "app_id")
    private Long appId;

    /**
     * 是否覆盖用户填写的值
     */
    private Boolean override;

    private String name;

}
