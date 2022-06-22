package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.constant.ChaosConstant;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCategoryQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCategoryUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.scene.CategoryFilterCondition;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.SceneFunctionCategoryInterceptor;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionCategoryRepository;
import com.alibaba.chaosblade.box.service.SceneFunctionCategoryService;
import com.alibaba.chaosblade.box.service.TranslateService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/11/12
 */
@Service
@Slf4j
public class SceneFunctionCategoryServiceImpl implements SceneFunctionCategoryService {

    @Resource
    private SceneFunctionCategoryRepository sceneFunctionCategoryRepository;

    @Resource
    private List<SceneFunctionCategoryInterceptor> sceneFunctionCategoryInterceptors;

    @Autowired
    private TranslateService translateService;

    @Override
    public SceneFunctionCategoryDO getCategoryById(String categoryId) {
        return sceneFunctionCategoryRepository.findById(categoryId).orElse(null);
    }

    @Override
    public List<SceneFunctionCategoryDO> getCategoriesByPhase(Integer phase, Integer scopeType, Integer osType, String lang, CategoryFilterCondition condition) {
        List<SceneFunctionCategoryDO> categories = this.getCategoriesByPhaseAndType(phase, null, scopeType, osType, lang);
        sceneFunctionCategoryInterceptors.forEach(sceneFunctionCategoryInterceptor -> sceneFunctionCategoryInterceptor.filterCategory(categories,condition));
        return categories;
    }

    @Override
    public List<SceneFunctionCategoryDO> getCategoriesByPhaseAndType(Integer phase, Integer type, Integer scopeType, Integer osType, String lang) {
        SceneFunctionCategoryQueryRequest request = new SceneFunctionCategoryQueryRequest();
        request.setPhase(phase);
        request.setType(type);
        request.setScopeType(scopeType);
        request.setOsType(osType);
        List<SceneFunctionCategoryDO> parallel = sceneFunctionCategoryRepository.getSceneFunctionCategories(request);

        if (parallel.isEmpty()) { return new ArrayList<>();}
        if (!Strings.isNullOrEmpty(lang) && lang.equals(ChaosConstant.LANGUAGE_EN)){
            translateNameToEn(parallel);
        }
        //过滤操作系统类型 1表示windows
//        if(null != osType && osType == 1) {
//            parallel = parallel.stream().filter(sceneFunctionCategoryDO -> {
//                return  null != sceneFunctionCategoryDO.getSupportOsTypes() && sceneFunctionCategoryDO.getSupportOsTypes().contains(osType);
//            }).collect(Collectors.toList());
//        }
        return buildTrees(parallel);
    }

    public void translateNameToEn(List<SceneFunctionCategoryDO> categories) {

        for (SceneFunctionCategoryDO categoryDO : categories) {
            categoryDO.setName(translateService.translateToEn(categoryDO.getName()));
        }

    }

    @Override
    public SceneFunctionCategoryDO addCategory(SceneFunctionCategoryUpdateRequest request) throws ChaosException {
        String parentId = request.getParentId();

        SceneFunctionCategoryDO parent = null;
        if (!Strings.isNullOrEmpty(parentId)) {
            parent = this.getCategoryById(parentId);
        }

        SceneFunctionCategoryDO category = new SceneFunctionCategoryDO();
        category.setCategoryId(IdWorker.getIdStr());
        category.setName(request.getName());
        category.setParentId(request.getParentId());

        if (null != parent) {
            category.setPhase(parent.getPhase());
        } else {
            category.setPhase(request.getPhase());
        }

        if (null != request.getType()) {
            category.setType(request.getType());
        } else {
            if (null != parent) {
                category.setType(parent.getType());
            } else {
                category.setType(SceneFunctionCategoryDO.DEFAULT_CATEGORY_TYPE);
            }
        }

        category.setSupportScopeTypeList(request.getSupportScopeTypes());

        boolean result = sceneFunctionCategoryRepository.add(category);
        if (!result) {
            throw new ChaosException(CommonErrorCode.B_ERROR_CREATE_MINIAPP_CATEGORY,
                "Add scene function category failed.");
        }
        return category;
    }

    @Override
    public void updateCategory(SceneFunctionCategoryUpdateRequest request) throws ChaosException {
        String categoryId = request.getCategoryId();
        if (Strings.isNullOrEmpty(categoryId)) {
            throw new ChaosException(CommonErrorCode.P_ARGUMENT_ILLEGAL,
                "CategoryId cannot be null or empty for the update.");
        }

        SceneFunctionCategoryDO category = this.getCategoryById(categoryId);
        if (null != category) {
            if (!Strings.isNullOrEmpty(request.getName())) {
                category.setName(request.getName());
            }
            if (!Strings.isNullOrEmpty(request.getParentId())) {
                category.setParentId(request.getParentId());
            }
            if (null != request.getPhase()) {
                category.setPhase(request.getPhase());
            }
            if (null != request.getType()) {
                category.setType(request.getType());
            }
            if (null != request.getIsDelete()) {
                category.setIsDelete(request.getIsDelete());
            }

            category.setSupportScopeTypeList(request.getSupportScopeTypes());

            boolean result = sceneFunctionCategoryRepository.update(category);
            if (!result) {
                throw new ChaosException(CommonErrorCode.B_UPDATE_APP_CATEGORY,
                    "Update scene function category failed.");
            }
        } else {
            throw new ChaosException(CommonErrorCode.B_MINIAPP_CATEGORY_NOT_EXIST);
        }
    }

    @Override
    public void deleteCategory(String categoryId) throws ChaosException {
        SceneFunctionCategoryUpdateRequest request = new SceneFunctionCategoryUpdateRequest();
        request.setCategoryId(categoryId);
        request.setIsDelete(true);
        updateCategory(request);
    }

    private List<SceneFunctionCategoryDO> buildTrees(List<SceneFunctionCategoryDO> categories) {
        return categories.stream()
            .filter(category -> Strings.isNullOrEmpty(category.getParentId()))
            .peek(category -> buildTree(categories, category))
            .collect(Collectors.toList());
    }

    private void buildTree(List<SceneFunctionCategoryDO> categories, SceneFunctionCategoryDO parentNode) {
        List<SceneFunctionCategoryDO> children = categories.stream()
            .filter(category -> Objects.equals(category.getParentId(), parentNode.getCategoryId()))
            .collect(Collectors.toList());
        if (!CollectionUtil.isNullOrEmpty(children)) {
            parentNode.setChildren(children);
            children.forEach(child -> buildTree(categories, child));
        }
    }

    @Getter
    @RequiredArgsConstructor(staticName = "wrap")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    private static class CategoryWrapper {

        Integer phase;
        SceneFunctionCategoryDO category;

    }

}
