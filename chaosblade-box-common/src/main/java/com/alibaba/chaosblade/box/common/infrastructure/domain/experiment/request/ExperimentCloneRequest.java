package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentCloneRequest extends BaseRequest {

    private String experimentId;

    private String name;

    private String workspaceId;
}
