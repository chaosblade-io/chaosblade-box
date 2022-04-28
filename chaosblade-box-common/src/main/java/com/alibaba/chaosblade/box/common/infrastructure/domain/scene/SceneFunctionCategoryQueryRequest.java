package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * Date:   2019/11/12
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionCategoryQueryRequest {

    String categoryId;
    String parentId;
    Integer phase;
    Integer type;
    Integer scopeType;
    Boolean recursive;
    Boolean filterNoChild = false;
    String cloudServiceType;
    Integer osType;

    String lang;

}
