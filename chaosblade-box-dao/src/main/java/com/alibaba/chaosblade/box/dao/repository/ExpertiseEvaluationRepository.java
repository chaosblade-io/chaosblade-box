package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExpertiseEvaluationMapper;
import com.alibaba.chaosblade.box.dao.model.ExpertiseEvaluationDO;
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
public class ExpertiseEvaluationRepository implements IRepository<String, ExpertiseEvaluationDO> {

    @Autowired
    private ExpertiseEvaluationMapper expertiseEvaluationMapper;

    @Override
    public Optional<ExpertiseEvaluationDO> findById(String s) {
        return Optional.ofNullable(expertiseEvaluationMapper.selectById(s));
    }

    @Override
    public boolean update(ExpertiseEvaluationDO expertiseEvaluationDO) {
        return expertiseEvaluationMapper.updateById(expertiseEvaluationDO) > 0;
    }

    @Override
    public boolean add(ExpertiseEvaluationDO expertiseEvaluationDO) {
        return expertiseEvaluationMapper.insert(expertiseEvaluationDO) > 0;
    }

    public int deleteByExpertiseId(String expertiseId) {
        QueryWrapper<ExpertiseEvaluationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("expertise_id", expertiseId);
        return expertiseEvaluationMapper.delete(queryWrapper);
    }

    public List<ExpertiseEvaluationDO> findByExpertiseId(String expertiseId) {
        QueryWrapper<ExpertiseEvaluationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("expertise_id", expertiseId);
        return expertiseEvaluationMapper.selectList(queryWrapper);
    }
}
