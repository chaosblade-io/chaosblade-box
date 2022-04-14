package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.AppType;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class ApplicationRepository implements IRepository<Long, ApplicationDO> {

	@Autowired
	private ApplicationMapper applicationMapper;

	@Override
	public Optional<ApplicationDO> findById(Long appId) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id", appId);
		return Optional.ofNullable(applicationMapper.selectOne(queryWrapper));
	}


	public ApplicationDO findByAppIdOrAppName(Long appId,String appName,String userIdForAppName,String namespaceForAppName)
	{
		ApplicationDO applicationDO=null;
		if (appId != null) {
			applicationDO = findById(appId).orElse(null);
		}
		if (!Strings.isNullOrEmpty(appName)) {
			applicationDO = findByUserIdAndAppNameAndUserNamespace(
				userIdForAppName, appName,namespaceForAppName);
		}
		return applicationDO;
	}

	public Optional<ApplicationDO> findByIdWithStr(String appId) {
		if (Strings.isNullOrEmpty(appId)) { return Optional.empty(); }
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id", Long.valueOf(appId));
		return Optional.ofNullable(applicationMapper.selectOne(queryWrapper));
	}

	public ApplicationDO findByUserIdAndAppNameAndUserNamespace(String userId, String appName, String namespace) {
		if (Strings.isNullOrEmpty(appName) || Strings.isNullOrEmpty(userId) || Strings.isNullOrEmpty(namespace)) {
			return null;
		}
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("app_name", appName);
		queryWrapper.eq("namespace", namespace);
		return applicationMapper.selectOne(queryWrapper);
	}

	public ApplicationDO findByUserIdAndAppId(String userId, Long appId) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("id", appId);
		return applicationMapper.selectOne(queryWrapper);
	}

	@Override
	public boolean update(ApplicationDO applicationDO) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id", applicationDO.getId());
		return applicationMapper.update(applicationDO, queryWrapper) > 0;
	}

	public List<ApplicationDO> searchApplicationByNamespace(String userId, String namespace, boolean filterDisabled) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("namespace", namespace);
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		return applicationMapper.selectList(queryWrapper);
	}

	public List<ApplicationDO> fuzzySearchApplication(String userId, String key) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.like("app_name", Optional.ofNullable(key).orElse(""));
		return applicationMapper.selectList(queryWrapper);
	}

	public List<ApplicationDO> fuzzySearchApplication(String userId, String namespace, String key,
		boolean filterDisabled) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.like("app_name", key);
		queryWrapper.eq("namespace", namespace);
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		return applicationMapper.selectList(queryWrapper);
	}

	public IPage<ApplicationDO> pageableFuzzySearchApplication(String userId, String namespace, String key,
		int page, int pageSize,boolean enabled,List<AppType> appTypes) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.like("app_name", key);
		queryWrapper.eq("namespace", namespace);
		queryWrapper.eq("disabled",!enabled);
		queryWrapper.in(!CollectionUtils.isEmpty(appTypes),"app_type",appTypes.stream().map(AppType::getType).collect(
			Collectors.toList()));
		return applicationMapper.selectPage(MyBatisUtil.getPage(page, pageSize), queryWrapper);
	}

	public List<ApplicationDO> fuzzySearchApplication(String userId, String namespace, String key,
		boolean filterDisabled, List<Long> appIds) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("app_name", key);
		queryWrapper.eq("namespace", namespace);
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		if (CollectionUtil.isNullOrEmpty(appIds)) {
			queryWrapper.eq("user_id", userId);
		} else {
			queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().in("id", appIds));
		}
		return applicationMapper.selectList(queryWrapper);
	}

	@Override
	public boolean add(ApplicationDO applicationDO) {
		return applicationMapper.insert(applicationDO) > 0;
	}

	public IPage<ApplicationDO> findApplicationsByUserId(String userId, String namespace, boolean filterDisabled,
		int page, int size) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("namespace", namespace);
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		return applicationMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

	public IPage<ApplicationDO> findApplicationsByUserIdAndAuthApp(String userId,
		String namespace,
		boolean filterDisabled,
		int page,
		int size,
		List<Long> appIds) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("namespace", namespace);
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		if (CollectionUtil.isNullOrEmpty(appIds)) {
			queryWrapper.eq("user_id", userId);
		} else {
			queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().in("id", appIds));
		}
		return applicationMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

	public List<AppNameAndIdPair> findApplicationByUserIdReturnNames(String userId, String namespace) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("namespace", namespace);
		return applicationMapper.selectList(queryWrapper).stream().map(new Function<ApplicationDO, AppNameAndIdPair>() {
			@Override
			public AppNameAndIdPair apply(ApplicationDO applicationDO) {
				AppNameAndIdPair appNameAndIdPair = new AppNameAndIdPair(applicationDO.getAppName(),
					String.valueOf(applicationDO.getId()));
				appNameAndIdPair.setAppType(applicationDO.getAppType());
				appNameAndIdPair.setScopeType(ScopeTypeEnum.judgeScopeByAppType(applicationDO.getAppType()).getValue());
				return appNameAndIdPair;
			}
		}).collect(Collectors.toList());
	}

	public List<AppNameAndIdPair> findApplicationByUserIdAndScopeTypeReturnNames(String userId, String namespace,
		String appType, String key, boolean filterDisabled) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("namespace", namespace);
		if (!Strings.isNullOrEmpty(appType)) {
			queryWrapper.eq("app_type", appType);
		}
		if (!Strings.isNullOrEmpty(key)) {
			queryWrapper.like("app_name", key);
		}
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		return applicationMapper.selectList(queryWrapper).stream().map(new Function<ApplicationDO, AppNameAndIdPair>() {
			@Override
			public AppNameAndIdPair apply(ApplicationDO applicationDO) {
				AppNameAndIdPair appNameAndIdPair = new AppNameAndIdPair(applicationDO.getAppName(),
					String.valueOf(applicationDO.getId()));
				appNameAndIdPair.setAppType(applicationDO.getAppType());
				appNameAndIdPair.setScopeType(ScopeTypeEnum.judgeScopeByAppType(applicationDO.getAppType()).getValue());
				return appNameAndIdPair;
			}
		}).collect(Collectors.toList());
	}

	public List<AppNameAndIdPair> findApplicationByUserIdAndScopeTypeReturnNames(String userId, String namespace,
		String appType, String key,
		boolean filterDisabled,
		List<Long> appIds
	) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("namespace", namespace);
		if (!Strings.isNullOrEmpty(appType)) {
			queryWrapper.eq("app_type", appType);
		}
		if (!Strings.isNullOrEmpty(key)) {
			queryWrapper.like("app_name", key);
		}
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		if (CollectionUtil.isNullOrEmpty(appIds)) {
			queryWrapper.eq("user_id", userId);
		} else {
			queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().in("id", appIds));
		}
		return applicationMapper.selectList(queryWrapper).stream().map(applicationDO -> {
			AppNameAndIdPair appNameAndIdPair = new AppNameAndIdPair(applicationDO.getAppName(),
				String.valueOf(applicationDO.getId()));
			appNameAndIdPair.setAppType(applicationDO.getAppType());
			appNameAndIdPair.setScopeType(ScopeTypeEnum.judgeScopeByAppType(applicationDO.getAppType()).getValue());
			return appNameAndIdPair;
		}).collect(Collectors.toList());
	}

	public List<ApplicationDO> findApplicationAll() {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		return applicationMapper.selectList(queryWrapper);
	}

	public List<ApplicationDO> findApplicationByDisabled(boolean isDisabled) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("disabled", isDisabled);
		return applicationMapper.selectList(queryWrapper);
	}

	/**
	 * 该方法应用定时任务使用
	 * @param isDisabled
	 * @param page
	 * @param size
	 * @return
	 */
	public IPage<ApplicationDO> pageApplicationByDisabled(boolean isDisabled, int page, int size) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("disabled", isDisabled);
		//未使用MyBatisUtil防止限制分页大小
		return applicationMapper.selectPage(new Page<>(page, size), queryWrapper);
	}

	public int deleteApplicationByUserId(String userId, boolean logical) {
		ApplicationDO applicationDO = new ApplicationDO();
		if (logical) {
			applicationDO.setDisabled(true);
			return applicationMapper.update(applicationDO, new QueryWrapper<ApplicationDO>().eq("user_id", userId));
		} else {
			return applicationMapper.delete(new QueryWrapper<ApplicationDO>().eq("user_id", userId));
		}
	}

	public int disableStatusWhenAllDevicesOffLine() {
		return 0;
	}

	/**
	 * 集团应用查询，不会重复，重复异常中断
	 *
	 * @param appName
	 * @return
	 */
	public Optional<ApplicationDO> searchOneApplication(String appName) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_name", appName);
		return Optional.ofNullable(applicationMapper.selectOne(queryWrapper));
	}

	public IPage<ApplicationDO> pageUserApplications(String userId, String namespace,
													 String appType, String key,
													 boolean filterDisabled,
													 List<Long> appIds, int page, int size) {
		QueryWrapper<ApplicationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("namespace", namespace);
		if (!Strings.isNullOrEmpty(appType)) {
			queryWrapper.eq("app_type", appType);
		}
		if (!Strings.isNullOrEmpty(key)) {
			queryWrapper.like("app_name", key);
		}
		if (filterDisabled) {
			queryWrapper.eq("disabled", false);
		}
		if (CollectionUtil.isNullOrEmpty(appIds)) {
			queryWrapper.eq("user_id", userId);
		} else {
			queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().in("id", appIds));
		}
		return applicationMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}

}
