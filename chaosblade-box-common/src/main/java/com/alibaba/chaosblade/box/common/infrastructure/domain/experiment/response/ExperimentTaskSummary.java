package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskSummary extends BaseExperimentTask {

    private List<ActivityTask> activities;

    private Integer permission;

    /**
     * 是否包含jvm场景
     */
    private Boolean isJvm;

}
