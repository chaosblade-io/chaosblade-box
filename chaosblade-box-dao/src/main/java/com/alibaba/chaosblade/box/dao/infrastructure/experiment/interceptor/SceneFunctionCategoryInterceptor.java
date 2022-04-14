package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;


import com.alibaba.chaosblade.box.common.infrastructure.scene.CategoryFilterCondition;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
public interface SceneFunctionCategoryInterceptor {

    /**
     * 过滤场景目录
     * @param categories
     * @param condition
     */
    void filterCategory(List<SceneFunctionCategoryDO> categories, CategoryFilterCondition condition);

}
