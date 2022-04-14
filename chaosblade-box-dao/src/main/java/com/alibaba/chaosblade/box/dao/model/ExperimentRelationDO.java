package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_experiment_relation")
public class ExperimentRelationDO extends BaseDO {

    public static String RELATION_TYPE_EXPERTISE = "expertise";

    /**
     * 关系ID
     */
    @TableField("relation_id")
    @TableId(type = IdType.ID_WORKER_STR)
    String relationId;

    /**
     * 实验ID
     */
    @TableField("experiment_id")
    String experimentId;

    /**
     * 关联的外部ID
     */
    @TableField("outer_id")
    String outerId;

    /**
     * 关联的外部描述信息
     */
    @TableField("outer_description")
    String outerDescription;

    /**
     * 关联的外部数据类型
     */
    @TableField("relation_type")
    String relationType;

    /**
     * 是否删除
     */
    @TableField("is_delete")
    Boolean isDelete;

}
