package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ScopeInfoQueryRequest extends BaseRequest implements Serializable {

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
