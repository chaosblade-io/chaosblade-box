package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.common.infrastructure.util.ArrayUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.DeviceMapper;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//import com.google.common.base.Predicate;

/**
 * @author sunju
 *
 */
@Repository
public class DeviceRepository extends ServiceImpl<DeviceMapper, DeviceDO> implements IRepository<String, DeviceDO> {

    @Resource
    @Getter
    private DeviceMapper deviceMapper;

    public PageableResponse<DeviceDO> getAliveDevices(PageableQueryWrapper<CloudDeviceQuery> pageableQueryWrapper) {
        int pageSize = pageableQueryWrapper.pageSize();
        int pageNo = pageableQueryWrapper.pageNumber();
        IPage<DeviceDO> result = deviceMapper.selectPage(
                MyBatisUtil.getPage(pageNo, pageSize),
                buildQueryWrapper(pageableQueryWrapper)
        );
        if (null == result) {
            return PageableResponse.of(pageNo, pageSize, Lists.newArrayList());
        }
        return PageableResponse.of(pageNo, pageSize, result.getRecords())
                .pages(result.getPages())
                .total(result.getTotal());
    }

    public PageableResponse<DeviceDO> getAliveDevicesByKeyAndTags(
            PageableQueryWrapper<CloudDeviceQuery> pageableQueryWrapper) {
        int size = pageableQueryWrapper.pageSize();
        int page = pageableQueryWrapper.pageNumber();
        CloudDeviceQuery query = pageableQueryWrapper.query();
        IPage<DeviceDO> result = null;
        //这里需要区分，因为目前k8s不支持标签检索
        if (query.getScopeType() == ScopeTypeEnum.K8s) {
            result = deviceMapper.selectPage(
                    MyBatisUtil.getPage(page, size),
                    buildQueryWrapper(pageableQueryWrapper)
            );
        } else {
            if (!Strings.isNullOrEmpty(query.getKey())) {
                query.setKey("%" + query.getKey() + "%");
            }
            result = deviceMapper.selectHostPageByKeyAndTags(MyBatisUtil.getPage(page, size), query);
        }

        if (null == result) {
            return PageableResponse.of(page, size, Lists.newArrayList());
        }
        return PageableResponse.of(page, size, result.getRecords())
                .pages(result.getPages())
                .total(result.getTotal());
    }

    public List<DeviceDO> getAliveDevices(CloudDeviceQuery query) {
        return deviceMapper.selectList(buildQueryWrapper(query));
    }

    private QueryWrapper<DeviceDO> buildQueryWrapper(PageableQueryWrapper<CloudDeviceQuery> pageableQueryWrapper) {
        CloudDeviceQuery query = pageableQueryWrapper.query();
        if (query.getScopeType() == ScopeTypeEnum.K8s) {
            pageableQueryWrapper.groupBy(new String[] {"cluster_id"});
        }
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

    private QueryWrapper<DeviceDO> buildQueryWrapper(CloudDeviceQuery query) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<>();
        if (!Strings.isNullOrEmpty(query.getKey())) {
            if (query.getScopeType() == ScopeTypeEnum.K8s) {
                queryWrapper.nested(nestedWrapper ->
                        nestedWrapper.or(wrapper -> wrapper.like("cluster_name", query.getKey()))
                                .or(wrapper -> wrapper.like("device_name", query.getKey()))
                );
            } else {
                queryWrapper.nested(nestedWrapper ->
                        nestedWrapper.or(wrapper -> wrapper.like("public_ip", query.getKey()))
                                .or(wrapper -> wrapper.like("private_ip", query.getKey()))
                                .or(wrapper -> wrapper.like("device_name", query.getKey()))
                );
            }
        }
        if (query.getIsExperiment() != null) {
            queryWrapper.eq("experiment_status", query.getIsExperiment());
        }
        if (!Strings.isNullOrEmpty(query.getUserId())) {
            queryWrapper.eq("user_id", query.getUserId());
        }
        if (!Strings.isNullOrEmpty(query.getNamespace())) {
            queryWrapper.eq("namespace", query.getNamespace());
        }
        if (query.getScopeType() != null) {
            queryWrapper.in(
                    "install_mode",
                    query.getScopeType()
                            .getAsList()
                            .stream()
                            .map(InstallMode::name)
                            .collect(Collectors.toList())
            );
        }
        queryWrapper.in("device_type", DeviceType.HOST_POD.getType(), DeviceType.HOST.getType())
                .eq("status", 2)
                .isNotNull("version");
        if (query.getLimit() != null) {
            queryWrapper.last(" limit " + query.getLimit());
        }
        return queryWrapper;
    }

    public List<DeviceDO> findByConfigurationIdIn(List<String> configurationIds) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.in("configuration_id", configurationIds);
        return deviceMapper.selectList(queryWrapper);
    }

    public DeviceDO findByConfigurationId(String configurationId) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("configuration_id", configurationId);
        return deviceMapper.selectOne(queryWrapper);
    }

    public DeviceDO findByConfigurationIdAndUserId(String userId,
                                                   String configurationId) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("configuration_id", configurationId);
        queryWrapper.eq("user_id", userId);
        return deviceMapper.selectOne(queryWrapper);
    }

    public DeviceDO findByConfigurationIdAndUserIdNamespace(String userId,
                                                   String configurationId,
                                                   String namespace) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("configuration_id", configurationId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("namespace", namespace);
        return deviceMapper.selectOne(queryWrapper);
    }

    public DeviceDO findByPrivateIp(String ip) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("private_ip", ip).last("limit 1");
        return deviceMapper.selectOne(queryWrapper);
    }

    public DeviceDO findActiveByPrivateIp(String ip) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("private_ip", ip).
                eq("status", DeviceStatus.ONLINE.getStatus()).
                last("limit 1");
        return deviceMapper.selectOne(queryWrapper);
    }

    @Override
    public Optional<DeviceDO> findById(String s) {
        return Optional.ofNullable(findByConfigurationId(s));
    }

    @Override
    public boolean update(DeviceDO deviceDO) {
        deviceDO.setGmtModified(new Date());
        return updateById(deviceDO);
    }

    @Override
    public boolean add(DeviceDO deviceDO) {
        deviceDO.setGmtCreate(new Date());
        deviceDO.setGmtModified(new Date());
        return deviceMapper.insert(deviceDO) > 0;
    }

    public List<DeviceDO> findByConfigurationIds(List<String> configurationIds) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.in("configuration_id", configurationIds);
        return deviceMapper.selectList(queryWrapper);
    }

    public List<String> getOnlineClusterIdsByUserId(String userId,String namespace) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.select("distinct cluster_id");
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNotNull("cluster_id");
        queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
        queryWrapper.groupBy("cluster_id");
        if(!Strings.isNullOrEmpty(namespace)) {
            queryWrapper.eq("namespace",namespace);
        }
        return deviceMapper.selectList(queryWrapper).stream().map(DeviceDO::getClusterId).filter(
                (Predicate<String>)input -> !Strings.isNullOrEmpty(input)).collect(Collectors.toList());
    }

    public IPage<DeviceDO> getDevicesByClusterId(String clusterId, int page, int size) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("cluster_id", clusterId);
        queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
        return deviceMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
    }

    public List<DeviceDO> listDevicesByClusterId(String clusterId, boolean online) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("cluster_id", clusterId);
        if(online) {
            queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
        }
        return deviceMapper.selectList(queryWrapper);
    }

    public List<DeviceDO> getClusterDeviceByIdLimitOne(String clusterId) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("cluster_id", clusterId);
        queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
        queryWrapper.last("limit 1");
        return deviceMapper.selectList(queryWrapper);
    }

    public List<DeviceDO> getAliveDevicesByUserId(String uid) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("user_id", uid);
        queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
        return deviceMapper.selectList(queryWrapper);
    }

    public DeviceDO getDeviceByDeviceId(String uid, String namespace, String deviceId, Integer deviceType) {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>();
        queryWrapper.eq("user_id", uid);
        queryWrapper.eq("namespace", namespace);
        queryWrapper.eq("device_id", deviceId);
        queryWrapper.eq("device_type", deviceType);
        return deviceMapper.selectOne(queryWrapper);
    }

}
