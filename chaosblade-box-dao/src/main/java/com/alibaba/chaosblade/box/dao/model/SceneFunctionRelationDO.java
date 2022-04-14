package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author jiumu
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@TableName("t_chaos_scene_function_relation")
public class SceneFunctionRelationDO extends BaseDO {

    /**
     * 唯一标志关系ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    String relationId;

    /**
     * parent function id
     */
    String outFunctionId;

    /**
     * child function id
     */
    String functionId;

}
