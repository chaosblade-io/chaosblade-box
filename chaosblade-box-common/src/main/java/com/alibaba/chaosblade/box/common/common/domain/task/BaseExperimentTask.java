package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseExperimentTask implements Serializable {

    /**
     * 演练名字
     */
    private String experimentName;

    /**
     * 演练ID
     */
    private String experimentId;

    /**
     * 演练任务Id
     */
    private String taskId;

    /**
     * 演练开始时间
     */
    private Date startTime;

    /**
     * 结束事件
     */
    private Date endTime;

    /**
     * 运行状态
     */
    private StateEnum state;

    /**
     * 结果
     */
    private ResultEnum result;

    /**
     * 演练错误信息
     */
    private String message;

    /**
     * 当前阶段
     */
    private PhaseType currentPhase;
    /**
     * 演练中涉及的aone应用名列表
     */
    private Set<String> aoneApps;

    private ChaosUser creator;

    private ExperimentTaskExtInfo extInfo;

    /**
     * 用户反馈状态
     */
    private Integer feedbackStatus;

    private Integer source;

    private String namespace;

}
