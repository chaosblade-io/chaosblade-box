package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/12
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
