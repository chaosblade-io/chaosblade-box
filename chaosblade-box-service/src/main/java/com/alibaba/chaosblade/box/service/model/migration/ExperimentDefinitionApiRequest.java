package com.alibaba.chaosblade.box.service.model.migration;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */

@Data
public class ExperimentDefinitionApiRequest extends BaseRequest {
    private String name;

    /**
     * 演练ID
     */
    private String experimentId;

    /**
     * 微流程group
     */
    private List<FlowGroup> flowGroups;

    /**
     * 运行模式
     */
    private ExperimentRunModeEnum runMode;

    /**
     * 持续时长
     */
    private Long duration;



}
