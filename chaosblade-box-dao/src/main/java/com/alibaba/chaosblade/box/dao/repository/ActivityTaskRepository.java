package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.enums.UserCheckState;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentActivityTaskMapper;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.query.ActivityTaskQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 * 
 */
@Component
public class ActivityTaskRepository extends ServiceImpl<ExperimentActivityTaskMapper, ActivityTaskDO>
    implements IRepository<String, ActivityTaskDO>, IQuery<ActivityTaskDO, ActivityTaskQuery> {
    @Autowired
    private ExperimentActivityTaskMapper experimentActivityTaskMapper;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Override
    public Optional<ActivityTaskDO> findById(String s) {
        return Optional.ofNullable(experimentActivityTaskMapper.selectById(s));
    }

    public int countAppCodesCountByAppId(Long appId) {
        QueryWrapper<ActivityTaskDO> taskDOQueryWrapper = new QueryWrapper<>();
        taskDOQueryWrapper.select("distinct app_code");
        taskDOQueryWrapper.eq("app_id", appId);
        taskDOQueryWrapper.eq("phase", PhaseType.ATTACK);
        return experimentActivityTaskMapper.selectCount(taskDOQueryWrapper);
    }

    @Override
    public boolean update(ActivityTaskDO activityTaskDO) {
        return experimentActivityTaskMapper.updateById(activityTaskDO) > 0;
    }

    public boolean updateActivityTaskFinished(ActivityTaskDO activityTaskDO) {
        QueryWrapper<ActivityTaskDO> taskDOQueryWrapper = new QueryWrapper<>();
        taskDOQueryWrapper.eq("task_id", activityTaskDO.getTaskId());
        taskDOQueryWrapper.ne("run_state", StateEnum.FINISHED);
        return experimentActivityTaskMapper.update(activityTaskDO, taskDOQueryWrapper) > 0;
    }

    @Override
    public boolean add(ActivityTaskDO activityTaskDO) {
        return experimentActivityTaskMapper.insert(activityTaskDO) > 0;
    }

    public List<ActivityTaskDO> findByExperimentTaskId(String experimentTaskId) {
        return experimentActivityTaskMapper.selectList(
            new QueryWrapper<ActivityTaskDO>().eq("experiment_task_id", experimentTaskId));
    }

    public int findUnfinishedTaskByExperimentTaskId(String experimentTaskId) {
        return experimentActivityTaskMapper.selectCount(
            new QueryWrapper<ActivityTaskDO>().eq("experiment_task_id", experimentTaskId)
                .ne("run_state", StateEnum.FINISHED.getValue()));
    }

    public int findUnfinishedTaskByExperimentTaskIdAndPhase(String experimentTaskId, PhaseType phaseType) {
        return experimentActivityTaskMapper.selectCount(
            new QueryWrapper<ActivityTaskDO>().eq("experiment_task_id", experimentTaskId)
                .eq("phase", phaseType)
                .ne("run_state", StateEnum.FINISHED.getValue()));
    }

    public List<ActivityTaskDO> findByAttackTaskId(String attackActivityTaskId) {
        QueryWrapper<ActivityTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attack_activity_task_id", attackActivityTaskId);
        return experimentActivityTaskMapper.selectList(queryWrapper);
    }

    public List<ActivityTaskDO> findByExperimentTaskIdAndPhase(String experimentTaskId, PhaseType phaseType) {
        return experimentActivityTaskMapper.selectList(
            new QueryWrapper<ActivityTaskDO>().eq("experiment_task_id", experimentTaskId)
                .eq("phase", phaseType));
    }

    private QueryWrapper<ActivityTaskDO> buildQuery(ActivityTaskQuery activityTaskQuery) {
        QueryWrapper<ActivityTaskDO> queryWrapper = new QueryWrapper<>();
        if (!Strings.isNullOrEmpty(activityTaskQuery.getTaskId())) {
            queryWrapper.eq("task_id", activityTaskQuery.getTaskId());
        }
        if (activityTaskQuery.getExperimentTaskId() != null) {
            queryWrapper.eq("experiment_task_id", activityTaskQuery.getExperimentTaskId());
        }
        if (activityTaskQuery.getActivityId() != null) {
            queryWrapper.eq("activity_id", activityTaskQuery.getActivityId());
        }
        if (!CollectionUtil.isNullOrEmpty(activityTaskQuery.getStates())) {
            queryWrapper.in("run_state", activityTaskQuery.getStates());
        }
        if (!CollectionUtil.isNullOrEmpty(activityTaskQuery.getExcludeStates())) {
            queryWrapper.notIn("run_state", activityTaskQuery.getExcludeStates());
        }
        return queryWrapper;
    }

    @Override
    public List<ActivityTaskDO> find(ActivityTaskQuery activityTaskQuery) {
        return experimentActivityTaskMapper.selectList(buildQuery(activityTaskQuery));
    }

    @Override
    public int count(ActivityTaskQuery activityTaskQuery) {
        return experimentActivityTaskMapper.selectCount(buildQuery(activityTaskQuery));
    }

    @Override
    public int delete(ActivityTaskQuery activityTaskQuery) {
        return experimentActivityTaskMapper.delete(buildQuery(activityTaskQuery));
    }

    public void updateStateStoppedIncludeMiniApp(String activityTaskId) {
        activityTargetExecutionResultRepository.findByActivityTaskId(activityTaskId).forEach(
            new Consumer<ExperimentMiniAppTaskDO>() {
                @Override
                public void accept(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
                    if (!experimentMiniAppTaskDO.isFinished()) {
                        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
                        experimentMiniAppTaskDO.setResult(ResultEnum.REJECTED.getValue());
                        activityTargetExecutionResultRepository.update(experimentMiniAppTaskDO);
                    }
                }
            });
        ActivityTaskDO activityTaskDO = new ActivityTaskDO();
        activityTaskDO.setState(StateEnum.FINISHED.getValue());
        activityTaskDO.setTaskId(activityTaskId);
        activityTaskDO.setResult(ResultEnum.STOPPED.getValue());
        update(activityTaskDO);
    }

    public boolean updateUserCheckState(ActivityTaskDO activityTaskDO, UserCheckState userCheckState) {
        activityTaskDO.setUserCheckState(userCheckState.getValue());
        return update(activityTaskDO);
    }

    public ActivityTaskDO findFirstRecoveryActivityTask(String experimentTaskId) {
        QueryWrapper<ActivityTaskDO> queryWrapper = new QueryWrapper<ActivityTaskDO>()
            .eq("experiment_task_id", experimentTaskId)
            .eq("phase", PhaseType.RECOVER)
            .orderByAsc("id").
                last("limit 1");
        return experimentActivityTaskMapper.selectOne(queryWrapper);
    }

    public Set<String> findAppCodesByExperimentTaskId(String experimentTaskId) {
        QueryWrapper<ActivityTaskDO> queryWrapper = new QueryWrapper<ActivityTaskDO>()
            .select("app_code")
            .eq("experiment_task_id", experimentTaskId)
            .eq("phase", "ATTACK");
        return experimentActivityTaskMapper.selectList(queryWrapper).stream()
            .map(ActivityTaskDO::getAppCode)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

}
