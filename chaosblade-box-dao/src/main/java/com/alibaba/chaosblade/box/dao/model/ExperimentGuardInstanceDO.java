package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardMonitorMetricResultEntity;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */

@TableName(value = "t_chaos_experiment_guard_instance")
@Data
public class ExperimentGuardInstanceDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER_STR)
    private String instanceId;

    private String name;

    private String experimentTaskId;

    private String guardId;

    /**
     * 状态
     */
    private GuardRunState state;

    private String appCode;

    /**
     * argument
     */
    private ExperimentGuardArgument argument;

    /**
     * action类型
     * 0~ACTION_TYPE_MONITOR
     * 1~ACTION_TYPE_RECOVERY
     */
    private Integer actionType;

    private String triggeredReason;

    /**
     * activityTaskId
     */
    private String activityTaskId;

    private ExperimentGuardMonitorMetricResultEntity value;

    public boolean isHalted() {
        return GuardRunState.TRIGGERED.equals(state) || GuardRunState.FINISHED.equals(state);
    }

}
