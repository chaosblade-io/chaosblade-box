package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin.lhb
 *
 *
 */
@TableName(value = "t_chaos_experiment_task_feedback")
@Data
public class ExperimentTaskFeedbackDO extends BaseDO {

    @TableId(value = "feedback_id", type = IdType.ID_WORKER)
    private String feedbackId;

    /**
     * 任务ID
     */
    @TableField(value = "experiment_task_id")
    private String experimentTaskId;

    /**
     * 主账号
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     *
     */
    @TableField(value = "experiment_id")
    private String experimentId;

    /**
     * 用户备注
     */
    private String memo;

    /**
     * 是否符合预期
     */
    @TableField(value = "expectation_status")
    private Integer expectationStatus;

    /**
     * 业务影响程度
     */
    @TableField(value = "business_status")
    private Integer businessStatus;

    /**
     * 反馈时间
     */
    @TableField(value = "feedback_time")
    private Date feedbackTime;

    @TableId(value = "extra_component")
    private ExtraFeedbackComponent extraComponent;

}
