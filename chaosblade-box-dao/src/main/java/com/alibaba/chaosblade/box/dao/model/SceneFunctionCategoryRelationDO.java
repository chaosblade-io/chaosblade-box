package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin.lhb
 *
 *
 */
@TableName("t_chaos_scene_function_category_relation")
@Data
public class SceneFunctionCategoryRelationDO extends BaseDO {

    @TableField(value = "category_id")
    private String categoryId;

    @TableField(value = "code")
    private String code;

    @TableField(value = "function_id")
    private String functionId;
}
