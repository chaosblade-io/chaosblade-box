package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardArgument;
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
@Data
@TableName("t_chaos_experiment_guard")
public class ExperimentGuardDO extends BaseDO {

    public static Integer ACTION_TYPE_MONITOR = 0;

    public static Integer ACTION_TYPE_RECOVERY = 1;

    public static Integer ACTION_TYPE_TIMEOUT_RECOVERY = 2;

    public static Integer ACTION_TYPE_HIT_COUNT_MONITOR = 3;

    @TableId(type = IdType.ID_WORKER_STR, value = "guard_id")
    private String guardId;

    private String experimentId;

    private String appCode;

    private String name;

    private Integer actionType;

    private ExperimentGuardArgument argument;

    private Boolean required;

}
