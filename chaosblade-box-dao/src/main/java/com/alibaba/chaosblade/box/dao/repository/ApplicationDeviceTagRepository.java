package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationDeviceMapper;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationDeviceTagMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceTagDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sunpeng
 *
 *
 */
@Component
public class ApplicationDeviceTagRepository implements IRepository<Long, ApplicationDeviceTagDO> {

    @Autowired
        private ApplicationDeviceMapper applicationDeviceMapper;

    @Autowired
    private ApplicationDeviceTagMapper applicationDeviceTagMapper;


    @Override
    public Optional<ApplicationDeviceTagDO> findById(Long aLong) {
        return Optional.of(applicationDeviceTagMapper.selectById(aLong));
    }

    @Override
    public boolean update(ApplicationDeviceTagDO applicationDeviceTagDO) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", applicationDeviceTagDO.getId());
        return applicationDeviceTagMapper.update(applicationDeviceTagDO, queryWrapper) > 0;
    }

    @Override
    public boolean add(ApplicationDeviceTagDO applicationDeviceTagDO) {
        return applicationDeviceTagMapper.insert(applicationDeviceTagDO) > 0;
    }

    public boolean deleteDeviceTag(String configurationId, String appId, String groupName, String userId) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("configuration_id", configurationId);
        queryWrapper.eq("app_id", appId);
        queryWrapper.eq("group_name", groupName);
        queryWrapper.eq("user_id", userId);
        return applicationDeviceTagMapper.delete(queryWrapper) > 0;
    }

    public List<ApplicationDeviceTagDO> getTagsByAppIdAndConfigurationIdAndUserId(Long appId,String configurationId,String userId) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("configuration_id", configurationId);
        queryWrapper.eq("app_id", appId);
        queryWrapper.eq("user_id", userId);
        return applicationDeviceTagMapper.selectList(queryWrapper);
    }

    public int countByAppIdAndPrivateIpAndUserIdAndTagName(Long appId,String groupName, String configurationId,String userId,String tagName) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("configuration_id", configurationId);
        queryWrapper.eq("app_id", appId);
        queryWrapper.eq("group_name", groupName);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("tag_name", tagName);
        return applicationDeviceTagMapper.selectCount(queryWrapper);
    }

    public List<String> listApplicationGroupTags(Long appId,List<String> groupNames, String userId,String key) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        if(null != appId) {
            queryWrapper.eq("app_id", appId);
        }
        if(null != groupNames && !CollectionUtil.isNullOrEmpty(groupNames)) {
            queryWrapper.in("group_name", groupNames);
        }
        if (!Strings.isNullOrEmpty(key)) {
            queryWrapper.like("tag_name", key);
        }
        queryWrapper.eq("user_id", userId);
        queryWrapper.select("distinct tag_name");
        return applicationDeviceTagMapper.selectList(queryWrapper).stream().map(ApplicationDeviceTagDO::getTagName).collect(
                Collectors.toList());
    }

    public List<String> listApplicationGroupTagsByConfigurationIds(List<String> configurationIds, String userId,String key) {
        QueryWrapper<ApplicationDeviceTagDO> queryWrapper = new QueryWrapper<>();
        if(null != configurationIds && !CollectionUtil.isNullOrEmpty(configurationIds)) {
            queryWrapper.in("configuration_id", configurationIds);
        }
        if (!Strings.isNullOrEmpty(key)) {
            queryWrapper.like("tag_name", key);
        }
        queryWrapper.eq("user_id", userId);
        queryWrapper.select("distinct tag_name");
        return applicationDeviceTagMapper.selectList(queryWrapper).stream().map(ApplicationDeviceTagDO::getTagName).collect(
                Collectors.toList());
    }

}
