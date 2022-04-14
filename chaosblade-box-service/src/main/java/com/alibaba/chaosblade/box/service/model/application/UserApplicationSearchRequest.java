package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class UserApplicationSearchRequest extends BaseRequest {

    private String key;

    private boolean filterDisabled;

}
