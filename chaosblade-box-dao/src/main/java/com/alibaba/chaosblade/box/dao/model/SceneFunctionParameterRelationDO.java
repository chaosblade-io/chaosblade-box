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
@TableName("t_chaos_function_parameter_relation")
public class SceneFunctionParameterRelationDO extends BaseDO {

    /**
     * 唯一标志关系ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    String relationId;

    /**
     * 指向归属的小程序ID
     */
    String functionId;

    /**
     * 指向父级参数ID
     */
    String outParameterId;

    /**
     * 指向归属的参数ID
     */
    String parameterId;

}
