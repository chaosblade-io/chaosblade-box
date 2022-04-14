package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.dao.model.base.BaseTaskDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

/**
 * 活动的运行记录
 *
 * @author haibin
 *
 *
 */
@Data
@TableName(value = "t_chaos_activity_task")
public class ActivityTaskDO extends BaseTaskDO {

    public static String FISSION_APP_CODE = "FISSION_APP_CODE";

    /**
     * 正常发起的流程
     */
    public static Integer TASK_SOURCE_NORMAL = 0;

    /**
     * 来自重试的任务
     */
    public static Integer TASK_SOURCE_RETRY = 1;

    /**
     * 有些activity是没有host的
     */
    public static String NO_HOST = "-1";

    /**
     * 活动ID
     */
    @TableField(value = "activity_id")
    private String activityId;

    /**
     * 演练任务ID
     */
    @TableField(value = "experiment_task_id")
    private String experimentTaskId;

    @TableField(value = "phase_task_id")
    private String phaseTaskId;

    /**
     * 当前阶段
     */
    @TableField(value = "phase")
    private PhaseType phase;

    /**
     * 是否单独运行,如果是单独运行,不会触发下一个活动的运行
     */
    @TableField(value = "task_mode")
    private Integer taskMode;

    /**
     * 上一个activityTaskId
     */
    @TableField(value = "pre_activity_task_id")
    private String preActivityTaskId;

    /**
     * 下一个activityTaskId
     */
    @TableField(value = "next_activity_task_id")
    private String nextActivityTaskId;

    /**
     * 对应的攻击活动任务ID
     */
    @TableField(value = "attack_activity_task_id")
    private String attackActivityTaskId;

    /**
     * 父 activity taskId
     */
    @TableField(value = "parent_activity_task_id", exist = false)
    private String parentActivityTaskId;

    /**
     * @see UserCheckState
     */
    @TableField(value = "user_check_state")
    public Integer userCheckState;

    @TableField(value = "run_param")
    public ActivityRunParam runParam;

    @TableField(value = "activity_order")
    private Integer activityOrder;

    @TableField(value = "app_code")
    private String appCode;

    @TableField(value = "attributes")
    private HashMap<String, String> attributes;

    @TableField(value = "activity_name")
    private String activityName;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    @TableField(value = "flow_id")
    private String flowId;

    @TableField(value = "app_id")
    private Long appId;

    @TableField(value = "user_id")
    private String userId;

    public String getExecutableAppCode() {
        return executableAppCode == null ? appCode : executableAppCode;
    }

    /**
     * 如果是裂变小程序，executableAppCode为父code,否则就等于小程序的code
     */
    @TableField(value = "executable_app_code")
    private String executableAppCode;

    public String getAttribute(String key) {
        if (attributes == null) { return null; }
        return attributes.get(key);
    }

    public void putAttribute(String key, String value) {
        if (key == null || value == null) { return; }
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    public boolean userHasChecked() {
        return userCheckState != null && !UserCheckState.USER_CHECK_STATE_WAITING.getValue().equals(userCheckState);
    }

    public boolean inPhase(PhaseType phaseType) {
        return this.phase.equals(phaseType);
    }

    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getValue().equals(this.result);
    }

    public boolean isFinishedAndUserCheckedIfNeed() {
        return isFinished() && (userCheckState == null || userHasChecked());
    }

}
