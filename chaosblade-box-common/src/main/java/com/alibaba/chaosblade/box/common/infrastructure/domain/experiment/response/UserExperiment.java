package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class UserExperiment {

    /**
     * 演练ID
     */
    private String experimentId;

    /**
     * 演练名称
     */
    private String name;

    /**
     * 演练的创建时间
     */
    private Date createTime;

    /**
     * 演练的标签
     */
    private List<String> tags = new ArrayList<>();

    /**
     * 演练包含了哪些小程序能力
     */
    private ArrayList<String> miniAppDesc;

    /**
     * 演练的状态
     */
    private ExperimentStateEnum state;

    /**
     * 演练任务信息
     */
    private ExperimentTask task;

    /**
     * 定时任务配置
     */
    private ExperimentSchedulerConfig schedulerConfig;

    /**
     * 演练中小程序的警示信息
     */
    private List<ExperimentAppRisk> experimentAppRisks;

    /**
     * 演练创建者
     */
    private String creator;

    private Integer permission;

}
