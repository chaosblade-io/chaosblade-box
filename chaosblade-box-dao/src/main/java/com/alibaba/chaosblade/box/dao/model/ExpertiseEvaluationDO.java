package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 * 
 *
 */
@Data
@TableName(value = "t_chaos_expertise_evaluation")
public class ExpertiseEvaluationDO extends BaseDO {

    @TableId(value = "evaluation_id", type = IdType.ID_WORKER_STR)
    private String evaluationId;

    @TableField(value = "expertise_id")
    private String expertiseId;

    @TableField(value = "description")
    private String description;

}
