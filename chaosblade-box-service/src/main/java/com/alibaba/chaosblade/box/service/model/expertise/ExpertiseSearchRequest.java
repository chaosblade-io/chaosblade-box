package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExpertiseSearchRequest extends PageableRequest {

    private String key;

    private List<String> tagNames;

    private String scopeType;

}
