package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object for change log.
 *
 * Three roles for a change of blow:
 *
 * Target - Entity which changed. e.g experiment, workspace, etc.
 * Operator - Operator who change the target. e.g user, system, etc.
 * Property - Changed property of target. e.g owner of experiment, member of workspace, etc.
 *
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_changelog")
public class ChangelogDO extends BaseDO {

    @TableField("changelog_id")
    @TableId(type = IdType.ID_WORKER_STR)
    String changelogId;

    /**
     * @see ChangelogTypes
     */
    @TableField("change_type")
    String changeType;

    /**
     * 类型描述
     */
    @TableField("type_description")
    String typeDescription;

    @TableField("target_id")
    String targetId;

    @TableField("target_description")
    String targetDescription;

    /**
     * @see ChangelogTypes
     */
    @TableField("target_type")
    String targetType;

    @TableField("operator_id")
    String operatorId;

    @TableField("operator_description")
    String operatorDescription;

    /**
     * @see ChangelogTypes
     */
    @TableField("operator_type")
    String operatorType;

    @TableField("property_id")
    String propertyId;

    @TableField("property_description")
    String propertyDescription;

    /**
     * @see ChangelogTypes
     */
    @TableField("property_type")
    String propertyType;

    /**
     * @see ChangelogTypes
     */
    @TableField("property_change_type")
    String propertyChangeType;

    @TableField("change_description")
    String changeDescription;

    /**
     * 错误码
     */
    @TableField("error_code")
    String errorCode;



}
