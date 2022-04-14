package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.FeedbackMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskFeedbackDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class FeedbackRepository implements IRepository<String, ExperimentTaskFeedbackDO> {
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public Optional<ExperimentTaskFeedbackDO> findById(String aLong) {
        return Optional.ofNullable(feedbackMapper.selectById(aLong));
    }

    @Override
    public boolean update(ExperimentTaskFeedbackDO experimentTaskFeedbackDO) {
        Preconditions.checkArgument(experimentTaskFeedbackDO.getId() != null, "id Required");
        return feedbackMapper.updateById(experimentTaskFeedbackDO) > 0;
    }

    @Override
    public boolean add(ExperimentTaskFeedbackDO experimentTaskFeedbackDO) {
        return feedbackMapper.insert(experimentTaskFeedbackDO) > 0;
    }

    public Optional<ExperimentTaskFeedbackDO> findByExperimentTaskId(String experimentTaskId) {
        Preconditions.checkArgument(experimentTaskId != null, "experimentTaskId Required");
        QueryWrapper<ExperimentTaskFeedbackDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("experiment_task_id", experimentTaskId);
        return Optional.ofNullable(feedbackMapper.selectOne(queryWrapper));
    }

}
