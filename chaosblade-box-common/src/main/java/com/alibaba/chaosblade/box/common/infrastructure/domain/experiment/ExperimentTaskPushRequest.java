package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ExperimentTaskPushRequest extends BaseRequest {
    private String experimentTaskId;
}
