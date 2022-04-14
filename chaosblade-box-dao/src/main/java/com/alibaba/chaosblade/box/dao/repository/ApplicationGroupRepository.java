package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ApplicationGroupMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationGroupRepository implements IRepository<Long, ApplicationGroupDO> {

    @Autowired
    private ApplicationGroupMapper applicationGroupMapper;

    @Override
    public Optional<ApplicationGroupDO> findById(Long aLong) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", aLong);
        return Optional.ofNullable(applicationGroupMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean update(ApplicationGroupDO applicationGroupDO) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", applicationGroupDO.getId());
        return applicationGroupMapper.update(applicationGroupDO, queryWrapper) > 0;
    }

    @Override
    public boolean add(ApplicationGroupDO applicationGroupDO) {
        return applicationGroupMapper.insert(applicationGroupDO) > 0;
    }

    public List<String> findApplicationGroupsByAppId(Long appId) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId);
        List<String> A = applicationGroupMapper.selectList(queryWrapper).stream().map(
            new Function<ApplicationGroupDO, String>() {
                @Override
                public String apply(ApplicationGroupDO applicationGroupDO) {
                    return applicationGroupDO.getName();
                }
            }).collect(Collectors.toList());
        return A;
    }

    public ApplicationGroupDO findApplicationGroup(Long appId, String groupName) {
        QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", groupName);
        queryWrapper.eq("app_id", appId);
        return applicationGroupMapper.selectOne(queryWrapper);
    }
}
