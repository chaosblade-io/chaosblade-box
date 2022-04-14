package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Author: sunju
 *
 * Date:   2019/11/12
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionCategoryUpdateRequest {

    String categoryId;
    String name;
    String parentId;
    Integer phase;
    Integer type;
    Boolean isDelete;
    List<Integer> supportScopeTypes;

}
