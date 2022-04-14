package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCategoryQueryRequest;
import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionCategoryMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionCategoryDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Author: sunju
 *
 * Date:   2019/11/12
 */
@Repository
public class SceneFunctionCategoryRepository implements IRepository<String, SceneFunctionCategoryDO> {

    @Resource
    private SceneFunctionCategoryMapper sceneFunctionCategoryMapper;

    @Override
    public Optional<SceneFunctionCategoryDO> findById(String categoryId) {
        return Optional.ofNullable(sceneFunctionCategoryMapper.selectOne(
            new QueryWrapper<SceneFunctionCategoryDO>()
                .eq("category_id", categoryId)
                .eq("is_delete", false)
        ));
    }

    @Override
    public boolean update(SceneFunctionCategoryDO sceneFunctionCategoryDO) {
        return sceneFunctionCategoryMapper.update(
            sceneFunctionCategoryDO,
            new UpdateWrapper<SceneFunctionCategoryDO>()
                .eq("category_id", sceneFunctionCategoryDO.getCategoryId())
        ) > 0;
    }

    @Override
    public boolean add(SceneFunctionCategoryDO sceneFunctionCategoryDO) {
        return sceneFunctionCategoryMapper.insert(sceneFunctionCategoryDO) > 0;
    }

    public List<SceneFunctionCategoryDO> getCategories() {
        return sceneFunctionCategoryMapper.selectList(
            new UpdateWrapper<SceneFunctionCategoryDO>()
                .eq("is_delete", false)
        );
    }

    public List<SceneFunctionCategoryDO> getSceneFunctionCategories(SceneFunctionCategoryQueryRequest queryRequest) {
        return sceneFunctionCategoryMapper.selectList(buildQueryWrapper(queryRequest));
    }

    private QueryWrapper<SceneFunctionCategoryDO> buildQueryWrapper(SceneFunctionCategoryQueryRequest queryRequest) {
        QueryWrapper<SceneFunctionCategoryDO> queryWrapper = new QueryWrapper<>();

        if (!Strings.isNullOrEmpty(queryRequest.getCategoryId())) {
            queryWrapper.eq("category_id", queryRequest.getCategoryId());
        }
        if (!Strings.isNullOrEmpty(queryRequest.getParentId())) {
            queryWrapper.eq("parent_id", queryRequest.getParentId());
        }
        if (null != queryRequest.getPhase()) {
            queryWrapper.apply(String.format("(phase & %d) > 0", queryRequest.getPhase()));
        }
        if (null != queryRequest.getType()) {
            queryWrapper.eq("type", queryRequest.getType());
        }
        Integer scopeType = queryRequest.getScopeType();
        Integer osType = queryRequest.getOsType();
        if (scopeType != null) {
            if (ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST == scopeType) {
                queryWrapper.eq("support_host", true);
            }
            if (ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S == scopeType) {
                queryWrapper.eq("support_k8s", true);
            }
            if (ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_CLOUD == scopeType) {
                queryWrapper.eq("support_cloud", true);
            }
        }
        if (osType != null) {
            queryWrapper.like("support_os_types", osType);
        }
        queryWrapper.eq("is_delete", false);
        return queryWrapper;
    }

}
