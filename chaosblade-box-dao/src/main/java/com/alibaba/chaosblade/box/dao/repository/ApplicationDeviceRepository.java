package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.AppType;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationDeviceMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.query.ApplicationDeviceQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Repository
public class ApplicationDeviceRepository implements IRepository<String, ApplicationDeviceDO> {

	@Autowired
	private ApplicationDeviceMapper applicationDeviceMapper;

	/**
	 * 根据应用配置ID获取应用设备信息
	 *
	 * @param appConfigurationId
	 * @return
	 */
	@Override
	public Optional<ApplicationDeviceDO> findById(String appConfigurationId) {
		if (Strings.isNullOrEmpty(appConfigurationId)) { return Optional.empty(); }
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("configuration_id", appConfigurationId);
		return Optional.ofNullable(applicationDeviceMapper.selectOne(queryWrapper));
	}

	public Optional<ApplicationDeviceDO> findByAppIdAndAppConfigurationId(Long appId, String configurationId) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("configuration_id", configurationId);
		queryWrapper.eq("app_id", appId);
		return Optional.ofNullable(applicationDeviceMapper.selectOne(queryWrapper));
	}

	public List<ApplicationDeviceDO> findByConfigurationIdIn(Long appId, Set<String> appConfigurationId,
															 Integer deviceType) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("device_type", deviceType);
		queryWrapper.in("configuration_id", appConfigurationId);
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public List<ApplicationDeviceDO> findDeviceByLastHealthPingTimeLt(Long heartBeatExpireTimeInMilles) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		queryWrapper.last("last_health_ping_time < now()-" + heartBeatExpireTimeInMilles);
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	/**
	 * 将最近心跳时间超出指定间隔的机器设置为下线
	 *
	 * @param heartBeatExpireTimeInMilles 心跳最长有效期
	 * @return
	 */
	public Integer setStatusOffLineWhenHealthTimeIntervalGt(Long heartBeatExpireTimeInMilles) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		long time = System.currentTimeMillis() - heartBeatExpireTimeInMilles;
		queryWrapper.last(" and last_health_ping_time < " + time);
		ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
		applicationDeviceDO.setStatus(DeviceStatus.OFFLINE.getStatus());
		return applicationDeviceMapper.update(applicationDeviceDO, queryWrapper);
	}

	@Override
	public boolean update(ApplicationDeviceDO applicationDeviceDO) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("configuration_id", applicationDeviceDO.getConfigurationId());
		return applicationDeviceMapper.update(applicationDeviceDO, queryWrapper) > 0;
	}

	@Override
	public boolean add(ApplicationDeviceDO applicationDeviceDO) {
		return applicationDeviceMapper.insert(applicationDeviceDO) > 0;
	}

	public Integer countByAppId(Long appId) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectCount(queryWrapper);
	}

	/**
	 * 根据应用ID以及分组和关键词来搜索设备
	 *
	 * @param chaosApplicationDO 应用
	 * @param groups                 分组列表
	 * @param key                    关键词
	 * @param page
	 * @param size
	 * @return
	 */
	public IPage<ApplicationDeviceDO> findLiveDeviceByAppIdAndGroupAndKey(ApplicationDO chaosApplicationDO,
																		  List<String> groups, String key,
																		  int page,
																		  int size) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", chaosApplicationDO.getId());
		if (!CollectionUtil.isNullOrEmpty(groups)) {
			queryWrapper.in("group_name", groups);
		}
		if (!Strings.isNullOrEmpty(key)) {
			queryWrapper.in("private_ip", key);
		}
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		if (AppType.HOST.getType() == chaosApplicationDO.getAppType()) {
			queryWrapper.in("dimension", ApplicationDimension.HOST.getValue(), ApplicationDimension.NODE.getValue(),
					ApplicationDimension.PROCESS.getValue());
		}
		return applicationDeviceMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

	public ApplicationDeviceDO findAnyOneByAppId(String appId) {
		ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
		applicationDeviceDO.setAppId(Long.valueOf(appId));
		applicationDeviceDO.setStatus(DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectOne(new QueryWrapper<>(applicationDeviceDO).last("limit 1"));
	}

	/**
	 * @param chaosApplicationDO
	 * @param groups
	 * @param key
	 * @param page
	 * @param size
	 * @return
	 */
	public IPage<ApplicationDeviceDO> findLiveDeviceByAppIdAndGroupAndKeyAndTags(ApplicationDO chaosApplicationDO,
																				 List<String> groups, String key, Integer osType,
																				 List<String> tags,
																				 List<String> kubNamespace,
																				 List<String> clusterIds,
																				 int page,
																				 int size) {

		ApplicationDeviceQuery query = new ApplicationDeviceQuery();
		query.setAppId(chaosApplicationDO.getId());
		query.setStatus(DeviceStatus.ONLINE.getStatus());
		if (!CollectionUtil.isNullOrEmpty(groups)) {
			query.setGroups(groups);
		}
		if (!CollectionUtil.isNullOrEmpty(tags)) {
			query.setTags(tags);
		}
		if (!Strings.isNullOrEmpty(key)) {
			query.setPartName("%" + key + "%");
		}
		if (null != osType) {
			query.setOsType(osType);
		}
		if (!CollectionUtil.isNullOrEmpty(kubNamespace)) {
			query.setKubNamespaces(kubNamespace);
		}
		if (!CollectionUtil.isNullOrEmpty(clusterIds)) {
			query.setClusterIds(clusterIds);
		}
		//host应用和主机应用区分开，因为host需要做groupBy处理，sql加了一层select逻辑防止分页出错
		//性能考虑将方法区分开
		if (AppType.HOST.getType() == chaosApplicationDO.getAppType()) {
			return applicationDeviceMapper.selectPageByTagsForHost(MyBatisUtil.getPage(page, size), query);
		} else {
			return applicationDeviceMapper.selectPageByTagsForK8s(MyBatisUtil.getPage(page, size), query);
		}
	}

	/**
	 * 查询在线的应用设备信息
	 *
	 * @param deviceQuery
	 * @param page
	 * @param size
	 * @return
	 */
	public IPage<ApplicationDeviceDO> searchLiveDevice(ApplicationDeviceQuery deviceQuery, int page, int size) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		if (deviceQuery.getAppId() != null) {
			queryWrapper.eq("app_id", deviceQuery.getAppId());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getHostConfigurationId())) {
			queryWrapper.eq("host_configuration_id", deviceQuery.getHostConfigurationId());
		}
		if (!CollectionUtil.isNullOrEmpty(deviceQuery.getDimensions())) {
			queryWrapper.in("dimension", deviceQuery.getDimensions());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getGroupName())) {
			queryWrapper.eq("group_name", deviceQuery.getGroupName());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getPartName())) {
			queryWrapper.nested(nestedWrapper ->
					nestedWrapper.or(wrapper -> wrapper.like("private_ip", deviceQuery.getPartName()))
							.or(wrapper -> wrapper.like("device_name", deviceQuery.getPartName()))
			);
		}

		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

	public List<ApplicationDeviceDO> findLiveDeviceByAppNameAndGroupAndIp(String appName, List<String> groups,
																		  List<String> ips) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_name", appName);
		if (!CollectionUtil.isNullOrEmpty(groups)) {
			queryWrapper.in("group_name", groups);
		}
		if (!CollectionUtil.isNullOrEmpty(ips)) {
			queryWrapper.in("private_ip", ips);
		}
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		queryWrapper.ne("dimension", ApplicationDimension.PROCESS.getValue());
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public List<ApplicationDeviceDO> searchAllLiveDevices(ApplicationDeviceQuery deviceQuery) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		if (deviceQuery.getAppId() != null) {
			queryWrapper.eq("app_id", deviceQuery.getAppId());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getHostConfigurationId())) {
			queryWrapper.eq("host_configuration_id", deviceQuery.getHostConfigurationId());
		}
		if (!CollectionUtil.isNullOrEmpty(deviceQuery.getDimensions())) {
			queryWrapper.in("dimension", deviceQuery.getDimensions());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getGroupName())) {
			queryWrapper.eq("group_name", deviceQuery.getGroupName());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getPartName())) {
			queryWrapper.nested(nestedWrapper ->
					nestedWrapper.or(wrapper -> wrapper.like("private_ip", deviceQuery.getPartName()))
							.or(wrapper -> wrapper.like("device_name", deviceQuery.getPartName()))
			);
		}

		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public List<ApplicationDeviceDO> findByAppInfoAndConfigurationIds(String appId, Set<String> groupName,
																	  List<String> configurationIds) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.in("group_name", groupName);
		queryWrapper.in("configuration_id", configurationIds);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public IPage<ApplicationDeviceDO> searchDeviceByAppIdOrAppNamePart(ApplicationDeviceQuery deviceQuery, int page,
																	   int size) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		if (null != deviceQuery.getAppId()) {
			queryWrapper.eq("app_id", deviceQuery.getAppId());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getPartName())) {
			queryWrapper.eq("namespace", deviceQuery.getNamespace());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getAppName())) {
			queryWrapper.eq("app_name", deviceQuery.getAppName());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getIp())) {
			queryWrapper.eq("private_ip", deviceQuery.getIp());
		}
		if (!Strings.isNullOrEmpty(deviceQuery.getHostConfigurationId())) {
			queryWrapper.eq("host_configuration_id", deviceQuery.getHostConfigurationId());
		}
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		queryWrapper.eq("user_id", deviceQuery.getUserId());

		return applicationDeviceMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

	public int countDeviceByApplication(Long id) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		if (null != id) {
			queryWrapper.eq("app_id", id);
		}
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectCount(queryWrapper);
	}

	public int countDeviceByClusterNode(String hostConfigurationId) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("host_configuration_id", hostConfigurationId);
		queryWrapper.eq("dimension", 2);
		queryWrapper.ne("status", DeviceStatus.OFFLINE.getStatus());
		return applicationDeviceMapper.selectCount(queryWrapper);
	}

	public int countDeviceByAppIdAndGroupNames(String id, List<String> groupNames) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		if (null != id) {
			queryWrapper.eq("app_id", id);
		}
		if (!CollectionUtil.isNullOrEmpty(groupNames)) {
			queryWrapper.in("group_name", groupNames);
		}
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectCount(queryWrapper);
	}

	public List<ApplicationDeviceDO> findByAppIdAndGroups(String appId, List<String> groups) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.in("group_name", groups);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public List<ApplicationDeviceDO> findLiveByAppIdLimitOne(Long appId) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		queryWrapper.last("limit 1");
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public List<ApplicationDeviceDO> findByConfigurationIds(List<String> configurationIds) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("configuration_id", configurationIds);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public ApplicationDeviceDO findByConfigurationId(String configurationIds) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("configuration_id", configurationIds);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		return applicationDeviceMapper.selectOne(queryWrapper);
	}

	public List<ApplicationDeviceDO> findUserDeviceGroupByAppId(String userId,List<Long> appIds) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("max(id) id");
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		queryWrapper.eq("is_deleted", 0);
		if (CollectionUtil.isNullOrEmpty(appIds)) {
			queryWrapper.eq("user_id", userId);
		} else {
			queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().in("app_id", appIds));
		}
		queryWrapper.groupBy("app_id");
		List<ApplicationDeviceDO> applicationDeviceIdList = applicationDeviceMapper.selectList(queryWrapper);
		if (CollectionUtil.isNullOrEmpty(applicationDeviceIdList)) {
			return Lists.newArrayList();
		}
		return applicationDeviceMapper.selectList(
				new QueryWrapper<ApplicationDeviceDO>()
						.lambda()
						.in(ApplicationDeviceDO::getId,
								applicationDeviceIdList
										.stream()
										.map(ApplicationDeviceDO::getId)
										.filter(Objects::nonNull)
										.collect(Collectors.toList())));
	}

	public List<ApplicationDeviceDO> findByClusterId(String user_id, String clusterId, boolean online) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", user_id);
		queryWrapper.eq("cluster_id", clusterId);
		if(online) {
			queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());
		}
		return applicationDeviceMapper.selectList(queryWrapper);
	}

	public Boolean deleteDevice(String userId, DeviceType deviceType, String configurationId) {
		QueryWrapper<ApplicationDeviceDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("configuration_id", configurationId);
		queryWrapper.eq("status", DeviceStatus.ONLINE.getStatus());

		ApplicationDeviceDO applicationDeviceDO = new ApplicationDeviceDO();
		applicationDeviceDO.setStatus(DeviceStatus.OFFLINE.getStatus());
		return applicationDeviceMapper.update(applicationDeviceDO, queryWrapper) > 0;
	}

}
