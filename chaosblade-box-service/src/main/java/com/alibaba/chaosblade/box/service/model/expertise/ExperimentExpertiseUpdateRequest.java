package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentExpertiseUpdateRequest extends BaseExpertiseOperationRequest {

    @JSONField(name = "expertise_id")
    private String expertiseId;

    private boolean syncConfig = false;

    private Integer expertiseState;
}
