package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.domain.FeedBackConstant;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.task.ExperimentTaskContext;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentTaskMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTaskQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentTaskRepository
    implements IRepository<String, ExperimentTaskDO>, IQuery<ExperimentTaskDO, ExperimentTaskQuery> {

    @Autowired
    private ExperimentTaskMapper experimentTaskMapper;

    @Override
    public Optional<ExperimentTaskDO> findById(String taskId) {
        return Optional.ofNullable(experimentTaskMapper.selectById(taskId));
    }

    public List<ExperimentTaskDO> findByExperimentId(String experimentId) {
        return experimentTaskMapper.selectList(new QueryWrapper<ExperimentTaskDO>().eq("experiment_Id", experimentId)
            .orderByDesc("id"));
    }

    public List<ExperimentTaskDO> findUnfinishedExperimentTasks() {
        return experimentTaskMapper.selectList(
            new QueryWrapper<ExperimentTaskDO>().ne("run_state", StateEnum.FINISHED.getValue())
                .orderByDesc("id"));
    }

    public List<ExperimentTaskDO> findByRunningExperimentTasksAndDurationTimeGtZero() {
        return experimentTaskMapper.selectList(
            new QueryWrapper<ExperimentTaskDO>().eq("run_state", StateEnum.RUNNING.getValue())
                .ge("duration", 0));
    }

    public List<ExperimentTaskDO> findFinishedByExperimentId(String experimentId) {
        return experimentTaskMapper.selectList(new QueryWrapper<ExperimentTaskDO>()
            .eq("experiment_Id", experimentId)
            .eq("run_state", StateEnum.FINISHED.getValue())
            .orderByDesc("id"));
    }

    public List<ExperimentTaskDO> findByExperimentIdsIn(List<String> experimentIds) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds);
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentTaskDO> findFinishedByExperimentIdsIn(List<String> experimentIds) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds)
            .eq("run_state", StateEnum.FINISHED.getValue())
            .orderByDesc("id");
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentTaskDO> findByUserIdAndRunState
        (String userId, StateEnum stateEnum) {
        return experimentTaskMapper.selectList(new QueryWrapper<ExperimentTaskDO>().eq("user_id", userId)
            .eq("run_state", stateEnum.getValue()));
    }

    public boolean existUnfinishedTaskByUserId(String userId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("run_state", StateEnum.FINISHED.getValue());
        return experimentTaskMapper.selectCount(queryWrapper) > 0;
    }

    public List<ExperimentTaskDO> findStoppingTasks() {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>();
        queryWrapper.eq("run_state", StateEnum.STOPPING.getValue());
        return experimentTaskMapper.selectList(queryWrapper);
    }

    @Override
    public List<ExperimentTaskDO> find(ExperimentTaskQuery query) {
        return experimentTaskMapper.selectList(buildQuery(query));
    }

    public List<ExperimentTaskDO> findByExperimentTasksOrderByExperimentDesc(String experimentId, Integer limit) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>();
        queryWrapper.eq("experiment_id", experimentId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit " + limit);
        return experimentTaskMapper.selectList(queryWrapper);
    }

    @Override
    public int count(ExperimentTaskQuery query) {
        return experimentTaskMapper.selectCount(buildQuery(query));
    }

    @Override
    public int delete(ExperimentTaskQuery experimentTaskQuery) {
        return experimentTaskMapper.delete(buildQuery(experimentTaskQuery));
    }

    public ExperimentTaskDO findLatestUnfinishedExperimentTask(String experimentId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("experiment_id", experimentId).ne("run_state", StateEnum.FINISHED.getValue())
            .orderByDesc("gmt_create")
            .last("limit 1");
        return experimentTaskMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean update(ExperimentTaskDO experimentTaskDO) {
        experimentTaskMapper.updateById(experimentTaskDO);
        return true;
    }

    public boolean updateFeedbackStatus(String experimentTaskId, FeedBackConstant.FeedbackStatus feedbackStatus) {
        ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
        experimentTaskDO.setFeedBackStatus(feedbackStatus.getValue());
        experimentTaskDO.setTaskId(experimentTaskId);
        experimentTaskMapper.updateById(experimentTaskDO);
        return true;
    }

    public IPage<ExperimentTaskDO> findByExperimentTasksPageableOrderByCreateTimeDesc(String experimentId, int page,
        int size) {
        return experimentTaskMapper.selectPage(MyBatisUtil.getPage(page, size),
            new QueryWrapper<ExperimentTaskDO>().orderByDesc("gmt_create").eq("experiment_id", experimentId));
    }

    public IPage<ExperimentTaskDO> findByExperimentTasksPageableByUserIdOrderByCreateTimeDesc(String userId, int page,
                                                                                      int size) {
        return experimentTaskMapper.selectPage(MyBatisUtil.getPage(page, size),
                new QueryWrapper<ExperimentTaskDO>().orderByDesc("gmt_create").eq("user_id", userId));
    }

    public List<ExperimentTaskDO> findByOuterId(String outerId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>().eq("outer_id",
            outerId);
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public boolean updateRunState(String experimentTaskId, StateEnum stateEnum) {
        ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
        experimentTaskDO.setTaskId(experimentTaskId);
        experimentTaskDO.setState(stateEnum.getValue());
        return experimentTaskMapper.updateById(experimentTaskDO) > 0;
    }

    public boolean updateContext(String experimentTaskId, ExperimentTaskContext experimentTaskContext) {
        ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
        experimentTaskDO.setTaskId(experimentTaskId);
        experimentTaskDO.setExperimentTaskContext(experimentTaskContext);
        return experimentTaskMapper.updateById(experimentTaskDO) > 0;
    }

    @Override
    public boolean add(ExperimentTaskDO experimentTaskDO) {
        experimentTaskMapper.insert(experimentTaskDO);
        return true;
    }

    private QueryWrapper<ExperimentTaskDO> buildQuery(ExperimentTaskQuery query) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<>();
        if (!Strings.isNullOrEmpty(query.getTaskId())) {
            queryWrapper.eq("task_id", query.getTaskId());
        }
        if (query.getGmtCreateFrom() != null) {
            queryWrapper.ge("gmt_create", query.getGmtCreateFrom());
        }
        if (query.getGmtCreateTo() != null) {
            queryWrapper.le("gmt_create", query.getGmtCreateTo());
        }
        if (query.getExperimentId() != null) {
            queryWrapper.eq("experiment_id", query.getExperimentId());
        }
        if (!CollectionUtil.isNullOrEmpty(query.getExcludeStates())) {
            queryWrapper.notIn("run_state", query.getExcludeStates());
        }
        if (!CollectionUtil.isNullOrEmpty(query.getStates())) {
            queryWrapper.in("run_state", query.getStates());
        }
        if (null != query.getUser()) {
            ChaosUser user = query.getUser();
            if (!Strings.isNullOrEmpty(user.getUserId())) {
                queryWrapper.eq("user_id", user.getUserId());
            }
        }
        if (query.getNamespace() != null) {
            queryWrapper.eq("namespace", query.getNamespace());
        }
        if (query.getHost() != null) {
            queryWrapper.eq("host_ip", query.getHost());
        }
        return queryWrapper;
    }

    public List<ExperimentTaskDO> findTasksBetweenByExperimentId(String experimentId, Date startTime, Date endTime) {
        ExperimentTaskQuery experimentTaskQuery = new ExperimentTaskQuery();
        experimentTaskQuery.setExperimentId(experimentId);
        experimentTaskQuery.setGmtCreateFrom(startTime);
        experimentTaskQuery.setGmtCreateTo(endTime);
        return find(experimentTaskQuery);
    }

    public List<ExperimentTaskDO> findFinishedTasksBetweenByExperimentId(String experimentId, Date startTime,
        Date endTime) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>()
            .eq("experiment_id", experimentId)
            .eq("run_state", StateEnum.FINISHED.getValue())
            .ge("gmt_create", startTime)
            .le("gmt_create", endTime)
            .orderByDesc("gmt_create");
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentTaskDO> findTasksBetweenByExperimentIdsIn(List<String> experimentIds, Date startTime,
        Date endTime) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds)
            .ge("gmt_create", startTime)
            .le("gmt_create", endTime)
            .orderByDesc("gmt_create");
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentTaskDO> findFinishedTasksBetweenByExperimentIdsIn(List<String> experimentIds, Date startTime,
        Date endTime) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>()
            .in(!CollectionUtil.isNullOrEmpty(experimentIds), "experiment_id", experimentIds)
            .eq("run_state", StateEnum.FINISHED.getValue())
            .ge("gmt_create", startTime)
            .le("gmt_create", endTime)
            .orderByDesc("gmt_create");
        return experimentTaskMapper.selectList(queryWrapper);
    }

    public void updateActivityTaskId(String taskId, String activityTaskId) {
        ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
        experimentTaskDO.setActivityTaskId(activityTaskId);
        experimentTaskDO.setTaskId(taskId);
        this.experimentTaskMapper.updateById(experimentTaskDO);
    }

    public boolean existByUserIdAndTaskId(ChaosUser user, String experimentTaskId) {
        ExperimentTaskQuery experimentTaskQuery = new ExperimentTaskQuery();
        experimentTaskQuery.setUser(user);
        experimentTaskQuery.setTaskId(experimentTaskId);
        return this.experimentTaskMapper.selectCount(buildQuery(experimentTaskQuery)) > 0;
    }

    public Integer deleteFinishedExperimentTaskByUserId(String userId, boolean logical) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run_state", StateEnum.FINISHED.getValue());
        if (logical) {
            ExperimentTaskDO experimentTaskDO = new ExperimentTaskDO();
            experimentTaskDO.setIsDelete(true);
            return experimentTaskMapper.update(experimentTaskDO, queryWrapper);
        } else {
            return experimentTaskMapper.delete(queryWrapper);
        }
    }

    public ExperimentTaskDO findUserLastFinishedExperimentTask(String userId) {
        QueryWrapper<ExperimentTaskDO> queryWrapper = new QueryWrapper<ExperimentTaskDO>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("run_state", StateEnum.FINISHED.getValue());
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 1");
        return experimentTaskMapper.selectOne(queryWrapper);
    }

}
