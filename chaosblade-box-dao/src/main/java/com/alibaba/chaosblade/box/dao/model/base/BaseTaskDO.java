package com.alibaba.chaosblade.box.dao.model.base;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseTaskDO extends BaseDO {

    @TableId(type = IdType.ID_WORKER_STR, value = "task_id")
    protected String taskId;

    /**
     * 任务的运行状态
     */
    @TableField(value = "run_state")
    protected Integer state;

    /**
     * 结束时间
     */
    @TableField(value = "gmt_end")
    protected Date gmtEnd;

    /**
     * 失败原因
     */
    @TableField(value = "error_message")
    protected String errorMessage;

    /**
     * 任务执行的目标机器
     */
    @TableField(value = "host_ip")
    protected String hostIp;

    /**
     * 结果标志
     */
    @TableField(value = "result_state")
    protected Integer result;

    public boolean isReady() {
        return StateEnum.READY.getValue().equals(state);
    }

    public boolean isRunning() {
        return StateEnum.RUNNING.getValue().equals(this.state);
    }

    public StateEnum getStateEnum() {
        return EnumUtil.integerValueOf(StateEnum.class, this.state);
    }

    public ResultEnum getResultEnum() {
        return EnumUtil.integerValueOf(ResultEnum.class, this.result);
    }

    public boolean isFinished() {
        return StateEnum.FINISHED.getValue().equals(this.state);
    }

    public boolean isRejcted() {
        return ResultEnum.REJECTED.getValue().equals(this.result);
    }

    public boolean isStopping() {
        return StateEnum.STOPPING.getValue().equals(this.state);
    }

    public boolean isError() {
        return ResultEnum.ERROR.getValue().equals(this.result);
    }
}
