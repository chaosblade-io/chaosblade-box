package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.constant.ChaosFunctionConstant;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.ArrayUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.SceneAuthorizedMapper;
import com.alibaba.chaosblade.box.dao.model.SceneAuthorizedDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sunju
 * Date 2019-08-20
 */
@Slf4j
@Repository
public class SceneAuthorizedRepository implements IRepository<String, SceneAuthorizedDO> {

    @Resource
    private SceneAuthorizedMapper sceneAuthorizedMapper;

    @Override
    public Optional<SceneAuthorizedDO> findById(String authorizedId) {
        return Optional.ofNullable(
            sceneAuthorizedMapper.selectOne(new QueryWrapper<SceneAuthorizedDO>().eq("authorized_id", authorizedId))
        );
    }

    @Override
    public boolean update(SceneAuthorizedDO sceneAuthorized) {
        return sceneAuthorizedMapper.update(
            sceneAuthorized,
            new UpdateWrapper<SceneAuthorizedDO>().eq("authorized_id", sceneAuthorized.getAuthorizedId())
        ) > 0;
    }

    public boolean updateByAuthorizedId(SceneAuthorizedUpdateRequest request) {
        if (Strings.isNullOrEmpty(request.getAuthorizedId()) && CollectionUtil.isNullOrEmpty(
            request.getAuthorizedIds())) {
            return false;
        }

        SceneAuthorizedDO sceneAuthorized = new SceneAuthorizedDO();
        if (!Strings.isNullOrEmpty(request.getFunctionName())) {
            sceneAuthorized.setFunctionName(request.getFunctionName());
        }
        if (!Strings.isNullOrEmpty(request.getFunctionCode())) {
            sceneAuthorized.setFunctionCode(request.getFunctionCode());
        }
        if (null != request.getEnabled()) {
            sceneAuthorized.setEnabled(request.getEnabled());
        }
        if (null != request.getPhase()) {
            sceneAuthorized.setPhase(request.getPhase());
        }
        if (null != request.getIsDelete()) {
            sceneAuthorized.setIsDelete(request.getIsDelete());
        }
        if (null != request.getSource()) {
            sceneAuthorized.setSource(request.getSource());
        }
        if (null != request.getSupportScopeTypes()) {
            sceneAuthorized.setSupportScopeTypeList(request.getSupportScopeTypes());
        }
        sceneAuthorized.setIsPublic(request.getIsPublic());
        UpdateWrapper<SceneAuthorizedDO> updateWrapper = new UpdateWrapper<>();
        if (!CollectionUtil.isNullOrEmpty(request.getAuthorizedIds())) {
            updateWrapper.in("authorized_id", request.getAuthorizedIds());
        } else {
            updateWrapper.eq("authorized_id", request.getAuthorizedId());
        }

        return sceneAuthorizedMapper.update(sceneAuthorized, updateWrapper) > 0;
    }

    public boolean updateByFunctionId(SceneAuthorizedUpdateRequest request) {
        if (Strings.isNullOrEmpty(request.getFunctionId()) && CollectionUtil.isNullOrEmpty(request.getFunctionIds())) {
            return false;
        }

        SceneAuthorizedDO sceneAuthorized = new SceneAuthorizedDO();
        if (!Strings.isNullOrEmpty(request.getFunctionName())) {
            sceneAuthorized.setFunctionName(request.getFunctionName());
        }
        if (!Strings.isNullOrEmpty(request.getFunctionCode())) {
            sceneAuthorized.setFunctionCode(request.getFunctionCode());
        }
        if (null != request.getEnabled()) {
            sceneAuthorized.setEnabled(request.getEnabled());
        }
        if (null != request.getPhase()) {
            sceneAuthorized.setPhase(request.getPhase());
        }
        if (null != request.getIsDelete()) {
            sceneAuthorized.setIsDelete(request.getIsDelete());
        }
        if (null != request.getSource()) {
            sceneAuthorized.setSource(request.getSource());
        }
        if (null != request.getSupportScopeTypes()) {
            sceneAuthorized.setSupportScopeTypeList(request.getSupportScopeTypes());
        }

        UpdateWrapper<SceneAuthorizedDO> updateWrapper = new UpdateWrapper<>();
        if (!CollectionUtil.isNullOrEmpty(request.getFunctionIds())) {
            updateWrapper.in("function_id", request.getFunctionIds());
        } else {
            updateWrapper.eq("function_id", request.getFunctionId());
        }

        return sceneAuthorizedMapper.update(sceneAuthorized, updateWrapper) > 0;
    }

    @Override
    public boolean add(SceneAuthorizedDO sceneAuthorized) {
        return sceneAuthorizedMapper.insert(sceneAuthorized) > 0;
    }

    public List<SceneAuthorizedDO> getAuthorizedRecords(SceneAuthorizedQueryRequest query) {
        return sceneAuthorizedMapper.selectList(buildQueryWrapper(query));
    }

    public PageableResponse<SceneAuthorizedDO> getAuthorizedRecords(
        PageableQueryWrapper<SceneAuthorizedQueryRequest> pageableQueryWrapper) {
        int pageSize = pageableQueryWrapper.pageSize();
        int pageNo = pageableQueryWrapper.pageNumber();

        IPage<SceneAuthorizedDO> result = sceneAuthorizedMapper.selectPage(
            new Page<>(pageableQueryWrapper.pageNumber(), pageableQueryWrapper.pageSize()),
            buildQueryWrapper(pageableQueryWrapper)
        );

        if (null == result) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }

        List<SceneAuthorizedDO> sceneAuthorizedList = result.getRecords();

        return PageableResponse.of(pageNo, pageSize, sceneAuthorizedList)
            .pages(result.getPages())
            .total(result.getTotal());
    }

    public List<SceneAuthorizedDO> getAuthorizedRecordsGroupBy(SceneAuthorizedQueryRequest query) {
        List<SceneAuthorizedDO> sceneAuthorizedDoList = sceneAuthorizedMapper.selectList(buildQueryWrapper(query)
                .select("max(id) id")
                .groupBy(
                        "function_id"
                ));
        if (CollectionUtil.isNullOrEmpty(sceneAuthorizedDoList)) {
            return Lists.newArrayList();
        }
        List<Long> collect = sceneAuthorizedDoList.stream().map(SceneAuthorizedDO::getId).filter(Objects::nonNull).collect(Collectors.toList());
        return sceneAuthorizedMapper.selectList(new QueryWrapper<SceneAuthorizedDO>().lambda().in(SceneAuthorizedDO::getId, collect));
    }

    private QueryWrapper<SceneAuthorizedDO> buildQueryWrapper(
        PageableQueryWrapper<SceneAuthorizedQueryRequest> pageableQueryWrapper) {
        SceneAuthorizedQueryRequest query = pageableQueryWrapper.query();

        return buildQueryWrapper(query)
            .groupBy(
                null != pageableQueryWrapper.groupBy() && pageableQueryWrapper.groupBy().length > 0,
                pageableQueryWrapper.groupBy()
            )
            .orderBy(
                !ArrayUtil.isNullOrEmpty(pageableQueryWrapper.orderBy()),
                pageableQueryWrapper.asc(),
                pageableQueryWrapper.orderBy()
            );
    }

    private QueryWrapper<SceneAuthorizedDO> buildQueryWrapper(SceneAuthorizedQueryRequest query) {
        QueryWrapper<SceneAuthorizedDO> queryWrapper = new QueryWrapper<>();

        if (!Strings.isNullOrEmpty(query.getSearchKey())) {
            queryWrapper.nested(nestedQueryWrapper -> nestedQueryWrapper
                .or(orWrapper -> orWrapper.like("function_name", query.getSearchKey()))
                .or(orWrapper -> orWrapper.like("function_code", query.getSearchKey()))
            );
        }
        if (!Strings.isNullOrEmpty(query.getFunctionId())) {
            queryWrapper.eq("function_id", query.getFunctionId());
        }
        if (!CollectionUtil.isNullOrEmpty(query.getFunctionIds())) {
            queryWrapper.in("function_id", query.getFunctionIds());
        }
        if (null != query.getEnabled()) {
            queryWrapper.and(andQueryWrapper -> {
                if (null == query.getEnabled()) {
                    return andQueryWrapper;
                }
                return andQueryWrapper.eq("enabled", query.getEnabled());
            });
        }
        if (null != query.getPermission()) {
            queryWrapper.apply(String.format("(permission & %d) = %d", query.getPermission(), query.getPermission()));
        }
        if (null != query.getPhase()) {
            queryWrapper.apply(String.format("(phase & %d) > 0", query.getPhase()));
        }
        if (null != query.getSource()) {
            queryWrapper.eq("source", query.getSource());
        }
        if (!Strings.isNullOrEmpty(query.getGrantFrom())) {
            queryWrapper.eq("grant_from", query.getGrantFrom());
        }
        if (null != query.getSupportScopeType()) {
            if (Objects.equals(query.getSupportScopeType(), ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_HOST)) {
                queryWrapper.eq("support_host", true);
            }
            if (Objects.equals(query.getSupportScopeType(), ChaosFunctionConstant.SUPPORT_SCOPE_TYPE_K8S)) {
                queryWrapper.eq("support_k8s", true);
                appendK8sResourceQuery(queryWrapper, query.getK8sResourceType());
            }
        }

        queryWrapper.eq("is_delete", Boolean.TRUE.equals(query.getIsDelete()));
        //默认是true
        if (null == query.getIsPublic()) {
            queryWrapper.eq("is_public", true);
        }
        if (!Strings.isNullOrEmpty(query.getGrantTo())) {
            queryWrapper.nested(nestedQueryWrapper -> {
                if (Boolean.TRUE.equals(query.getIsPublic())) {
                    if (Strings.isNullOrEmpty(query.getGrantTo())) {
                        return nestedQueryWrapper.eq("is_public", true);
                    }
                    return nestedQueryWrapper
                        .or(wrapper -> wrapper.eq("grant_to", query.getGrantTo()).eq("is_public", false))
                        .or(wrapper -> wrapper.eq("is_public", true));
                }
                return nestedQueryWrapper.eq("grant_to", query.getGrantTo()).eq("is_public", false);
            });
        }
        return queryWrapper;
    }

    private void appendK8sResourceQuery(
        QueryWrapper<SceneAuthorizedDO> queryWrapper, Integer k8sResourceType) {
        if (k8sResourceType == null) { return; }
        if (ChaosFunctionConstant.K8_RESOURCE_TYPE_POD == k8sResourceType) {
            queryWrapper.and(wrapper -> wrapper.likeRight("function_code", "chaos.pod-").or()
                .like("function_code", "litmuschaos.pod-")
            );
        }
        if (ChaosFunctionConstant.K8_RESOURCE_TYPE_NODE == k8sResourceType) {
            queryWrapper.and(wrapper -> wrapper.likeRight("function_code", "chaos.node-").or()
                .like("function_code", "litmuschaos.node-")
            );
        }
        if (ChaosFunctionConstant.K8_RESOURCE_TYPE_CONTAINER == k8sResourceType) {
            queryWrapper.and(wrapper -> wrapper.likeRight("function_code", "chaos.container-").or()
                .like("function_code", "litmuschaos.container-")
                .or().in("function_code", "chaos.pod-pod.delete")
            );
        }
    }

    //增加pod删除
    //pod扩容这几个

}
