package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentHostRelationMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO.*;


/**
 * @author haibin
 *
 * 
 */
@Component
public class ExperimentHostRelationRepository
	extends ServiceImpl<ExperimentHostRelationMapper, ExperimentHostRelationDO>
	implements IRepository<Long, ExperimentHostRelationDO> {
	
	@Autowired
	private ExperimentHostRelationMapper experimentHostRelationMapper;
	
	@Override
	public Optional<ExperimentHostRelationDO> findById(Long s) {
		return Optional.ofNullable(experimentHostRelationMapper.selectById(s));
	}
	
	@Override
	public boolean update(ExperimentHostRelationDO experimentHostRelationDO) {
		return experimentHostRelationMapper.updateById(experimentHostRelationDO) > 0;
	}
	
	@Override
	public boolean add(ExperimentHostRelationDO experimentHostRelationDO) {
		return experimentHostRelationMapper.insert(experimentHostRelationDO) > 0;
	}
	
	public List<ExperimentHostRelationDO> findByQuery(QueryWrapper<ExperimentHostRelationDO> queryWrapper) {
		return experimentHostRelationMapper.selectList(queryWrapper);
	}
	
	public List<ExperimentHostRelationDO> findRunningScopesByAppId(String appId, String injectionType) {
		ExperimentHostRelationDO experimentHostRelationDO = new ExperimentHostRelationDO();
		experimentHostRelationDO.setAppId(appId);
		experimentHostRelationDO.setInjectionTargetType(injectionType);
		experimentHostRelationDO.setTaskState(StateEnum.RUNNING.getValue());
		experimentHostRelationDO.setRelationType(ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		return experimentHostRelationMapper.selectList(new QueryWrapper<>(experimentHostRelationDO));
	}
	
	public List<ExperimentHostRelationDO> findByExperimentTaskAndInjectionType(String experimentTaskId, String injectionType) {
		ExperimentHostRelationDO experimentHostRelationDO = new ExperimentHostRelationDO();
		experimentHostRelationDO.setOuterId(experimentTaskId);
		experimentHostRelationDO.setInjectionTargetType(injectionType);
		experimentHostRelationDO.setRelationType(ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		return experimentHostRelationMapper.selectList(new QueryWrapper<>(experimentHostRelationDO));
	}
	
	public boolean updateTaskState(String experimentTaskId, StateEnum stateEnum) {
		QueryWrapper<ExperimentHostRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(COLUMN_RELATION_TYPE, ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		queryWrapper.eq(COLUMN_OUTER_ID, experimentTaskId);
		queryWrapper.ne(COLUMN_EXPERIMENT_TASK_STATE, stateEnum.getValue());
		ExperimentHostRelationDO experimentHostRelationDO = new ExperimentHostRelationDO();
		experimentHostRelationDO.setTaskState(stateEnum.getValue());
		return experimentHostRelationMapper.update(experimentHostRelationDO, queryWrapper) > 0;
	}
	
	public ExperimentHostRelationDO findByConfigurationIdAndExperimentTaskId(String configurationId,
		String experimentTaskId) {
		QueryWrapper<ExperimentHostRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(COLUMN_CONFIGURATION_ID, configurationId);
		queryWrapper.eq(COLUMN_RELATION_TYPE, ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		queryWrapper.eq(COLUMN_OUTER_ID, experimentTaskId);
		return experimentHostRelationMapper.selectOne(queryWrapper);
	}
	
	public int countRunningTasksByAppId(String appId) {
		QueryWrapper<ExperimentHostRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(COLUMN_APP_ID, appId);
		queryWrapper.eq(COLUMN_RELATION_TYPE, ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		queryWrapper.eq(COLUMN_EXPERIMENT_TASK_STATE, StateEnum.RUNNING.getValue());
		return experimentHostRelationMapper.selectCount(queryWrapper);
	}
	
	//select * from t_chaos_experiment_host_relation where  app_configuration_id="fb0c2ac36f56bae75f279090a24e40ac" order by
	// run_state asc, id desc;
	public IPage<ExperimentHostRelationDO> findByAppConfigurationId(
		String appConfigurationId, int size, int page, String injectionType) {
		ExperimentHostRelationDO experimentHostRelationDO = new ExperimentHostRelationDO();
		experimentHostRelationDO.setAppConfigurationId(appConfigurationId);
		experimentHostRelationDO.setRelationType(ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		experimentHostRelationDO.setInjectionTargetType(injectionType);
		return experimentHostRelationMapper.selectPage(MyBatisUtil.getPage(page, size),
			new QueryWrapper<>(experimentHostRelationDO).orderByAsc(COLUMN_EXPERIMENT_TASK_STATE)
				.orderByDesc(COLUMN_GMT_CREATE));
	}
	
	public Integer countExperimentTaskByHost(String configuration) {
		QueryWrapper<ExperimentHostRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(COLUMN_CONFIGURATION_ID, configuration);
		queryWrapper.orderByDesc(COLUMN_ID);
		queryWrapper.eq(COLUMN_RELATION_TYPE, ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		return experimentHostRelationMapper.selectCount(queryWrapper);
	}
	
	public IPage<ExperimentHostRelationDO> findExperimentTaskByConfigurationIdOrderByGmtCreateDesc(
		String configurationId,
		int page, int size) {
		QueryWrapper<ExperimentHostRelationDO> experimentRelationDOQueryWrapper = new QueryWrapper<>();
		experimentRelationDOQueryWrapper.eq(COLUMN_CONFIGURATION_ID, configurationId);
		experimentRelationDOQueryWrapper.orderByDesc(BaseDO.COLUMN_GMT_CREATE);
		return experimentHostRelationMapper.selectPage(MyBatisUtil.getPage(page, size),
			experimentRelationDOQueryWrapper);
	}
	
	public ExperimentHostRelationDO findByAppConfigurationIdAndExperimentTaskIdAndInjectionTypeAndInjectionTargetName(
		String appConfigurationId, String experimentTaskId, String injectionType, String injectionTargetName) {
		QueryWrapper<ExperimentHostRelationDO> experimentRelationDOQueryWrapper = new QueryWrapper<>();
		experimentRelationDOQueryWrapper.eq(COLUMN_APP_CONFIGURATION_ID, appConfigurationId);
		experimentRelationDOQueryWrapper.eq(COLUMN_RELATION_TYPE, ExperimentHostRelationDO.RELATION_EXPERIMENT_TASK);
		experimentRelationDOQueryWrapper.eq(COLUMN_OUTER_ID, experimentTaskId);
		experimentRelationDOQueryWrapper.eq(COLUMN_INJECTION_TARGET_TYPE, injectionType);
		experimentRelationDOQueryWrapper.eq(COLUMN_INJECTION_TARGET_NAME, injectionTargetName);
		return experimentHostRelationMapper.selectOne(experimentRelationDOQueryWrapper);
	}
	
}
