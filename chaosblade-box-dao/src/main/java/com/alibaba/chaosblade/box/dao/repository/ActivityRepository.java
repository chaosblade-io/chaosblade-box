package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppCodeHelper;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentActivityMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentActivityQuery;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
public class ActivityRepository
    implements IRepository<String, ExperimentActivityDO> {

    @Autowired
    ExperimentActivityMapper experimentActivityMapper;

    @Autowired
    private MiniAppCodeHelper miniAppCodeHelper;

    @Override
    public Optional<ExperimentActivityDO> findById(String s) {
        return Optional.ofNullable(
            findLatestActivityByActivityId(s));
    }

    @Override
    public boolean update(ExperimentActivityDO experimentActivityDO) {
        return experimentActivityMapper.updateById(experimentActivityDO) > 0;
    }

    public boolean deleteByActivityId(ExperimentActivityDO experimentActivityDO) {
        ExperimentActivityDO updateExperimentActivityDO = new ExperimentActivityDO();
        updateExperimentActivityDO.setIsDeleted(true);
        updateExperimentActivityDO.setActivityId(experimentActivityDO.getActivityId());
        return experimentActivityMapper.updateById(updateExperimentActivityDO) > 0;
    }

    @Override
    public boolean add(ExperimentActivityDO experimentActivityDO) {
        experimentActivityDO.setVersion(0);
        return experimentActivityMapper.insert(experimentActivityDO) > 0;
    }

    public List<ExperimentActivityDO> findActivities(String experimentId) {
        ExperimentActivityQuery experimentActivityQuery = new ExperimentActivityQuery();
        experimentActivityQuery.setExperimentId(experimentId);
        return find(experimentActivityQuery);
    }

    public ExperimentActivityDO findLatestActivityByActivityId(String activityId) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 1");
        return experimentActivityMapper.selectOne(queryWrapper);
    }

    public List<ExperimentActivityDO> find(ExperimentActivityQuery query) {
        List<ExperimentActivityDO> experimentActivityDOS = experimentActivityMapper.selectList(
            buildExperimentActivityQueryWrapper(query));
        return experimentActivityDOS;
    }

    public int count(ExperimentActivityQuery query) {
        return experimentActivityMapper.selectCount(buildExperimentActivityQueryWrapper(query));
    }

    public List<ExperimentActivityDO> findByExperimentIdAndPhase(String experimentId, PhaseType phase) {
        ExperimentActivityQuery experimentActivityQuery = new ExperimentActivityQuery();
        experimentActivityQuery.setExperimentId(experimentId);
        experimentActivityQuery.setPhase(phase);
        return find(experimentActivityQuery);
    }

    public int deleteByNoFlowId(String experimentId) {
        ExperimentActivityDO experimentActivityDO = new ExperimentActivityDO();
        experimentActivityDO.setIsDeleted(true);
        return experimentActivityMapper.update(experimentActivityDO, new QueryWrapper<ExperimentActivityDO>()
            .eq("experiment_id", experimentId).isNull("flow_id"));
    }

    private Wrapper<ExperimentActivityDO> buildExperimentActivityQueryWrapper(ExperimentActivityQuery query) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = new QueryWrapper<>();
        if (query.getExperimentId() != null) {
            queryWrapper.eq("experiment_id", query.getExperimentId());
        }
        if (query.getPhase() != null) {
            queryWrapper.eq("phase", query.getPhase());
        }
        if (query.getParentActivityId() != null) {
            queryWrapper.eq("parent_activity_id", query.getParentActivityId());
        }
        if (query.getActivityId() != null) {
            queryWrapper.eq("activity_id", query.getActivityId());
        }
        if (query.isExcludePointcut()) {
            queryWrapper.isNull("parent_activity_id");
        }
        if (!CollectionUtil.isNullOrEmpty(query.getFlowIds())) {
            queryWrapper.in("flow_id", query.getFlowIds());
        }
        queryWrapper.eq("is_deleted", query.isDeleted());
        return queryWrapper;
    }

    public void batchInsert(List<ExperimentActivityDO> experimentActivityDOS) {
        experimentActivityDOS.forEach(this::add);
    }

    public List<ExperimentActivityDO> findByFlowIdAndAppCode(String flowId, String appCode) {
        QueryWrapper<ExperimentActivityDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flow_id", flowId);
        if (null != appCode) {
            queryWrapper.eq("app_code", appCode);
        }
        return experimentActivityMapper.selectList(queryWrapper);
    }

}
