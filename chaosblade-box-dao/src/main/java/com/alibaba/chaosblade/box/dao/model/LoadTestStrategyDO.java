package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 压测策略实体类
 *
 * @author ZhengMingZhuo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_chaos_load_test_strategy")
public class LoadTestStrategyDO extends BaseDO {

    /**
     * 策略ID
     */
    @TableField(value = "strategy_id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String strategyId;

    /**
     * 是否启用：0-禁用，1-启用
     */
    @TableField(value = "enable")
    private Integer enable;

    /**
     * 压测定义ID，关联t_chaos_load_test_definition表的definition_id字段
     */
    @TableField(value = "definition_id")
    private String definitionId;

    /**
     * 实验ID，关联t_chaos_experiment表的experiment_id字段
     */
    @TableField(value = "experiment_id")
    private String experimentId;

    /**
     * 故障前预热时间（秒）
     */
    @TableField(value = "start_before_fault_sec")
    private Integer startBeforeFaultSec;

    /**
     * 压测持续时长（秒）
     */
    @TableField(value = "traffic_duration_sec")
    private Integer trafficDurationSec;

    /**
     * 压测异常时是否中止演练：0-不中止，1-中止
     */
    @TableField(value = "abort_on_load_failure")
    private Integer abortOnLoadFailure;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 命名空间
     */
    @TableField(value = "namespace")
    private String namespace;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
}
