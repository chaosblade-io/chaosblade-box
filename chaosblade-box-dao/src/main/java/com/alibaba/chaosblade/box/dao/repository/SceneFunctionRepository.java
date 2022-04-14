package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.SceneFunctionMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: sunju
 *
 * Date:   2018/12/29
 */
@Repository
public class SceneFunctionRepository implements IRepository<Long, SceneFunctionDO> {

    @Resource
    private SceneFunctionMapper sceneFunctionMapper;

    @Override
    public Optional<SceneFunctionDO> findById(Long id) {
        return Optional.of(sceneFunctionMapper.selectById(id));
    }

    public List<SceneFunctionDO> findBySceneId(String sceneId) {
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .eq("scene_id", sceneId)
            .eq("is_delete", false);
        return sceneFunctionMapper.selectList(queryWrapper);
    }

    public Optional<SceneFunctionDO> findByFunctionId(String functionId) {
        if (Strings.isNullOrEmpty(functionId)) { return Optional.empty(); }
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .eq("function_id", functionId)
            .eq("is_delete", false);
        return Optional.ofNullable(sceneFunctionMapper.selectOne(queryWrapper));
    }

    public List<SceneFunctionDO> findByFunctionIds(List<String> functionIds) {
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .in("function_id", functionIds)
            .eq("is_delete", false)
            .isNull("parent_code");

        return sceneFunctionMapper.selectList(queryWrapper);
    }

    public List<SceneFunctionDO> findByFunctionCodes(List<String> codes) {
        if (CollectionUtil.isNullOrEmpty(codes)) {
            return Lists.newArrayList();
        }

        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .in("code", codes)
            .eq("is_delete", false);

        return sceneFunctionMapper.selectList(queryWrapper);
    }

    public List<SceneFunctionDO> findByFunctionCodeRightLike(String likeWord) {
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .likeRight("code", likeWord)
            .eq("is_delete", false);
        return sceneFunctionMapper.selectList(queryWrapper);
    }

    public Optional<SceneFunctionDO> findByCode(String code) {
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .eq("code", code)
            .eq("is_delete", false);

        return Optional.ofNullable(sceneFunctionMapper.selectOne(queryWrapper));
    }

    public Optional<Map<String, String>> findNamesByCode(List<String> codes) {
        if (CollectionUtil.isNullOrEmpty(codes)) {
            return Optional.of(new HashMap<>());
        }
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .select("name", "code")
            .in("code", codes)
            .eq("is_delete", false);
        Map<String, String> appDesc = new HashMap<>();
        List<SceneFunctionDO> sceneFunctionDOS = sceneFunctionMapper.selectList(queryWrapper);
        for (SceneFunctionDO sceneFunctionDO : sceneFunctionDOS) {
            appDesc.put(sceneFunctionDO.getCode(), sceneFunctionDO.getName());
        }
        return Optional.of(appDesc);
    }

    public List<SceneFunctionDO> findBySourceAndPhase(Integer source, int phaseFlag) {
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .eq("source", source)
            .eq("phase_flag", phaseFlag)
            .eq("is_delete", false);
        return sceneFunctionMapper.selectList(queryWrapper);
    }

    @Override
    public boolean update(SceneFunctionDO sceneFunction) {
        int rows = sceneFunctionMapper.updateById(sceneFunction);
        return rows > 0;
    }

    public boolean updateByFunctionId(SceneFunctionDO sceneFunction) {
        UpdateWrapper<SceneFunctionDO> updateWrapper = new UpdateWrapper<SceneFunctionDO>()
            .eq("function_id", sceneFunction.getFunctionId())
            .set(!Strings.isNullOrEmpty(sceneFunction.getCode()), "code", sceneFunction.getCode())
            .set(!Strings.isNullOrEmpty(sceneFunction.getName()), "name", sceneFunction.getName())
            .set(!Strings.isNullOrEmpty(sceneFunction.getDescription()), "description", sceneFunction.getDescription())
            .set("agent_required", sceneFunction.getAgentRequired());

        int rows = sceneFunctionMapper.update(sceneFunction, updateWrapper);
        return rows > 0;
    }

    public boolean updateByCode(SceneFunctionDO sceneFunction) {
        UpdateWrapper<SceneFunctionDO> updateWrapper = new UpdateWrapper<SceneFunctionDO>()
            .eq("code", sceneFunction.getCode())
            .eq("is_delete", false);

        int rows = sceneFunctionMapper.update(sceneFunction, updateWrapper);
        return rows > 0;
    }

    @Override
    public boolean add(SceneFunctionDO sceneFunction) {
        if (null == sceneFunction.getFunctionId()) {
            sceneFunction.setFunctionId(IdWorker.getIdStr());
        }
        int rows = sceneFunctionMapper.insert(sceneFunction);
        return rows > 0;
    }

    public boolean deleteByFunctionId(String functionId) {
        if (Strings.isNullOrEmpty(functionId)) {
            return false;
        }

        Optional<SceneFunctionDO> existOptional = this.findByFunctionId(functionId);
        if (!existOptional.isPresent()) {
            return false;
        }

        SceneFunctionDO exist = existOptional.get();
        exist.setIsDelete(true);

        UpdateWrapper<SceneFunctionDO> updateWrapper = new UpdateWrapper<SceneFunctionDO>()
            .eq("function_id", functionId);

        int rows = sceneFunctionMapper.update(exist, updateWrapper);
        return rows > 0;
    }

    public List<SceneFunctionDO> findInFunctionIds(List<String> functionIds) {
        if (CollectionUtil.isNullOrEmpty(functionIds)) {
            return null;
        }
        QueryWrapper<SceneFunctionDO> queryWrapper = new QueryWrapper<SceneFunctionDO>()
            .in("function_id", functionIds)
            .eq("is_delete", false);
        return sceneFunctionMapper.selectList(queryWrapper);
    }

    public boolean deleteInFunctionIds(List<String> functionIds) {
        if (CollectionUtil.isNullOrEmpty(functionIds)) {
            return false;
        }

        List<SceneFunctionDO> exists = this.findInFunctionIds(functionIds);
        int rows = 0;
        for (SceneFunctionDO exist : exists) {
            exist.setIsDelete(true);
            UpdateWrapper<SceneFunctionDO> updateWrapper = new UpdateWrapper<SceneFunctionDO>()
                .eq("function_id", exist.getFunctionId());

            rows += sceneFunctionMapper.update(exist, updateWrapper);
        }

        return rows > 0;
    }

    public List<SceneFunctionDO> findAvailableFunctions() {
        return sceneFunctionMapper.selectList(new QueryWrapper<SceneFunctionDO>().eq("is_delete", false));
    }

    public PageableResponse<SceneFunctionDO> findByPage(PageableQueryWrapper<SceneQueryRequest> pageableQueryWrapper) {
        SceneQueryRequest query = pageableQueryWrapper.query();

        int pageSize = pageableQueryWrapper.pageSize();
        int pageNo = pageableQueryWrapper.pageNumber();

        QueryWrapper<SceneFunctionDO> sceneFunctionDOQueryWrapper =  new QueryWrapper<SceneFunctionDO>()
                .eq("is_delete", false)
                .in("function_id", query.getFunctionIds())
                .orderByAsc("gmt_create")
                .isNull("parent_code");;

//        if(null != query.getOsType()) {
//            sceneFunctionDOQueryWrapper.like("support_os_types",query.getOsType().toString());
//        }
        if(null != query.getOsType() && query.getOsType() == 1) {
            sceneFunctionDOQueryWrapper.like("support_os_types",query.getOsType().toString());
        }
        if(null != query.getOsType() && query.getOsType() == 0) {
            sceneFunctionDOQueryWrapper.like("support_os_types",query.getOsType().toString());
            sceneFunctionDOQueryWrapper.and(
                    wrapper -> wrapper.like("support_os_types", query.getOsType().toString()).or().isNull("support_os_types"));
        }

//        if(null != query.getSupportScopeType()) {
//            sceneFunctionDOQueryWrapper.like("support_scope_types",query.getSupportScopeType().toString());
//        }

        IPage<SceneFunctionDO> result = sceneFunctionMapper.selectPage(
                new Page<>(pageableQueryWrapper.pageNumber(), pageableQueryWrapper.pageSize()),
                sceneFunctionDOQueryWrapper
        );
        if (null == result) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }

        List<SceneFunctionDO> sceneFunctionDOS = result.getRecords();

        return PageableResponse.of(pageNo, pageSize, sceneFunctionDOS)
                .pages(result.getPages())
                .total(result.getTotal());
    }


}
