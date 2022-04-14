package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ActivityRunParam;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ActivityTask implements Serializable {

    private String experimentTaskId;

    /**
     * 活动任务ID
     */
    private String activityTaskId;

    private String activityId;

    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 运行状态
     */
    private StateEnum state;

    /**
     * 运行结果
     */
    private ResultEnum runResult;

    /**
     * 用户校验结果
     */
    private UserCheckState userCheckState;

    /**
     * 所处阶段
     */
    private PhaseType phase;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 运行时候的参数和定义
     */
    private ActivityRunParam runParam;

    /**
     * 小程序的执行信息
     */
    private List<MiniAppTaskSummary> apps;

    /**
     * 小程序的名字
     */
    private String miniAppName;

    /**
     * 小程序的code
     */
    private String miniAppCode;

    /**
     * activity顺序
     */
    private Integer order;

    private String executableAppCode;

    private String flowId;

    /**
     * 是否运行重试
     */
    private Boolean retryable;

}
