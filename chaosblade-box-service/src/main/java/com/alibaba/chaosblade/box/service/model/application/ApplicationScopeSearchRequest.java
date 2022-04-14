package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ApplicationScopeSearchRequest extends PageableRequest {

    @JSONField(name = "app_id")
    private Long appId;
    @JSONField(name = "group")
    private String groupName;
    @JSONField(name = "key")
    private String partName;

    private String appName;

    private String ip;
}
