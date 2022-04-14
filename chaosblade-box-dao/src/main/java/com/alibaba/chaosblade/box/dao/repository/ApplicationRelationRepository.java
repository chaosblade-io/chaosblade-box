package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationRelationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationRelationDO;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class ApplicationRelationRepository implements IRepository<String, ApplicationRelationDO> {
	
	@Autowired
	private ApplicationRelationMapper applicationRelationMapper;
	
	public List<ApplicationRelationDO> findRelationByAppIdAndRelationType(Long appId, String relationType) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("relation_type", relationType);
		return applicationRelationMapper.selectList(queryWrapper);
	}
	
	public int deleteRelationByOuterId(String outerId, String relationType) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("outer_id", outerId);
		queryWrapper.eq("relation_type", relationType);
		return applicationRelationMapper.delete(queryWrapper);
	}
	
	public IPage<ApplicationRelationDO> pageableFindRelationsByAppIdAndRelationType(Long appId, String relationType,
		int page, int size) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("relation_type", relationType);
		queryWrapper.orderByDesc(BaseDO.COLUMN_GMT_CREATE);
		return applicationRelationMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
	}
	
	public ApplicationRelationDO findLatestExperimentTaskByAppId(Long appId) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("relation_type", ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
		queryWrapper.orderByDesc("id");
		queryWrapper.last("limit 1");
		return applicationRelationMapper.selectOne(queryWrapper);
	}
	
	
	public long updateExperimentTaskState(String taskId, StateEnum stateEnum, ResultEnum resultEnum)
	{
		ApplicationRelationDO applicationRelationDO=new ApplicationRelationDO();
		applicationRelationDO.setTaskState(stateEnum.getValue());
		applicationRelationDO.setTaskResult(resultEnum.getValue());
		ApplicationRelationDO whereApplicationDO=new ApplicationRelationDO();
		whereApplicationDO.setOuterId(taskId);
		whereApplicationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
		return applicationRelationMapper.update(applicationRelationDO,new QueryWrapper<>(whereApplicationDO));
		
	}
	
	
	public Integer countByAppIdAndRelationType(Long appId, String relationType) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("relation_type", relationType);
		return applicationRelationMapper.selectCount(queryWrapper);
	}
	
	public List<ApplicationRelationDO> findByOuterId(String outerId, String relationType) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("outer_id", outerId);
		queryWrapper.eq("relation_type", relationType);
		return applicationRelationMapper.selectList(queryWrapper);
	}
	
	public List<ApplicationRelationDO> findByTaskState(StateEnum stateEnum) {
		ApplicationRelationDO applicationRelationDO=new ApplicationRelationDO();
		applicationRelationDO.setTaskState(stateEnum.getValue());
		applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
		return applicationRelationMapper.selectList(new QueryWrapper<>(applicationRelationDO));
	}
	
	@Override
	public Optional<ApplicationRelationDO> findById(String s) {
		return Optional.empty();
	}
	
	@Override
	public boolean update(ApplicationRelationDO applicationRelationDO) {
		return applicationRelationMapper.updateById(applicationRelationDO) > 0;
	}
	
	@Override
	public boolean add(ApplicationRelationDO applicationRelationDO) {
		return applicationRelationMapper.insert(applicationRelationDO) > 0;
	}
	
	public int deleteRelationByAppIdAndOuterIdAndRelationType(Long appId, String outerId, String relationType) {
		QueryWrapper<ApplicationRelationDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("app_id", appId);
		queryWrapper.eq("outer_id", outerId);
		queryWrapper.eq("relation_type", relationType);
		return applicationRelationMapper.delete(queryWrapper);
	}
	
	
	public List<ApplicationRelationDO> findRunningTaskByAppId(Long appId)
	{
		ApplicationRelationDO applicationRelationDO=new ApplicationRelationDO();
		applicationRelationDO.setAppId(appId);
		applicationRelationDO.setRelationType(ApplicationRelationDO.RELATION_EXPERIMENT_TASK);
		applicationRelationDO.setTaskState(StateEnum.RUNNING.getValue());
		return  applicationRelationMapper.selectList(new QueryWrapper<>(applicationRelationDO));
	}
	
}
