package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@TableName(value = "t_chaos_expertise")
@Data
public class ExpertiseDO extends BaseDO {

    @TableId(value = "expertise_id", type = IdType.ID_WORKER)
    private String expertiseId;

    @TableField(value = "name", strategy = FieldStrategy.NOT_EMPTY)
    private String name;

    @TableField(value = "function_desc", strategy = FieldStrategy.NOT_EMPTY)
    private String functionDesc;

    @TableField(value = "background_desc", strategy = FieldStrategy.NOT_EMPTY)
    private String backgroundDesc;

    @TableField(value = "design_concept", strategy = FieldStrategy.NOT_EMPTY)
    private String designConcept;

    /**
     * 状态
     *
     * @see {@link ExpertiseConstant}
     */
    private Integer state;

    /**
     * 关联的演练id
     */
    @TableField(value = "experiment_Id")
    private String experimentId;

    @TableField(value = "run_time")
    private ExpertiseRunTimeInfo runTime;

    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "namespace")
    private String namespace;


    /**
     * 经验库类型
     *
     * @see {@link ExpertiseConstant}
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 支持的应用类型
     */
    @TableField(value = "scope_type")
    private String scopeType;

}
