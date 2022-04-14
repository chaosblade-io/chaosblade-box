package com.alibaba.chaosblade.box.service.model.scene;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sunju
 *
 * Date:   2019/11/14
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SceneFunctionCategoryVO {

    String categoryId;
    String parentId;
    String name;
    Integer phase;
    Integer level;
    Integer type;
    List<Integer> supportScopeTypes;
    List<SceneFunctionCategoryVO> children;

    public static SceneFunctionCategoryVO from(SceneFunctionCategoryDO sceneFunctionCategory) {
        int level = 1;

        SceneFunctionCategoryVO category = convert(sceneFunctionCategory, level);
        category.setChildren(convertChildren(sceneFunctionCategory.getChildren(), level + 1));

        return category;
    }

    private static List<SceneFunctionCategoryVO> convertChildren(List<SceneFunctionCategoryDO> children, int level) {
        List<SceneFunctionCategoryVO> categories = Lists.newArrayList();
        if (null == children || children.isEmpty()) {
            return categories;
        }

        for (SceneFunctionCategoryDO child : children) {
            SceneFunctionCategoryVO category = convert(child, level);

            if (!CollectionUtil.isNullOrEmpty(child.getChildren())) {
                category.setChildren(convertChildren(child.getChildren(), level + 1));
            }

            categories.add(category);
        }

        return categories;
    }

    private static SceneFunctionCategoryVO convert(SceneFunctionCategoryDO sceneFunctionCategory, int level) {
        SceneFunctionCategoryVO category = new SceneFunctionCategoryVO();
        category.setCategoryId(sceneFunctionCategory.getCategoryId());
        category.setParentId(sceneFunctionCategory.getParentId());
        category.setName(sceneFunctionCategory.getName());
        category.setPhase(sceneFunctionCategory.getPhase());
        category.setLevel(level);
        category.setType(sceneFunctionCategory.getType());

        List<Integer> supportScopeTypes = new ArrayList<>();
        if (Boolean.TRUE.equals(sceneFunctionCategory.getSupportK8s())) {
            supportScopeTypes.add(ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S);
        }
        if (Boolean.TRUE.equals(sceneFunctionCategory.getSupportHost())) {
            supportScopeTypes.add(ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST);
        }
        category.setSupportScopeTypes(supportScopeTypes);

        return category;
    }

}
