package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ExperimentRelationMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
public class ExperimentRelationRepository implements IRepository<String, ExperimentRelationDO> {

    @Resource
    private ExperimentRelationMapper experimentRelationMapper;

    @Override
    public Optional<ExperimentRelationDO> findById(String relationId) {
        if (Strings.isNullOrEmpty(relationId)) {
            return Optional.empty();
        }

        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<ExperimentRelationDO>()
            .eq("relation_id", relationId);
        return Optional.of(experimentRelationMapper.selectOne(queryWrapper));
    }

    public List<ExperimentRelationDO> findByExperimentId(String experimentId, String relationType) {
        if (Strings.isNullOrEmpty(experimentId)) {
            return Lists.newArrayList();
        }

        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<ExperimentRelationDO>()
            .eq("experiment_id", experimentId)
            .eq(!Strings.isNullOrEmpty(relationType), "relation_type", relationType)
            .eq("is_delete", false);

        return experimentRelationMapper.selectList(queryWrapper);
    }
    


    @Override
    public boolean update(ExperimentRelationDO experimentRelation) {
        if (null == experimentRelation) {
            return false;
        }

        String relationId = experimentRelation.getRelationId();
        if (Strings.isNullOrEmpty(relationId)) {
            return false;
        }

        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("relation_id", relationId);
        ExperimentRelationDO relation = experimentRelationMapper.selectOne(queryWrapper);
        if (null == relation) {
            return false;
        }

        experimentRelation.setId(relation.getId());
        return experimentRelationMapper.updateById(experimentRelation) > 0;
    }

    @Override
    public boolean add(ExperimentRelationDO experimentRelation) {
        if (null == experimentRelation) {
            return false;
        }

        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<ExperimentRelationDO>()
            .eq("experiment_id", experimentRelation.getExperimentId())
            .eq("outer_id", experimentRelation.getOuterId())
            .eq("relation_type", experimentRelation.getRelationType())
            .eq("is_delete", false);
        ExperimentRelationDO relation = experimentRelationMapper.selectOne(queryWrapper);
        if (null != relation) {
            return true;
        }

        return experimentRelationMapper.insert(experimentRelation) > 0;
    }

    public boolean logicDelete(ExperimentRelationDO experimentRelation) {
        experimentRelation.setIsDelete(true);
        return this.update(experimentRelation);
    }

    public List<ExperimentRelationDO> findByOuterIdAndRelationType(String outerId, String relationType) {
        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("outer_id", outerId);
        queryWrapper.eq("relation_type", ExperimentRelationDO.RELATION_TYPE_EXPERTISE);
        return experimentRelationMapper.selectList(queryWrapper);
    }

    public ExperimentRelationDO findByExperimentIdAndOuterIdAndRelationType(String experimentId,
        String expertiseId, String relationType) {
        QueryWrapper<ExperimentRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("experiment_id", experimentId);
        queryWrapper.eq("outer_id", expertiseId);
        queryWrapper.eq("relation_type", relationType);
        return experimentRelationMapper.selectOne(queryWrapper);
    }

}
