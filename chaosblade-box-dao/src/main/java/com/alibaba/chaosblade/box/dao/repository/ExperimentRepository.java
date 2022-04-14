package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.ExperimentQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.ExperimentSummaryInfo;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentPageQuery;
import com.alibaba.chaosblade.box.dao.query.ExperimentQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 演练的repo，演练对象的读取和保存都在这里面做,缓存也在这里面控制
 * </p>
 *
 * @author haibin
 *
 *
 */
@Component
public class ExperimentRepository implements IRepository<String, ExperimentDO>, IQuery<ExperimentDO, ExperimentQuery> {

    @Resource
    private ExperimentMapper experimentMapper;

    @Override
    public Optional<ExperimentDO> findById(String experimentId) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>()
            .eq("experiment_id", experimentId)
            .eq("is_delete", false);

        return Optional.ofNullable(experimentMapper.selectOne(queryWrapper));
    }

    public Optional<ExperimentDO> findByIdAndNamespace(String experimentId, String namespace) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>()
            .eq("experiment_id", experimentId)
            .eq("namespace", namespace)
            .eq("is_delete", false);

        return Optional.ofNullable(experimentMapper.selectOne(queryWrapper));
    }

    @Override
    public List<ExperimentDO> find(ExperimentQuery experimentQuery) {
        return experimentMapper.selectList(buildExperimentQueryWrapper(experimentQuery));
    }

    public List<ExperimentDO> findAll() {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        return experimentMapper.selectList(queryWrapper);
    }

    public boolean existByUserIdAndExperimentId(ChaosUser user, String experimentId) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<>();
        appendUser(queryWrapper, user);
        queryWrapper.eq("experiment_id", experimentId);
        return experimentMapper.selectCount(queryWrapper) > 0;
    }

    public IPage<ExperimentDO> findByPage(ExperimentPageQuery experimentPageQuery, int pageNo, int pageSize) {
        return experimentMapper.selectExperimentsByPage(
            MyBatisUtil.getPage(pageNo, pageSize), experimentPageQuery
        );
    }

    @Override
    public int count(ExperimentQuery query) {
        return experimentMapper.selectCount(buildExperimentQueryWrapper(query));
    }

    @Override
    public int delete(ExperimentQuery experimentQuery) {
        return experimentMapper.delete(buildExperimentQueryWrapper(experimentQuery));
    }

    public boolean logicDelete(ExperimentQuery experimentQuery) {
        QueryWrapper<ExperimentDO> queryWrapper = buildExperimentQueryWrapper(experimentQuery);
        List<ExperimentDO> experiments = experimentMapper.selectList(queryWrapper);
        if (null != experiments && !experiments.isEmpty()) {
            ExperimentDO experiment = experiments.get(0);
            experiment.setIsDelete(true);
            return experimentMapper.updateById(experiment) > 0;
        }
        return false;
    }

    @Override
    public boolean update(ExperimentDO experimentDO) {
        return experimentMapper.updateById(experimentDO) > 0;
    }

    @Override
    public boolean add(ExperimentDO experimentDO) {
        return experimentMapper.insert(experimentDO) > 0;
    }

    private void appendUser(QueryWrapper<ExperimentDO> queryWrapper, ChaosUser user) {
        if (null != user) {

            if (!Strings.isNullOrEmpty(user.getUserId())) {
                queryWrapper.eq("user_id", user.getUserId());
            }
        }
    }

    private QueryWrapper<ExperimentDO> buildExperimentQueryWrapper(ExperimentQuery query) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<>();
        appendUser(queryWrapper, query.getUser());

        queryWrapper.eq(!Strings.isNullOrEmpty(query.getNamespace()), "namespace", query.getNamespace())
            .eq(null != query.getState(), "state", ExperimentStateEnum.getValue(query.getState()))
            //            .eq("is_delete", false)
            .eq("is_template", false)
            .ne("state", ExperimentStateEnum.INVISIBLE.getValue())
            .in(
                !CollectionUtil.isNullOrEmpty(query.getResults()),
                "result_state",
                !CollectionUtil.isNullOrEmpty(query.getResults())
                    ? query.getResults()
                    .stream()
                    .map(ResultEnum::getValue)
                    .collect(Collectors.toList())
                    : Lists.newArrayList()
            )
            .in(null != query.getExperimentIds() && !query.getExperimentIds().isEmpty(), "experiment_id",
                query.getExperimentIds())
            .like(!Strings.isNullOrEmpty(query.getPartName()), "name", query.getPartName())
            .orderByDesc("gmt_create");

        if (!Boolean.TRUE.equals(query.getContainsDeleted())) {
            queryWrapper.eq("is_delete", false);
        }

        return queryWrapper;
    }

    public List<ExperimentDO> findByExperimentIdsIn(List<String> experimentIds) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds)
            .eq("is_delete", false)
            .ne("state", ExperimentStateEnum.INVISIBLE.getValue());
        return experimentMapper.selectList(queryWrapper);
    }

    public List<ExperimentDO> findBetweenByExperimentIdsIn(List<String> experimentIds, Date startTime, Date endTime) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds)
            .eq("is_delete", false)
            .ge("gmt_modified", startTime)
            .le("gmt_modified", endTime)
            .ne("state", ExperimentStateEnum.INVISIBLE.getValue())
            .orderByDesc("gmt_modified");
        return experimentMapper.selectList(queryWrapper);
    }

    public int countByExpertiseId(String expertiseId) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>()
            .eq("is_template", false)
            .eq("outer_id", expertiseId);
        return experimentMapper.selectCount(queryWrapper);
    }

    public List<ExperimentSummaryInfo> getExperimentSummaryInfoIn30Days(ChaosUser user, ExperimentQueryRequest request) {
        return experimentMapper.getExperimentSummaryInfoIn30Days(
            user.getUserId(),
            request.getNamespace());
    }

    public List<ExperimentSummaryInfo> getExperimentSummaryInfo(ChaosUser user, ExperimentQueryRequest request) {
        return experimentMapper.getExperimentSummaryInfo(
            user.getUserId(),
            request.getNamespace());
    }

    public Integer deleteFinishedExperimentByUserId(String userId, boolean logical) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("state", ExperimentStateEnum.RUNNING.getValue());
        if (logical) {
            ExperimentDO experimentDO = new ExperimentDO();
            experimentDO.setIsDelete(true);
            return experimentMapper.update(experimentDO, queryWrapper);
        } else {
            return experimentMapper.delete(queryWrapper);
        }
    }

    public List<ExperimentDO> findByUserId(String userId) {
        QueryWrapper<ExperimentDO> queryWrapper = new QueryWrapper<ExperimentDO>();
        queryWrapper.eq("user_id", userId).
                eq("is_delete", false);
        return experimentMapper.selectList(queryWrapper);
    }
}
