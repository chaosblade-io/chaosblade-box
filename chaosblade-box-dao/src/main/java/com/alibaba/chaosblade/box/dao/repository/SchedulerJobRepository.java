package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.SchedulerTaskMapper;
import com.alibaba.chaosblade.box.dao.model.SchedulerJobDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class SchedulerJobRepository implements IRepository<String, SchedulerJobDO> {

    @Autowired
    private SchedulerTaskMapper schedulerTaskMapper;

    public SchedulerJobDO findByBusinessIdAndBusinessType(Integer businessType, String businessId) {
        QueryWrapper<SchedulerJobDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("business_type", businessType);
        queryWrapper.eq("business_id", businessId);
        return schedulerTaskMapper.selectOne(queryWrapper);
    }




    @Override
    public Optional<SchedulerJobDO> findById(String taskId) {
        return Optional.ofNullable(schedulerTaskMapper.selectById(taskId));
    }

    public List<SchedulerJobDO> findEnabledSchedulerJob() {
        QueryWrapper<SchedulerJobDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enabled", true);
        return schedulerTaskMapper.selectList(queryWrapper);
    }

    @Override
    public boolean update(SchedulerJobDO schedulerJobDO) {
        return schedulerTaskMapper.updateById(schedulerJobDO) > 0;
    }

    @Override
    public boolean add(SchedulerJobDO schedulerJobDO) {
        return schedulerTaskMapper.insert(schedulerJobDO) > 0;
    }

}
