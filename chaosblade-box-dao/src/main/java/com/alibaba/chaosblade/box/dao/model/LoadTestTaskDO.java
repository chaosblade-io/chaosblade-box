package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 压测任务实体类
 *
 * @author ZhengMingZhuo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_chaos_load_test_task")
public class LoadTestTaskDO extends BaseDO {

    /**
     * 压测任务ID
     */
    @TableField(value = "task_id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String taskId;

    /**
     * 压测策略ID，关联t_chaos_load_test_strategy表
     */
    @TableField(value = "strategy_id")
    private String strategyId;

    /**
     * 演练任务ID，关联t_chaos_experiment_task表
     */
    @TableField(value = "experiment_task_id")
    private String experimentTaskId;

    /**
     * 压测引擎执行ID
     */
    @TableField(value = "execution_id")
    private String executionId;

    /**
     * 任务状态：PENDING|RUNNING|SUCCEEDED|FAILED|STOPPED|TIMEOUT
     */
    @TableField(value = "status")
    private String status;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 错误信息
     */
    @TableField(value = "error_message")
    private String errorMessage;

    /**
     * 结果文件路径
     */
    @TableField(value = "result_path")
    private String resultPath;

    /**
     * 报告路径
     */
    @TableField(value = "report_path")
    private String reportPath;

    /**
     * 日志路径
     */
    @TableField(value = "log_path")
    private String logPath;

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

    /**
     * 压测任务状态枚举
     */
    public enum Status {
        PENDING("PENDING", "等待中"),
        RUNNING("RUNNING", "运行中"),
        SUCCEEDED("SUCCEEDED", "成功"),
        FAILED("FAILED", "失败"),
        STOPPED("STOPPED", "已停止"),
        TIMEOUT("TIMEOUT", "超时");

        private final String code;
        private final String description;

        Status(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        public static Status fromCode(String code) {
            for (Status status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }
    }

    /**
     * 判断任务是否正在运行
     */
    public boolean isRunning() {
        return Status.RUNNING.getCode().equals(this.status);
    }

    /**
     * 判断任务是否已完成（成功、失败、停止、超时）
     */
    public boolean isFinished() {
        return Status.SUCCEEDED.getCode().equals(this.status) ||
               Status.FAILED.getCode().equals(this.status) ||
               Status.STOPPED.getCode().equals(this.status) ||
               Status.TIMEOUT.getCode().equals(this.status);
    }

    /**
     * 判断任务是否成功
     */
    public boolean isSucceeded() {
        return Status.SUCCEEDED.getCode().equals(this.status);
    }

    /**
     * 判断任务是否失败
     */
    public boolean isFailed() {
        return Status.FAILED.getCode().equals(this.status);
    }

    /**
     * 判断任务是否被停止
     */
    public boolean isStopped() {
        return Status.STOPPED.getCode().equals(this.status);
    }

    /**
     * 判断任务是否超时
     */
    public boolean isTimeout() {
        return Status.TIMEOUT.getCode().equals(this.status);
    }
}
