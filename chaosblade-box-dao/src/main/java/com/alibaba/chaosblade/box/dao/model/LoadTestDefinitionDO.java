package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 压测定义实体类
 *
 * @author ZhengMingZhuo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_chaos_load_test_definition")
public class LoadTestDefinitionDO extends BaseDO {

    /**
     * 压测定义ID
     */
    @TableField(value = "definition_id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String definitionId;

    /**
     * 压测定义名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 引擎类型：JMETER、K6、LOCUST、CUSTOM
     */
    @TableField(value = "engine_type")
    private String engineType;

    /**
     * 目标端点
     */
    @TableField(value = "endpoint")
    private String endpoint;

    /**
     * 入口类型：JMX、SCRIPT、URL
     */
    @TableField(value = "entry")
    private String entry;

    /**
     * 文件URL引用
     */
    @TableField(value = "content_ref")
    private String contentRef;

    /**
     * URL型用例配置（JSON格式）
     */
    @TableField(value = "url_case")
    private String urlCase;

    /**
     * 创建者
     */
    @TableField(value = "created_by")
    private String createdBy;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 命名空间
     */
    @TableField(value = "namespace")
    private String namespace;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
}
