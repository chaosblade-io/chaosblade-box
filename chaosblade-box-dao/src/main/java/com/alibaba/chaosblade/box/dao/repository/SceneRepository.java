package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.util.ArrayUtil;
import com.alibaba.chaosblade.box.dao.mapper.SceneMapper;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
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
import java.util.List;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
public class SceneRepository implements IRepository<Long, SceneDO> {

    @Resource
    private SceneMapper sceneMapper;

    @Override
    public Optional<SceneDO> findById(Long id) {
        return Optional.ofNullable(sceneMapper.selectById(id));
    }

    public Optional<SceneDO> findBySceneId(String sceneId) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<SceneDO>()
                .eq("scene_id", sceneId);

        return Optional.ofNullable(sceneMapper.selectOne(queryWrapper));
    }

    public Optional<SceneDO> findByCode(String code) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<SceneDO>()
                .eq("code", code)
                .eq("is_delete", false);

        return Optional.ofNullable(sceneMapper.selectOne(queryWrapper));
    }

    public Optional<List<SceneDO>> findByUser(ChaosUser user) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", user.getUserId());

        queryWrapper.eq("is_delete", false);

        return Optional.ofNullable(sceneMapper.selectList(queryWrapper));
    }

    public Optional<List<SceneDO>> findByUserIdOrPublic(String empId) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<SceneDO>()
                .eq("is_delete", false)
                .and(wrapper -> wrapper.eq("user_id", empId)
                                       .or()
                                       .eq("is_public", 1));

        return Optional.ofNullable(sceneMapper.selectList(queryWrapper));
    }

    @Override
    public boolean update(SceneDO scene) {
        int rows = sceneMapper.updateById(scene);
        return rows > 0;
    }

    public boolean updateBySceneId(SceneDO scene) {
        UpdateWrapper<SceneDO> updateWrapper = new UpdateWrapper<SceneDO>()
                .eq("scene_id", scene.getSceneId())
                .set(null != scene.getState(), "runState", scene.getState())
                .set(!Strings.isNullOrEmpty(scene.getCode()), "code", scene.getCode())
                .set(!Strings.isNullOrEmpty(scene.getName()), "name", scene.getName())
                .set(!Strings.isNullOrEmpty(scene.getDescription()), "description", scene.getDescription())
                .set(!Strings.isNullOrEmpty(scene.getVersion()), "version", scene.getVersion())
                .set(!Strings.isNullOrEmpty(scene.getUserId()), "user_id", scene.getUserId());

        int rows = sceneMapper.update(scene, updateWrapper);
        return rows > 0;
    }

    public boolean updateByCode(SceneDO scene) {
        UpdateWrapper<SceneDO> updateWrapper = new UpdateWrapper<SceneDO>()
                .eq("code", scene.getCode())
                .eq("is_delete", false);

        int rows = sceneMapper.update(scene, updateWrapper);
        return rows > 0;
    }

    @Override
    public boolean add(SceneDO scene) {
        if (null == scene.getSceneId()) {
            scene.setSceneId(IdWorker.getIdStr());
        }
        int rows = sceneMapper.insert(scene);
        return rows > 0;
    }

    public Optional<List<SceneDO>> search(String keyword, Integer state) {
        return Optional.of(sceneMapper.search(keyword, state));
    }

    public Optional<List<SceneDO>> queryAll() {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<SceneDO>()
                .eq("is_delete", false);

        return Optional.of(sceneMapper.selectList(queryWrapper));
    }

    public boolean deleteBySceneId(String sceneId) {
        if (Strings.isNullOrEmpty(sceneId)) {
            return false;
        }

        Optional<SceneDO> existOptional = this.findBySceneId(sceneId);
        if (!existOptional.isPresent()) {
            return false;
        }

        SceneDO exist = existOptional.get();
        exist.setIsDelete(true);

        UpdateWrapper<SceneDO> updateWrapper = new UpdateWrapper<SceneDO>()
                .eq("scene_id", sceneId);

        int rows = sceneMapper.update(exist, updateWrapper);
        return rows > 0;
    }

    public PageableResponse<SceneDO> getScenes(PageableQueryWrapper<SceneQueryRequest> pageableQueryWrapper) {
        int pageSize = pageableQueryWrapper.pageSize();
        int pageNo = pageableQueryWrapper.pageNumber();

        IPage<SceneDO> result = sceneMapper.selectPage(
                new Page<>(pageableQueryWrapper.pageNumber(), pageableQueryWrapper.pageSize()),
                buildQueryWrapper(pageableQueryWrapper)
        );

        if (null == result) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }

        List<SceneDO> scenes = result.getRecords();

        return PageableResponse.of(pageNo, pageSize, scenes)
                               .pages(result.getPages())
                               .total(result.getTotal());
    }

    private QueryWrapper<SceneDO> buildQueryWrapper(PageableQueryWrapper<SceneQueryRequest> pageableQueryWrapper) {
        SceneQueryRequest query = pageableQueryWrapper.query();

        return buildQueryWrapper(query)
                .orderBy(!ArrayUtil.isNullOrEmpty(pageableQueryWrapper.orderBy()), pageableQueryWrapper.asc(), pageableQueryWrapper.orderBy());
    }

    private QueryWrapper<SceneDO> buildQueryWrapper(SceneQueryRequest query) {
        QueryWrapper<SceneDO> queryWrapper = new QueryWrapper<>();

        if (!Strings.isNullOrEmpty(query.getSceneId())) {
            queryWrapper.eq("scene_id", query.getSceneId());
        }

        ChaosUser user = query.getUser();
        if (null != user) {
            queryWrapper.eq("user_id", user.getUserId());

        }

        queryWrapper.eq("is_delete", query.getIsDelete());

        return queryWrapper;


    }

}
