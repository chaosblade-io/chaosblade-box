package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response;

import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardConfiguration;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentFlowInfo {

    /**
     * 演练ID
     */
    private String experimentId;

    /**
     * 微流程group
     */
    private List<MiniFlowGroup> flowGroups;

    /**
     * 守护者
     */
    private ExperimentGuardConfiguration guardConf;

    /**
     * 运行模式
     */
    private ExperimentRunModeEnum runMode;

    /**
     * 演练持续时间
     */
    private Long duration;

    private ExperimentSchedulerConfig schedulerConfig;

}
