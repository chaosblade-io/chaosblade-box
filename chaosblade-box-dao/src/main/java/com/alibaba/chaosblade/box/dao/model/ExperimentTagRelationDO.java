package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 演练和标签的绑定关系
 *
 * @author haibin
 *
 *
 */
@Data
@TableName(value = "t_chaos_experiment_tag")
public class ExperimentTagRelationDO extends BaseDO {

    @TableField(value = "tag_id")
    private String tagId;

    @TableField(value = "experiment_id")
    private String relationId;

    @TableField(value = "type")
    private Integer tagType;

    @TableField(value = "tag_name")
    private String tagName;

    @TableField(value = "user_id")
    private String userId;

}
