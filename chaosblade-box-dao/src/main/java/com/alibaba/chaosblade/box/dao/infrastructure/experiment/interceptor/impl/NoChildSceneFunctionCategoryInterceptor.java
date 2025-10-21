/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.infrastructure.scene.CategoryFilterCondition;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.SceneFunctionCategoryInterceptor;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionCategoryRelationRepository;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author sunpeng */
@Order(Integer.MIN_VALUE)
@Component
public class NoChildSceneFunctionCategoryInterceptor implements SceneFunctionCategoryInterceptor {

  @Resource private SceneFunctionCategoryRelationRepository sceneFunctionCategoryRelationRepository;

  @Override
  public void filterCategory(
      List<SceneFunctionCategoryDO> categories, CategoryFilterCondition condition) {
    if (condition.getFilterNoChild() && !CollectionUtil.isNullOrEmpty(categories)) {
      filterNoChild(categories);
    }
  }

  private void filterNoChild(List<SceneFunctionCategoryDO> categories) {
    categories.removeIf(this::removeIfNoChild);
  }

  private boolean removeIfNoChild(SceneFunctionCategoryDO sceneFunctionCategoryDO) {
    if (CollectionUtil.isNullOrEmpty(sceneFunctionCategoryDO.getChildren())) {
      return sceneFunctionCategoryRelationRepository.countByCategoryId(
              sceneFunctionCategoryDO.getCategoryId())
          <= 0;
    } else {
      sceneFunctionCategoryDO.getChildren().removeIf(this::removeIfNoChild);
    }
    return CollectionUtil.isNullOrEmpty(sceneFunctionCategoryDO.getChildren());
  }
}
