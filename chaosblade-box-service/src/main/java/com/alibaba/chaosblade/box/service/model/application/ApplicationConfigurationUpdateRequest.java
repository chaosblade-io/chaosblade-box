package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.configuration.ConfigurationScope;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ApplicationConfigurationUpdateRequest extends BaseRequest {

    private String value;

    @JSONField(name = "app_id")
    private String appId;

    private String alias;

    /**
     * 选择范围
     */
    private ConfigurationScope scope;

    /**
     * 是否覆盖用户的参数值
     */
    private Boolean override;

    private String name;

}
