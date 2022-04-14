package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class ExperimentAppRiskMessageRequest extends BaseRequest {

    private String experimentId;

}
