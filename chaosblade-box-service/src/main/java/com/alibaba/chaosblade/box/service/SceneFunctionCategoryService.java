package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCategoryUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.scene.CategoryFilterCondition;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;

import java.util.List;

/**
 * 小程序类目服务
 * Author: sunju
 *
 * Date:   2019/11/12
 */
public interface SceneFunctionCategoryService {

    /**
     * 根据类型ID查询类目
     *
     * @param categoryId
     * @return
     */
    SceneFunctionCategoryDO getCategoryById(String categoryId);

    /**
     * 根据演练阶段和支持机器类型查询类目
     *
     * @param phase
     * @param scopeType
     * @param condition
     * @return
     */
    List<SceneFunctionCategoryDO> getCategoriesByPhase(Integer phase, Integer scopeType, Integer osType, String lang, CategoryFilterCondition condition);

    /**
     * 根据演练阶段,支持类型以及类目类型
     *
     * @param phase
     * @param type
     * @param scopeType
     * @return
     */
    List<SceneFunctionCategoryDO> getCategoriesByPhaseAndType(Integer phase, Integer type, Integer scopeType, Integer opType, String lang);

    /**
     * 新增类目
     *
     * @param request
     * @return
     * @throws ChaosException
     */
    SceneFunctionCategoryDO addCategory(SceneFunctionCategoryUpdateRequest request) throws ChaosException;

    /**
     * 更新类目
     *
     * @param request
     * @throws ChaosException
     */
    void updateCategory(SceneFunctionCategoryUpdateRequest request) throws ChaosException;

    /**
     * 删除类目
     *
     * @param categoryId
     * @throws ChaosException
     */
    void deleteCategory(String categoryId) throws ChaosException;
}
