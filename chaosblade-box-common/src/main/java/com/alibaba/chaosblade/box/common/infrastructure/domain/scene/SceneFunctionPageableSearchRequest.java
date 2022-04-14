package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import com.alibaba.chaosblade.box.common.infrastructure.domain.PageableSearchRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionPageableSearchRequest extends PageableSearchRequest {

    Integer phase;
    Integer source;
    Integer enabled;
    String categoryId;
    Integer scopeType;

    private Integer k8sResourceType;

}
