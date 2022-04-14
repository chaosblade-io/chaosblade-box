package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Set;

/**
 * 演练经验缩略图
 *
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseView {

    @JSONField(name = "expertise_id")
    private String expertiseId;

    private String name;

    @JSONField(name = "function_desc")
    private String functionDesc;

    @JSONField(name = "tags")
    private Set<String> tags;

    @JSONField(name = "flow")
    private ExpertiseFlowView flow;

    @JSONField(name = "type")
    private Integer type;

    @JSONField(name = "scope_type")
    private Set<Integer> scopeType;

}
