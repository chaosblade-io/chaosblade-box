package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.infrastructure.scene.CategoryFilterCondition;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.SceneFunctionCategoryInterceptor;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionCategoryRelationRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Order(Integer.MIN_VALUE)
@Component
public class NoChildSceneFunctionCategoryInterceptor implements SceneFunctionCategoryInterceptor{

    @Resource
    private SceneFunctionCategoryRelationRepository sceneFunctionCategoryRelationRepository;

    @Override
    public void filterCategory(List<SceneFunctionCategoryDO> categories, CategoryFilterCondition condition) {
        if(condition.getFilterNoChild() && !CollectionUtil.isNullOrEmpty(categories)) {
            filterNoChild(categories);
        }
    }

    private void filterNoChild(List<SceneFunctionCategoryDO> categories) {
        categories.removeIf(this::removeIfNoChild);
    }

    private boolean removeIfNoChild(SceneFunctionCategoryDO sceneFunctionCategoryDO) {
        if (CollectionUtil.isNullOrEmpty(sceneFunctionCategoryDO.getChildren())) {
            return sceneFunctionCategoryRelationRepository
                    .countByCategoryId(sceneFunctionCategoryDO.getCategoryId()) <= 0;
        } else {
            sceneFunctionCategoryDO.getChildren().removeIf(this::removeIfNoChild);
        }
        return CollectionUtil.isNullOrEmpty(sceneFunctionCategoryDO.getChildren());
    }

}
