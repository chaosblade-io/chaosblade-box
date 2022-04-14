package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ApplicationConfigurationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationConfigurationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author haibin.lhb
 * 
 * 
 */
@Component
public class ApplicationConfigurationRepository implements IRepository<Long, ApplicationConfigurationDO> {

    @Autowired
    private ApplicationConfigurationMapper applicationConfigurationMapper;

    public ApplicationConfigurationDO findByAliasAndAppId(String alias, String appId) {
        QueryWrapper<ApplicationConfigurationDO> applicationConfigurationDOQueryWrapper = new QueryWrapper<>();
        applicationConfigurationDOQueryWrapper.eq("alias", alias);
        applicationConfigurationDOQueryWrapper.eq("app_id", Long.valueOf(appId));
        return applicationConfigurationMapper.selectOne(applicationConfigurationDOQueryWrapper);
    }

    @Override
    public Optional<ApplicationConfigurationDO> findById(Long aLong) {
        return Optional.ofNullable(applicationConfigurationMapper.selectById(aLong));
    }

    @Override
    public boolean update(ApplicationConfigurationDO applicationConfigurationDO) {
        applicationConfigurationDO.setGmtModified(new Date());
        return applicationConfigurationMapper.updateById(applicationConfigurationDO) > 0;
    }

    @Override
    public boolean add(ApplicationConfigurationDO applicationConfigurationDO) {
        applicationConfigurationDO.setGmtModified(new Date());
        return applicationConfigurationMapper.insert(applicationConfigurationDO) > 0;
    }

    public List<ApplicationConfigurationDO> findByApplicationId(Long appId) {
        QueryWrapper<ApplicationConfigurationDO> applicationConfigurationDOQueryWrapper = new QueryWrapper<>();
        applicationConfigurationDOQueryWrapper.eq("app_id", appId);
        return applicationConfigurationMapper.selectList(applicationConfigurationDOQueryWrapper);
    }
}
