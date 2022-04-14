package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 * 
 * 
 */
@Data
public class ScopeExtInfo {

    @JSONField(name = "app_name")
    private String appName;

    @JSONField(name = "labels")
    private List<String> labels;
}
