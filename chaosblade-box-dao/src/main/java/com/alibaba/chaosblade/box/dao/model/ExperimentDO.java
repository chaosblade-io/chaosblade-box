package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ExperimentAttributes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentOperatorType;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.HashMap;

@Data
@TableName(value = "t_chaos_experiment")
public class ExperimentDO extends BaseDO {

    /**
     * 演练id
     */
    @TableField(value = "experiment_id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String experimentId;

    /**
     * 演练名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 演练描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 演练故障等级
     *
     * @deprecated
     */
    @TableField(value = "level")
    private String level;

    /**
     * 演练的负责人ID
     *
     * @deprecated
     */
    @TableField(value = "owner_user_id")
    private String ownerUserId;

    /**
     * user id
     */
    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "user_type")
    private ExperimentOperatorType userType;

    /**
     * 演练的状态
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 最近一次演练的结果
     */
    @TableField(value = "result_state")
    private Integer result;

    /**
     * 最近一次运行的taskId
     */
    @TableField(value = "task_id")
    private String experimentTaskId;

    /**
     * cloud only
     */
    @TableField(value = "namespace")
    String namespace;

    /**
     * 小程序描述
     */
    @TableField(value = "mini_app_desc")
    String miniAppDesc;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    Boolean isDelete;

    @TableField(value = "outer_id")
    private String outerId;

    @TableField(value = "scheduler_config")
    private ExperimentSchedulerConfig schedulerConfig;

    /**
     * 运行模式
     */
    @TableField(value = "run_mode")
    private ExperimentRunModeEnum runMode;

    @TableField(value = "attributes")
    private HashMap<String, String> attributes;

    /**
     * 演练持续时间
     */
    @TableField(value = "duration")
    private Long duration;

    /**
     * 是否是模板
     */
    @TableField(value = "is_template")
    private Boolean isTemplate;

    /**
     * 演练来源
     */
    @TableField(value = "source")
    private Integer source;

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

    public void removeAttribute(String key) {
        if (attributes == null) { return; }
        attributes.remove(key);
    }

    public boolean isDraft() {
        return ExperimentStateEnum.DRAFT.getValue().equals(this.state);
    }

    public boolean isNotReady() {
        return ExperimentStateEnum.DRAFT.getValue().equals(this.state) || ExperimentStateEnum.INVISIBLE.getValue()
                .equals(this.state);
    }

    public ExperimentStateEnum getExperimentStateEnum() {
        return EnumUtil.integerValueOf(ExperimentStateEnum.class, this.state);
    }

    public boolean isNewExperimentDefinition() {
        return Boolean.parseBoolean(getAttribute(ExperimentAttributes.ATTR_NEW_DEFINITION_FLOW));
    }

    public ChaosUser getCreator() {
        return new ChaosUser(userId);
    }

    public boolean isRunning() {
        return ExperimentStateEnum.RUNNING.getValue().equals(state);
    }

    public boolean isFinished() {
        return !isRunning();
    }

    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getValue().equals(result);
    }

}
