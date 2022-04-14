package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.DBConstant;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentActivityTargetTaskMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.query.ActivityTargetExecutionResultQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityTargetExecutionResultRepository
        extends ServiceImpl<ExperimentActivityTargetTaskMapper, ExperimentMiniAppTaskDO>
        implements IRepository<String, ExperimentMiniAppTaskDO>,
        IQuery<ExperimentMiniAppTaskDO, ActivityTargetExecutionResultQuery> {

    @Autowired
    private ExperimentActivityTargetTaskMapper experimentActivityTargetTaskMapper;

    @Override
    public Optional<ExperimentMiniAppTaskDO> findById(String s) {
        return Optional.ofNullable(experimentActivityTargetTaskMapper.selectById(s));
    }

    @Override
    public boolean update(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        return updateById(experimentMiniAppTaskDO);
    }

    public boolean updateNotFinishedTask(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull(DBConstant.GMT_END);
        queryWrapper.eq(DBConstant.TASK_ID, experimentMiniAppTaskDO.getTaskId());
        return update(experimentMiniAppTaskDO, queryWrapper);
    }

    public boolean startTask(String taskId, String activityTaskId, StateEnum stateEnum) {
        UpdateWrapper<ExperimentMiniAppTaskDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(DBConstant.RUN_STATE, stateEnum.getValue());
        updateWrapper.set(DBConstant.START_TIME, new Date());
        updateWrapper.set(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        updateWrapper.set(DBConstant.GMT_END, null);
        updateWrapper.set(DBConstant.ERROR_MESSAGE, null);
        updateWrapper.eq(DBConstant.TASK_ID, taskId);
        return update(updateWrapper);
    }

    public ExperimentMiniAppTaskDO findByActivityTaskIdAndHostAndAppCode(String activityTaskId, String host,
                                                                         String appCode) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.HOST_IP, host);
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        queryWrapper.eq(DBConstant.APP_CODE, appCode);
        return experimentActivityTargetTaskMapper.selectOne(queryWrapper);
    }

    public List<Host> findHostsByExperimentTaskAndPhase(String experimentTaskId, PhaseType phaseType) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        queryWrapper.isNotNull(DBConstant.HOST_IP);
        queryWrapper.eq(DBConstant.PHASE, phaseType);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper).stream().map(
                new Function<ExperimentMiniAppTaskDO, Host>() {
                    @Override
                    public Host apply(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
                        Host host = new Host(experimentMiniAppTaskDO.getHostIp(), -1);
                        host.setIp(host.getIp());
                        host.setDeviceConfigurationId(experimentMiniAppTaskDO.getDeviceConfigurationId());
                        return host;
                    }
                }).distinct().collect(Collectors.toList());
    }

    @Override
    public boolean add(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        if (experimentMiniAppTaskDO.getTaskId() == null) {
            experimentMiniAppTaskDO.setTaskId(IdWorker.getIdStr());
        }
        return experimentActivityTargetTaskMapper.insert(experimentMiniAppTaskDO) > 0;
    }

    public List<ExperimentMiniAppTaskDO> findByActivityTaskId(String activityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findByActivityTaskIdReturnSummary(String activityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(ExperimentMiniAppTaskDO.class, new Predicate<TableFieldInfo>() {
            @Override
            public boolean test(TableFieldInfo tableFieldInfo) {
                Set<String> ignoreColumns = new HashSet<>();
                ignoreColumns.add(DBConstant.DATA);
                ignoreColumns.add(DBConstant.ORIGIN_REQUEST);
                ignoreColumns.add(DBConstant.ORIGIN_RESPONSE);
                ignoreColumns.add(DBConstant.ERROR_MESSAGE);
                return !ignoreColumns.contains(tableFieldInfo.getColumn());
            }
        });
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findRunningTask(String appCode) {
        QueryWrapper<ExperimentMiniAppTaskDO> experimentActivityTargetTaskDOQueryWrapper = new QueryWrapper<>();
        experimentActivityTargetTaskDOQueryWrapper.eq(DBConstant.APP_CODE, appCode);
        experimentActivityTargetTaskDOQueryWrapper.eq("run_state", StateEnum.RUNNING.getValue());
        return experimentActivityTargetTaskMapper.selectList(experimentActivityTargetTaskDOQueryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findAsyncRunningTask() {
        QueryWrapper<ExperimentMiniAppTaskDO> experimentActivityTargetTaskDOQueryWrapper = new QueryWrapper<>();
        experimentActivityTargetTaskDOQueryWrapper.eq("async", true);
        experimentActivityTargetTaskDOQueryWrapper.eq(DBConstant.PHASE, PhaseType.ATTACK);
        experimentActivityTargetTaskDOQueryWrapper.eq("run_state", StateEnum.RUNNING.getValue());
        return experimentActivityTargetTaskMapper.selectList(experimentActivityTargetTaskDOQueryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findRunningTaskByCodeLikeLeft(String appCode) {
        QueryWrapper<ExperimentMiniAppTaskDO> experimentActivityTargetTaskDOQueryWrapper = new QueryWrapper<>();
        experimentActivityTargetTaskDOQueryWrapper.likeLeft(DBConstant.APP_CODE, appCode);
        experimentActivityTargetTaskDOQueryWrapper.eq("run_state", StateEnum.RUNNING.getValue());
        return experimentActivityTargetTaskMapper.selectList(experimentActivityTargetTaskDOQueryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findByActivityTaskIdAndResultNotSuccess(String activityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        queryWrapper.and(wrapper -> wrapper.ne("result_state", ResultEnum.SUCCESS.getValue()).or()
                .isNull("result_state"));
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public Integer countByTaskIdAndResultSuccess(String experimentTaskId, PhaseType phaseType) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        queryWrapper.eq("result_state", ResultEnum.SUCCESS.getValue());
        queryWrapper.eq(DBConstant.PHASE, phaseType);
        return experimentActivityTargetTaskMapper.selectCount(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> selectByTaskId(String experimentTaskId, PhaseType phaseType) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        queryWrapper.eq(DBConstant.PHASE, phaseType);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findByExperimentTaskIdAndConfigurationIdAndState(
            String experimentTaskId,
            String configurationId,
            StateEnum stateEnum) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        queryWrapper.eq("device_configuration_id", configurationId);
        queryWrapper.eq("run_state", stateEnum.getValue());
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findUnFinishedResults(String hostIp) {
        if (Strings.isNullOrEmpty(hostIp)) {return new ArrayList<>();}
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.HOST_IP, hostIp);
        queryWrapper.isNull(DBConstant.GMT_END);
        queryWrapper.ne(DBConstant.EXPERIMENT_TASK_ID, "");
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    @Override
    public List<ExperimentMiniAppTaskDO> find(
            ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery) {
        return experimentActivityTargetTaskMapper.selectList(buildQueryWrapper(activityTargetExecutionResultQuery));
    }

    public List<ExperimentMiniAppTaskDO> findByHostAndExperimentTaskId(String host, String experimentTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        queryWrapper.eq(DBConstant.HOST_IP, host);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public ExperimentMiniAppTaskDO findOne(
            ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery) {
        List<ExperimentMiniAppTaskDO> experimentMiniAppTaskDOS = experimentActivityTargetTaskMapper
                .selectList(
                        buildQueryWrapper(activityTargetExecutionResultQuery));
        if (experimentMiniAppTaskDOS.isEmpty()) {return null;}
        return experimentMiniAppTaskDOS.get(0);
    }

    private QueryWrapper<ExperimentMiniAppTaskDO> buildQueryWrapper(
            ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper
                = new QueryWrapper<ExperimentMiniAppTaskDO>();
        if (!Strings.isNullOrEmpty(activityTargetExecutionResultQuery.getActivityTaskId())) {
            queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTargetExecutionResultQuery.getActivityTaskId());
        }
        if (!Strings.isNullOrEmpty(activityTargetExecutionResultQuery.getAppCode())) {
            queryWrapper.eq(DBConstant.APP_CODE, activityTargetExecutionResultQuery.getAppCode());
        }
        if (!Strings.isNullOrEmpty(activityTargetExecutionResultQuery.getHostIp())) {
            queryWrapper.eq(DBConstant.HOST_IP, activityTargetExecutionResultQuery.getHostIp());
        }
        return queryWrapper;
    }

    @Override
    public int count(ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery) {
        return 0;
    }

    @Override
    public int delete(ActivityTargetExecutionResultQuery activityTargetExecutionResultQuery) {
        return 0;
    }

    public List<ExperimentMiniAppTaskDO> findFailedTasksByActivityTaskId(String oldActivityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper
                = new QueryWrapper<ExperimentMiniAppTaskDO>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, oldActivityTaskId);
        queryWrapper.ne("result_state", ResultEnum.SUCCESS.getValue());
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findRunningTasksByConfigurationId(String configurationId) {
        if (Strings.isNullOrEmpty(configurationId)) {return new ArrayList<>();}
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper
                = new QueryWrapper<ExperimentMiniAppTaskDO>();
        queryWrapper.eq("device_configuration_id", configurationId);
        queryWrapper.ne("run_state", StateEnum.RUNNING.getValue());
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public List<ExperimentMiniAppTaskDO> findRunningTaskByAppConfigurationIdAndPhase(String appConfigurationId,
                                                                                     PhaseType phaseType) {
        if(Strings.isNullOrEmpty(appConfigurationId)) {
            return Lists.newArrayList();
        }
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper
                = new QueryWrapper<ExperimentMiniAppTaskDO>();
        queryWrapper.eq("app_configuration_id", appConfigurationId);
        queryWrapper.eq("run_state", StateEnum.RUNNING.getValue());
        queryWrapper.eq("phase",phaseType);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper);
    }

    public Set<String> findHostIpsByExperimentTaskId(String experimentTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper
                = new QueryWrapper<ExperimentMiniAppTaskDO>()
                .eq(DBConstant.EXPERIMENT_TASK_ID, experimentTaskId);
        return experimentActivityTargetTaskMapper.selectList(queryWrapper).stream().map(
                ExperimentMiniAppTaskDO::getHostIp).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public List<ExperimentMiniAppTaskDO> findByExperimentTaskIdAndPhase(String experimentTaskId, PhaseType phaseType) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = new ExperimentMiniAppTaskDO();
        experimentMiniAppTaskDO.setExperimentTaskId(experimentTaskId);
        experimentMiniAppTaskDO.setPhase(phaseType);
        return experimentActivityTargetTaskMapper.selectList(new QueryWrapper<>(experimentMiniAppTaskDO));
    }

    public Integer countFinishedTaskByAppCode(String appCode) {
        if (Strings.isNullOrEmpty(appCode)) {
            return 0;
        }
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<ExperimentMiniAppTaskDO>()
                .eq(DBConstant.APP_CODE, appCode)
                .isNotNull(DBConstant.GMT_END);
        return experimentActivityTargetTaskMapper.selectCount(queryWrapper);
    }

    public void updateMiniAppTasksByActivityTaskId(String activityTaskId) {
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = new ExperimentMiniAppTaskDO();
        experimentMiniAppTaskDO.setState(StateEnum.READY.getValue());
        experimentMiniAppTaskDO.setData("");
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        experimentActivityTargetTaskMapper.update(experimentMiniAppTaskDO, queryWrapper);
    }

    public IPage<ExperimentMiniAppTaskDO> pageableFindByErrorMessage(String errorMessage, int page, int size) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ERROR_MESSAGE, errorMessage);
        queryWrapper.eq("result_state", 1);
        return experimentActivityTargetTaskMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
    }

    public Integer countFailedTask(String activityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        queryWrapper.ne("result_state", 0);
        return experimentActivityTargetTaskMapper.selectCount(queryWrapper);
    }

    public Integer countTotalTask(String activityTaskId) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DBConstant.ACTIVITY_TASK_ID, activityTaskId);
        return experimentActivityTargetTaskMapper.selectCount(queryWrapper);
    }

    public boolean isAllAttackTaskFailed(List<String> experimentTaskIds) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(DBConstant.EXPERIMENT_TASK_ID, experimentTaskIds);
        queryWrapper.eq(DBConstant.PHASE, "ATTACK");
        queryWrapper.eq("result_state", ResultEnum.SUCCESS.getValue());
        return experimentActivityTargetTaskMapper.selectCount(queryWrapper) == 0;
    }

    public IPage<ExperimentMiniAppTaskDO> pageableFindByAppConfigurationIds(List<String> appConfigurationIds, int page,
                                                                            int size) {
        QueryWrapper<ExperimentMiniAppTaskDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("app_configuration_id", appConfigurationIds);
        queryWrapper.eq(DBConstant.PHASE, PhaseType.ATTACK.name());
        queryWrapper.groupBy(DBConstant.EXPERIMENT_TASK_ID);
        queryWrapper.orderByDesc(DBConstant.START_TIME);
        return experimentActivityTargetTaskMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
    }

}
