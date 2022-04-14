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
public class PageableScopeExperimentTaskQueryRequest extends PageableRequest {


    /**
     * 机器类型
     */
    @JSONField(name = "scope_type")
    private Integer scopeType;

    /**
     * configurationId
     */
    @JSONField(name = "configuration_id")
    private String configurationId;
}
