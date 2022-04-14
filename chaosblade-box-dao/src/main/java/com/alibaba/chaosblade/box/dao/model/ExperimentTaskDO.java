package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
@TableName(value = "t_chaos_experiment_task")
public class ExperimentTaskDO extends BaseTaskDO {

    /**
     * 任务对应的演练ID
     */
    @TableField(value = "experiment_id")
    private String experimentId;

    /**
     * 当前步骤的任务ID
     */
    @TableField(value = "activity_task_id")
    private String activityTaskId;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 演练任务的人员
     */
    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "outer_id")
    private String outerId;

    @TableField(value = "namespace")
    private String namespace;

    @TableField(value = "experiment_task_context")
    private ExperimentTaskContext experimentTaskContext;

    @TableField(value = "duration")
    private Long duration;

    @TableField(value = "name")
    private String name;

    @TableField(value = "feedback_status")
    private Integer feedBackStatus;

    /**
     * 外部员工工号：由mk发起的演练执行，保存发起人工号
     */
    @TableField(value = "outer_user_id")
    private String outerUserId;


    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    Boolean isDelete;



}
