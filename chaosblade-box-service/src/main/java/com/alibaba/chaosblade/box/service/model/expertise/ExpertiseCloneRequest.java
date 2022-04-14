package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseCloneRequest  extends BaseRequest {

    private String expertiseId;

    private String name;

}
