package com.alibaba.chaosblade.box.service.model.loadtest;

import com.alibaba.chaosblade.box.dao.model.LoadTestTaskDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 压测任务视图对象
 *
 * @author ZhengMingZhuo
 */
@Data
@ApiModel("压测任务视图对象")
public class LoadTestTaskVO {

    @ApiModelProperty("压测任务ID")
    private String taskId;

    @ApiModelProperty("压测策略ID")
    private String strategyId;

    @ApiModelProperty("演练任务ID")
    private String experimentTaskId;

    @ApiModelProperty("压测引擎执行ID")
    private String executionId;

    @ApiModelProperty("任务状态")
    private String status;

    @ApiModelProperty("状态描述")
    private String statusDescription;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date endTime;

    @ApiModelProperty("错误信息")
    private String errorMessage;

    @ApiModelProperty("结果文件路径")
    private String resultPath;

    @ApiModelProperty("报告路径")
    private String reportPath;

    @ApiModelProperty("日志路径")
    private String logPath;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date createdAt;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Shanghai")
    private Date updatedAt;

    /**
     * 从DO对象转换为VO对象
     */
    public static LoadTestTaskVO from(LoadTestTaskDO taskDO) {
        if (taskDO == null) {
            return null;
        }

        LoadTestTaskVO vo = new LoadTestTaskVO();
        vo.setTaskId(taskDO.getTaskId());
        vo.setStrategyId(taskDO.getStrategyId());
        vo.setExperimentTaskId(taskDO.getExperimentTaskId());
        vo.setExecutionId(taskDO.getExecutionId());
        vo.setStatus(taskDO.getStatus());
        vo.setStatusDescription(getStatusDescription(taskDO.getStatus()));
        vo.setStartTime(taskDO.getStartTime());
        vo.setEndTime(taskDO.getEndTime());
        vo.setErrorMessage(taskDO.getErrorMessage());
        vo.setResultPath(taskDO.getResultPath());
        vo.setReportPath(taskDO.getReportPath());
        vo.setLogPath(taskDO.getLogPath());
        vo.setCreatedAt(taskDO.getGmtCreate());
        vo.setUpdatedAt(taskDO.getGmtModified());

        return vo;
    }

    /**
     * 获取状态描述
     */
    private static String getStatusDescription(String status) {
        LoadTestTaskDO.Status statusEnum = LoadTestTaskDO.Status.fromCode(status);
        return statusEnum != null ? statusEnum.getDescription() : status;
    }
}
