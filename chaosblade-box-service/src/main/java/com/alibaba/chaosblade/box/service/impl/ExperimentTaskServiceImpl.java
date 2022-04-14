package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskResult;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskRequest;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.ThreadPoolExecutors;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.ChangeActionType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.ChangeOperatorType;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskControl;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskLog;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskPushRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentTaskSimple;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentTaskGuardsResult;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentTaskSummary;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.Trackers;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChangeLogExecutor;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskFinishedCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskSimpleInfoFinder;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTaskQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.service.ExperimentTaskService;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentTaskPushCommand;
import com.alibaba.chaosblade.box.service.command.task.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class ExperimentTaskServiceImpl implements ExperimentTaskService, InitializingBean {

    @Autowired
    private ExperimentTaskManager experimentTaskManager;

    @Autowired
    private ExperimentTaskSimpleInfoFinder experimentTaskSimpleInfoFinder;

    @Resource
    private ExperimentTaskRepository experimentTaskRepository;

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Resource
    private ExperimentRepository experimentRepository;

    @Autowired
    private CommandBus commandBus;

    @Resource
    private ExperimentChecker experimentChecker;

    private ThreadPoolExecutor experimentTaskStopThreadPool;

    @Override
    public Response<ExperimentTask> getExperimentTaskFullInfo(String taskId) {
        return commandBus.syncRun(
            ExperimentTaskQueryCommand.class,
            new ExperimentTaskQueryRequest(taskId)
        );
    }

    @Override
    public Response pushExperimentTask(ExperimentTaskPushRequest experimentTaskPushRequest) {
        return commandBus.syncRun(ExperimentTaskPushCommand.class, experimentTaskPushRequest);
    }

    @Override
    public Response<ExperimentTaskSummary> getExperimentTaskSummary(String taskId) {
        return commandBus.syncRun(
            ExperimentTaskSummaryQueryCommand.class,
            new ExperimentTaskQueryRequest(taskId)
        );
    }

    @Override
    public Response<List<ExperimentTask>> getExperimentTasksByOuterId(String outerId) {
        List<ExperimentTask> experimentTasks = experimentTaskRepository.findByOuterId(outerId)
            .stream()
            .map(experimentTaskDO -> getExperimentTaskFullInfo(experimentTaskDO.getTaskId()).getResult())
            .collect(Collectors.toList());
        return Response.okWithData(experimentTasks);
    }

    @Override
    public Response<List<ExperimentTask>> getExperimentTasksByExperimentId(String experimentId) {
        List<ExperimentTask> experimentTasks = experimentTaskRepository.findByExperimentId(experimentId)
            .stream()
            .map(experimentTaskDO -> getExperimentTaskFullInfo(experimentTaskDO.getTaskId()).getResult())
            .collect(Collectors.toList());
        return Response.okWithData(experimentTasks);
    }

    @Override
    public Response<ExperimentTaskSimple> getExperimentTaskSimpleInfo(String taskId) {
        Response<ExperimentTaskDO> experimentTaskDOResponse = experimentChecker.assertExperimentTaskExist(taskId);
        if (!experimentTaskDOResponse.isSuccess()) {
            return Response.failedWith(experimentTaskDOResponse.getError());
        }
        return Response.okWithData(
            experimentTaskSimpleInfoFinder.findByExperimentTaskDO(experimentTaskDOResponse.getResult())
        );
    }

    @Override
    public Response<List<ExperimentTaskSimple>> getExperimentTasksSimpleByExperimentId(
        ExperimentTaskRequest experimentTaskRequest) {
        String experimentId = experimentTaskRequest.getExperimentId();
        Date startTime = experimentTaskRequest.getStartTime();
        Date endTime = experimentTaskRequest.getEndTime();

        List<ExperimentTaskDO> experimentTaskDOS;
        if (null != startTime && null != endTime) {
            experimentTaskDOS = experimentTaskRepository.findFinishedTasksBetweenByExperimentId(experimentId, startTime,
                endTime);
        } else {
            experimentTaskDOS = experimentTaskRepository.findFinishedByExperimentId(experimentId);
        }

        ExperimentDO experimentDO = experimentRepository.findById(experimentId).get();
        List<ExperimentTaskSimple> experimentTaskSimples = experimentTaskDOS.stream()
            .map(experimentTaskDO -> experimentTaskSimpleInfoFinder
                .findByExperimentTaskDO(experimentDO, experimentTaskDO))
            .collect(Collectors.toList());

        return Response.okWithData(filterTaskSimples(experimentTaskSimples, experimentTaskRequest));
    }

    @Override
    public Response<ExperimentTaskResult> getExperimentTasksResultByExperimentId(
        ExperimentTaskRequest experimentTaskRequest) {
        List<ExperimentTaskSimple> experimentTaskSimples = this.getExperimentTasksSimpleByExperimentId(
            experimentTaskRequest).getResult();
        ExperimentTaskResult experimentTaskResult = new ExperimentTaskResult();
        Integer totalCount = 0;
        Integer successCount = 0;
        Integer stoppedCount = 0;
        Integer failedCount = 0;
        Integer errorCount = 0;
        Set<String> aoneApps = new HashSet<>();
        Set<String> machines = new HashSet<>();
        Set<String> appDescs = new HashSet<>();
        for (ExperimentTaskSimple experimentTaskSimple : experimentTaskSimples) {
            if (!StateEnum.FINISHED.equals(experimentTaskSimple.getState())) {
                continue;
            }
            if (ResultEnum.SUCCESS.equals(experimentTaskSimple.getResult())) {
                successCount++;
            } else if (ResultEnum.STOPPED.equals(experimentTaskSimple.getResult())) {
                stoppedCount++;
            } else if (ResultEnum.ERROR.equals(experimentTaskSimple.getResult())) {
                errorCount++;
            } else {
                failedCount++;
            }
            aoneApps.addAll(experimentTaskSimple.getAoneApps());
            appDescs.addAll(experimentTaskSimple.getAppDescs());
            machines.addAll(experimentTaskSimple.getHostIps());
            totalCount++;
        }
        experimentTaskResult.setTotalCount(totalCount);
        experimentTaskResult.setSuccessCount(successCount);
        experimentTaskResult.setStoppedCount(stoppedCount);
        experimentTaskResult.setFailedCount(failedCount);
        experimentTaskResult.setErrorCount(errorCount);
        experimentTaskResult.setAoneApps(aoneApps);
        experimentTaskResult.setMachines(machines);
        experimentTaskResult.setAppDescs(appDescs);
        return Response.okWithData(experimentTaskResult);
    }

    @Override
    public Response<Map<String, Integer>> getExperimentTaskResultCount(ExperimentTaskRequest experimentTaskRequest) {
        List<ExperimentTaskSimple> experimentTaskSimples = this.getExperimentTasksSimpleByExperimentId(
            experimentTaskRequest).getResult();
        int successCount = 0;
        int stoppedCount = 0;
        int errorCount = 0;
        int failedCount = 0;
        for (ExperimentTaskSimple experimentTaskSimple : experimentTaskSimples) {
            if (!StateEnum.FINISHED.equals(experimentTaskSimple.getState())) {
                continue;
            }
            if (ResultEnum.SUCCESS.equals(experimentTaskSimple.getResult())) {
                successCount++;
            } else if (ResultEnum.STOPPED.equals(experimentTaskSimple.getResult())) {
                stoppedCount++;
            } else if (ResultEnum.ERROR.equals(experimentTaskSimple.getResult())) {
                errorCount++;
            } else {
                failedCount++;
            }
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(ResultEnum.SUCCESS.name(), successCount);
        result.put(ResultEnum.STOPPED.name(), stoppedCount);
        result.put(ResultEnum.ERROR.name(), errorCount);
        result.put(ResultEnum.FAILED.name(), failedCount);
        return Response.okWithData(result);
    }

    @Override
    public Response<Map<Long, Integer>> getExperimentTaskCount(ExperimentTaskRequest experimentTaskRequest) {
        Date startTime = experimentTaskRequest.getStartTime();
        Date endTime = experimentTaskRequest.getEndTime();
        Map<Long, Integer> result = new TreeMap<>();

        //以月为统计对象
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        while (true) {
            Date sTime = cal.getTime();
            if (sTime.getTime() > endTime.getTime()) {
                break;
            }
            cal.add(Calendar.MONTH, 1);
            Date eTime = cal.getTime();
            if (sTime.getTime() < startTime.getTime()) {
                experimentTaskRequest.setStartTime(startTime);
            } else {
                experimentTaskRequest.setStartTime(sTime);
            }
            experimentTaskRequest.setEndTime(eTime);
            List<ExperimentTaskSimple> experimentTaskSimples =
                this.getExperimentTasksSimpleByExperimentId(experimentTaskRequest).getResult();
            if (null == experimentTaskSimples || experimentTaskSimples.isEmpty()) {
                result.put(sTime.getTime(), 0);
            } else {
                result.put(sTime.getTime(), experimentTaskSimples.size());
            }
        }

        return Response.okWithData(result);
    }

    @Override
    public Response<ExperimentTaskLog> getExperimentTaskLog(String experimentTaskId) {
        return commandBus.syncRun(new ExperimentTaskLogQueryCommand(experimentTaskId));
    }

    @Override
    public Response<ExperimentTaskControl> getControlList(ExperimentTaskRequest experimentTaskRequest) {
        ExperimentTaskResult experimentTaskResult = this.getExperimentTasksResultByExperimentId(experimentTaskRequest)
            .getResult();

        ExperimentTaskControl experimentTaskControl = new ExperimentTaskControl();
        experimentTaskControl.setResultStats(
            Lists.newArrayList(ResultEnum.SUCCESS, ResultEnum.FAILED, ResultEnum.ERROR, ResultEnum.STOPPED));
        experimentTaskControl.setAoneApps(Lists.newArrayList(experimentTaskResult.getAoneApps()));
        experimentTaskControl.setAppDescs(
            sceneFunctionRepository.findNamesByCode(Lists.newArrayList(experimentTaskResult.getAppDescs()))
                .orElse(null));
        return Response.okWithData(experimentTaskControl);
    }

    @Override
    public Response<Map<Long, Double>> getExperimentTaskCost(ExperimentTaskRequest experimentTaskRequest) {
        Date startTime = experimentTaskRequest.getStartTime();
        Date endTime = experimentTaskRequest.getEndTime();
        Map<Long, Double> result = new TreeMap<>();

        experimentTaskRequest.setStartTime(startTime);
        experimentTaskRequest.setEndTime(endTime);
        List<ExperimentTaskSimple> experimentTaskSimples = this.getExperimentTasksSimpleByExperimentId(
            experimentTaskRequest).getResult();

        if (null == experimentTaskSimples || 0 == experimentTaskSimples.size()) {
            result.put(startTime.getTime(), 0.00);
        } else {
            for (ExperimentTaskSimple experimentTaskSimple : experimentTaskSimples) {
                int cnt = 0;
                while (result.containsKey(experimentTaskSimple.getStartTime().getTime() + cnt)) {
                    cnt++;
                }
                BigDecimal bd = BigDecimal.valueOf(
                    (experimentTaskSimple.getEndTime().getTime() - experimentTaskSimple.getStartTime().getTime())
                        / 60000.0);
                result.put(experimentTaskSimple.getStartTime().getTime() + cnt,
                    bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()
                );
            }
        }

        return Response.okWithData(result);
    }

    @Autowired
    private Trackers trackers;

    @Override
    public Response<Void> stopTask(ChaosUser user, String experimentTaskId,
                                   ExperimentTaskStopOption experimentTaskStopOption) {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> experimentTaskManager.stopTask(user, experimentTaskId, experimentTaskStopOption),
            new Consumer<Response<Void>>() {
                @Override
                public void accept(Response<Void> voidResponse) {
                    if (voidResponse.isSuccess()) {
                        trackers.trackExperimentTaskOperation(ChangeActionType.STOP, ChangeOperatorType.USER, user,
                            experimentTaskRepository.findById(experimentTaskId).get().getExperimentId(),
                            experimentTaskId);
                    }
                }
            });
    }

    @Override
    public Response<Void> adminStopTask(ChaosUser user, String experimentTaskId,
                                        ExperimentTaskStopOption experimentTaskStopOption) {
        commandBus.syncRun(ExperimentTaskFinishedCommand.class, experimentTaskId);
        return Response.okWithData(null);
    }

    @Override
    public Response<Void> stopAllTasks(ChaosUser user, BaseExperimentTaskRequest baseExperimentTaskRequest) {
        ExperimentTaskQuery experimentTaskQuery = new ExperimentTaskQuery();
        experimentTaskQuery.setNamespace(baseExperimentTaskRequest.getNamespace());
        experimentTaskQuery.setUser(user);
        experimentTaskQuery.setStates(Lists.newArrayList(StateEnum.RUNNING.getValue(), StateEnum.SUSPEND.getValue()));

        List<ExperimentTaskDO> experimentTaskDOS = experimentTaskRepository.find(experimentTaskQuery);

        ExperimentTaskStopOption experimentTaskStopOption = new ExperimentTaskStopOption();
        experimentTaskDOS.forEach(experimentTaskDO ->
            experimentTaskStopThreadPool.submit(() -> {
                experimentTaskManager.stopTask(user, experimentTaskDO.getTaskId(), experimentTaskStopOption);
            })
        );

        return Response.okWithData(null);
    }

    @Override
    public PageQueryResponse<BaseExperimentTask> pageableQueryExperimentTaskSummary(
        ExperimentPageableQueryRequest experimentPageableQueryRequest) {
        return commandBus.syncRun(ExperimentTaskPageableQueryCommand.class,
            experimentPageableQueryRequest);
    }

    @Override
    public ExperimentTaskDO checkExperimentIsRunning(String experimentId) {
        return experimentTaskRepository.findLatestUnfinishedExperimentTask(experimentId);
    }

    @Override
    public Response<ExperimentTaskGuardsResult> queryExperimentTaskGuardInfo(
        BaseExperimentTaskRequest baseExperimentTaskRequest) {
        return commandBus.syncRun(ExperimentTaskGuardInfoQueryCommand.class, baseExperimentTaskRequest);
    }

    @Override
    public PageQueryResponse<BaseExperimentTask> pageableQueryExperimentTaskByUserId(
        ExperimentTaskPageableQueryRequest experimentTaskPageableQueryRequest) {
        return commandBus.syncRun(ExperimentTaskPageableQueryByUserIdCommand.class,
            experimentTaskPageableQueryRequest);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        experimentTaskStopThreadPool = new ThreadPoolExecutor(
            2,
            5,
            2,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            ThreadPoolExecutors.defaultThreadFactory("ExperimentTask-Stop")
        );
        log.info("init experiment task stop thread pool success");
    }

    private List<ExperimentTaskSimple> filterTaskSimples(List<ExperimentTaskSimple> experimentTaskSimples,
        ExperimentTaskRequest experimentTaskRequest) {
        Set<String> aoneApps = experimentTaskRequest.getAoneApps();
        Set<String> appDescs = experimentTaskRequest.getAppDescs();
        Set<ResultEnum> resultEnums = experimentTaskRequest.getResultEnums();

        return experimentTaskSimples.stream()
            .filter(experimentTaskSimple -> {
                if (!StateEnum.FINISHED.equals(experimentTaskSimple.getState())) {
                    return false;
                }
                ResultEnum result = experimentTaskSimple.getResult();
                if (!CollectionUtil.isNullOrEmpty(aoneApps) && !judgeCross(
                    experimentTaskSimple.getAoneApps(),
                    aoneApps
                )) {
                    return false;
                }
                if (!CollectionUtil.isNullOrEmpty(appDescs) && !judgeCross(
                    experimentTaskSimple.getAppDescs(),
                    appDescs
                )) {
                    return false;
                }
                if (!CollectionUtil.isNullOrEmpty(resultEnums)) {
                    if (ResultEnum.FAILED.equals(result) || ResultEnum.SOPPED_FAILED.equals(result)
                        || ResultEnum.REJECTED.equals(result)) {
                        return resultEnums.contains(ResultEnum.FAILED);
                    } else {
                        return resultEnums.contains(result);
                    }
                }
                return true;
            })
            .collect(Collectors.toList());
    }

    private Boolean judgeCross(Set<String> set1, Set<String> set2) {
        if (null == set1 || null == set2) {
            return false;
        }

        Set<String> s = new HashSet<>(set1);
        s.retainAll(set2);

        return !s.isEmpty();
    }

}
