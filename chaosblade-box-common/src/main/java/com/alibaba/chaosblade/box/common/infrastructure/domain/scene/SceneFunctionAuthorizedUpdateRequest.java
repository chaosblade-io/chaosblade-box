package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class SceneFunctionAuthorizedUpdateRequest extends BaseRequest {

    private String functionId;

    private String userId;

    private String permissions;



}
