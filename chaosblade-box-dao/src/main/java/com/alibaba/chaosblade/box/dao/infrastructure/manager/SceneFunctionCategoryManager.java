package com.alibaba.chaosblade.box.dao.infrastructure.manager;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryRelationDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionCategoryRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class SceneFunctionCategoryManager {

    @Autowired
    private SceneFunctionCategoryRelationRepository sceneFunctionCategoryRelationRepository;

    public void rebindFunctionCategories(List<SceneFunctionDO> sceneFunctionDOS) {
        sceneFunctionDOS.forEach(sceneFunctionDO -> {
            sceneFunctionCategoryRelationRepository.deleteByFunctionCode(sceneFunctionDO.getCode());
            if (CollectionUtil.isNullOrEmpty(sceneFunctionDO.getCategoryList())) { return; }
            List<SceneFunctionCategoryRelationDO> sceneFunctionCategoryRelationDOS = sceneFunctionDO
                .getCategoryList().stream().map(
                    s -> {
                        SceneFunctionCategoryRelationDO sceneFunctionCategoryRelationDO
                            = new SceneFunctionCategoryRelationDO();
                        sceneFunctionCategoryRelationDO.setCode(sceneFunctionDO.getCode());
                        sceneFunctionCategoryRelationDO.setCategoryId(s);
                        sceneFunctionCategoryRelationDO.setFunctionId(sceneFunctionDO.getFunctionId());
                        return sceneFunctionCategoryRelationDO;
                    }).collect(Collectors.toList());
            if (!sceneFunctionCategoryRelationDOS.isEmpty()) {
                sceneFunctionCategoryRelationRepository.saveBatch(sceneFunctionCategoryRelationDOS);
            }
        });
    }

    public List<String> getFunctionIdByCategoryId(String categoryId) {
        return sceneFunctionCategoryRelationRepository.findByCategoryId(categoryId).stream().map(
            SceneFunctionCategoryRelationDO::getFunctionId).collect(Collectors.toList());
    }
}
