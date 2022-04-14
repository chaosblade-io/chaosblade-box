package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentTagMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentTagRelationDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTagQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTagRepository
        implements IRepository<String, ExperimentTagRelationDO>, IQuery<ExperimentTagRelationDO, ExperimentTagQuery> {

    @Autowired
    private ExperimentTagMapper experimentTagMapper;

    @Override
    public Optional<ExperimentTagRelationDO> findById(String s) {
        return Optional.ofNullable(experimentTagMapper.selectById(s));
    }

    @Override
    public boolean update(ExperimentTagRelationDO experimentTagRelationDO) {
        return experimentTagMapper.updateById(experimentTagRelationDO) > 0;
    }

    @Override
    public boolean add(ExperimentTagRelationDO experimentTagRelationDO) {
        return experimentTagMapper.insert(experimentTagRelationDO) > 0;
    }

    @Override
    public List<ExperimentTagRelationDO> find(ExperimentTagQuery experimentTagQuery) {
        return experimentTagMapper.selectList(buildQuery(experimentTagQuery));
    }

    public List<String> findDistinctTags(List<String> experimentIds) {
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setExperimentIds(experimentIds);
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = buildQuery(experimentTagQuery);
        queryWrapper.select("distinct tag_name");
        return experimentTagMapper.selectList(queryWrapper).stream().map(ExperimentTagRelationDO::getTagName).collect(
                Collectors.toList());
    }

    public List<String> findDistinctTagsByUserId(String userId, Integer tagType) {
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setUserId(userId);
        experimentTagQuery.setTagType(tagType);
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = buildQuery(experimentTagQuery);
        queryWrapper.select("distinct tag_name");
        return experimentTagMapper.selectList(queryWrapper).stream().map(ExperimentTagRelationDO::getTagName).collect(
                Collectors.toList());
    }

    public List<String> findUserAndAdminDistinctTagsByUserId(String userId, Integer tagType) {
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.eq(tagType != null, "type",
                tagType);
        queryWrapper.select("distinct tag_name");
        return experimentTagMapper.selectList(queryWrapper).stream().map(ExperimentTagRelationDO::getTagName).collect(
                Collectors.toList());
    }

    @Override
    public int count(ExperimentTagQuery experimentTagQuery) {
        return experimentTagMapper.selectCount(buildQuery(experimentTagQuery));
    }

    @Override
    public int delete(ExperimentTagQuery experimentTagQuery) {
        return experimentTagMapper.delete(buildQuery(experimentTagQuery));
    }

    public int deleteByRelationId(String relationId, Integer tagType) {
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setRelationId(relationId);
        experimentTagQuery.setTagType(tagType);
        return delete(experimentTagQuery);
    }

    public List<ExperimentTagRelationDO> findByExperimentId(String experimentId) {
        if (experimentId == null) {
            return new ArrayList<>();
        }
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setRelationId(experimentId);
        experimentTagQuery.setTagType(TagDO.TAG_TYPE_EXPERIMENT);
        return find(experimentTagQuery);
    }

    public List<ExperimentTagRelationDO> findByExpertiseId(String expertiseId) {
        if (expertiseId == null) {
            return new ArrayList<>();
        }
        ExperimentTagQuery experimentTagQuery = new ExperimentTagQuery();
        experimentTagQuery.setRelationId(expertiseId);
        experimentTagQuery.setTagType(TagDO.TAG_TYPE_EXPERTISE);
        return find(experimentTagQuery);
    }

    private QueryWrapper<ExperimentTagRelationDO> buildQuery(ExperimentTagQuery experimentTagQuery) {
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(experimentTagQuery.getRelationId() != null, "experiment_id",
                experimentTagQuery.getRelationId());
        queryWrapper.eq(experimentTagQuery.getTagId() != null, "tag_id",
                experimentTagQuery.getTagId());
        queryWrapper.eq(experimentTagQuery.getUserId() != null, "user_id", experimentTagQuery.getUserId());
        queryWrapper.eq(experimentTagQuery.getTagType() != null, "type",
                experimentTagQuery.getTagType());
        queryWrapper.likeRight(!Strings.isNullOrEmpty(experimentTagQuery.getKey()), "tag_name",
                experimentTagQuery.getKey());
        queryWrapper.in(!CollectionUtil.isNullOrEmpty(experimentTagQuery.getTagNames()), "tag_name",
                experimentTagQuery.getTagNames());
        queryWrapper.in(!CollectionUtil.isNullOrEmpty(experimentTagQuery.getExperimentIds()), "experiment_id",
                experimentTagQuery.getExperimentIds());
        return queryWrapper;
    }

    public List<ExperimentTagRelationDO> findByExperimentIdIn(List<String> experimentIds) {
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("experiment_id", experimentIds);
        queryWrapper.eq("type", TagDO.TAG_TYPE_EXPERTISE);
        return experimentTagMapper.selectList(queryWrapper);
    }

    public List<ExperimentTagRelationDO> findByTagNameIsNull() {
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("tag_name");
        return experimentTagMapper.selectList(queryWrapper);
    }

    public List<ExperimentTagRelationDO> findByTagTypeAndUserIdIsNull(Integer tagType) {
        QueryWrapper<ExperimentTagRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("user_id");
        queryWrapper.eq("type", tagType);
        return experimentTagMapper.selectList(queryWrapper);
    }

    public IPage<ExperimentTagRelationDO> findByTagId(String tagId, int pageNo, int pageSize) {
        return experimentTagMapper.selectPage(MyBatisUtil.getPage(pageNo, pageSize),
                new QueryWrapper<ExperimentTagRelationDO>().eq(tagId != null, "tag_id", tagId));
    }

    public List<ExperimentTagRelationDO> findByTagId(String tagId) {
        return experimentTagMapper.selectList(new QueryWrapper<ExperimentTagRelationDO>().eq("tag_id", tagId));
    }

}
