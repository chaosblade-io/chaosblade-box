package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 * 
 */
@Data
public class ExperimentScopePageableRequest extends PageableRequest {

    @JSONField(name = "scope_type")
    private Integer scopeType;

    private ExperimentScopeFilter filter;

}
