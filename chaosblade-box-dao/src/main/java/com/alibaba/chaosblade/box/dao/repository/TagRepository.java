package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.TagMapper;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.query.TagQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
public class TagRepository implements IRepository<String, TagDO>, IQuery<TagDO, TagQuery> {

    @Resource
    private TagMapper tagMapper;

    @Override
    public Optional<TagDO> findById(String id) {
        return Optional.ofNullable(tagMapper.selectById(id));
    }

    @Override
    public boolean update(TagDO tag) {
        int rows = tagMapper.updateById(tag);
        return rows > 0;
    }

    @Override
    public boolean add(TagDO tag) {
        int rows = tagMapper.insert(tag);
        return rows > 0;
    }

    public List<TagDO> findByIdsIn(Collection<String> ids) {
        if (CollectionUtil.isNullOrEmpty(ids)) { return new ArrayList<>(); }
        return tagMapper.selectList(new QueryWrapper<TagDO>().in("tag_id", ids));
    }

    public List<TagDO> findByExpertiseIdsIn(Collection<String> ids) {
        if (CollectionUtil.isNullOrEmpty(ids)) { return new ArrayList<>(); }
        return tagMapper.selectList(new QueryWrapper<TagDO>().in("tag_id", ids));
    }

    public Optional<TagDO> findByName(String name) {
        Preconditions.checkArgument(name != null);
        return Optional.ofNullable(tagMapper.selectOne(new QueryWrapper<TagDO>().eq("name", name)));
    }

    public Optional<TagDO> findByUserIdNameAndType(String userId, String name, Integer tagType) {
        return Optional.ofNullable(tagMapper.selectOne(new QueryWrapper<TagDO>().eq("user_id", userId).eq("name", name)
            .eq("type", tagType)
            .last("limit 1")));
    }

    @Override
    public List<TagDO> find(TagQuery tagQuery) {
        return tagMapper.selectList(buildQuery(tagQuery));
    }

    @Override
    public int count(TagQuery tagQuery) {
        return tagMapper.selectCount(buildQuery(tagQuery));
    }

    @Override
    public int delete(TagQuery tagQuery) {
        return tagMapper.delete(buildQuery(tagQuery));
    }

    private QueryWrapper<TagDO> buildQuery(TagQuery tagQuery) {
        QueryWrapper<TagDO> queryWrapper = new QueryWrapper<TagDO>();
        queryWrapper.like(!Strings.isNullOrEmpty(tagQuery.getPartKey()), "name", tagQuery.getPartKey());
        queryWrapper.eq(!Strings.isNullOrEmpty(tagQuery.getUserId()), "user_id", tagQuery.getUserId());
        queryWrapper.in(!CollectionUtil.isNullOrEmpty(tagQuery.getTagNames()), "name", tagQuery.getTagNames());
        queryWrapper.eq(tagQuery.getTagType() != null, "type", tagQuery.getTagType());
        return queryWrapper;
    }
}
