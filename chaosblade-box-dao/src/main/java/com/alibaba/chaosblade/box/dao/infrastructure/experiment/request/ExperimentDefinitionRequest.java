package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuardConfiguration;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentDefinitionRequest extends BaseRequest {

    private ExperimentDO experimentDO;

    public String getExperimentId() {
        return experimentDO != null ? experimentDO.getExperimentId() : experimentId;
    }

    /**
     * 演练ID
     */
    private String experimentId;

    /**
     * 专家经验Id
     */
    private String expertiseId;

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
     * 是否老流程适配
     */
    private boolean isOldAdapter;

    /**
     * 持续时长
     */
    private Long duration;

    private boolean isExpertise;

    private ExperimentSchedulerConfig schedulerConfig;

}
