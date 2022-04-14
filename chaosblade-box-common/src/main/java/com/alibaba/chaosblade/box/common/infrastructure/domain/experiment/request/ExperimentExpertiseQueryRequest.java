package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentExpertiseQueryRequest extends BaseRequest {

    @JSONField(name = "expertise_id")
    private String expertiseId;
}
