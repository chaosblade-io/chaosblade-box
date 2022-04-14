package com.alibaba.chaosblade.box.common.common.domain.task;

import com.alibaba.chaosblade.box.common.common.domain.experiment.PhaseInfo;
import lombok.Data;

import java.util.List;

/**
 * 演练的任务对象，一个演练分为:
 * Experiment->Phase->Activity
 *
 * 1/一个演练包含多个阶段(Phase),
 * 2.一个Phase对应了多个Activity
 * 3.一个activity对应一个小程序
 *
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTask extends BaseExperimentTask {

    /**
     * 进度百分比
     */
    private long progressPercent;

    /**
     * 每个阶段的信息
     */
    private List<PhaseInfo> phases;

}
