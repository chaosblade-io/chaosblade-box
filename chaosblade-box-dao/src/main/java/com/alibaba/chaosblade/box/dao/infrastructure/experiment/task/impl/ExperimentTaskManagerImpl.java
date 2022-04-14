package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.task.BaseExperimentTask;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTaskStopOption;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentTaskStat;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentTaskStatRequest;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentTaskStatRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentTaskStopRequest;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskCreateCommand;
import com.alibaba.chaosblade.box.dao.command.task.ExperimentTaskStopCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskManager;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentTaskQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentTaskManagerImpl implements ExperimentTaskManager {

    @Resource
    private ExperimentTaskRepository experimentTaskRepository;

    @Resource
    private ExperimentTaskUtil experimentTaskStatUtil;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ExperimentTaskUtil experimentTaskUtil;

    @Override
    public Response<ExperimentTaskDO> createTask(ExperimentTaskCreateRequest experimentTaskCreateRequest) {
        return commandBus.syncRun(ExperimentTaskCreateCommand.class, experimentTaskCreateRequest);
    }

    @Override
    public Response<Void> stopTask(ChaosUser user, String experimentTaskId,
                                   ExperimentTaskStopOption experimentTaskStopOption) {
        ExperimentTaskStopRequest experimentTaskStopRequest = new ExperimentTaskStopRequest();
        experimentTaskStopRequest.setUser(user);
        experimentTaskStopRequest.setTaskId(experimentTaskId);
        return commandBus.syncRun(ExperimentTaskStopCommand.class, experimentTaskStopRequest);
    }

    @Override
    public Response<ExperimentStat> getUserExperimentTaskStatistics(
        UserExperimentTaskStatRequest userExperimentTaskStatRequest) {
        ExperimentTaskQuery query = new ExperimentTaskQuery();
        query.setUser(userExperimentTaskStatRequest.getUser());
        query.setNamespace(userExperimentTaskStatRequest.getNamespace());
        List<ExperimentTaskDO> experimentTaskDOS = experimentTaskRepository.find(query);
        long runningCount = 0;
        long finishedCount = 0;
        long exceptionCount = 0;
        long failureCount = 0;
        long totalCount = 0;
        totalCount = experimentTaskDOS.size();
        for (ExperimentTaskDO experimentTaskDO : experimentTaskDOS) {
            if (experimentTaskDO.isFinished()) {
                finishedCount++;
                ResultEnum resultEnum = ResultEnum.of(experimentTaskDO.getResult());
                if (ResultEnum.ERROR.equals(resultEnum)) {
                    exceptionCount++;
                } else if (ResultEnum.FAILED.equals(resultEnum)) {
                    failureCount++;
                }
            } else {
                runningCount++;
            }
        }
        ExperimentStat experimentStat = new ExperimentStat();
        experimentStat.setException(exceptionCount);
        experimentStat.setFailure(failureCount);
        experimentStat.setFinished(finishedCount);
        experimentStat.setRunning(runningCount);
        experimentStat.setTotal(totalCount);
        return Response.okWithData(experimentStat);
    }

    @Override
    public Response<ExperimentTaskStat> doExperimentTaskStatistic(ExperimentTaskStatRequest experimentTaskStatRequest) {
        List<ExperimentTaskDO> experimentTaskDOS = experimentTaskRepository.findTasksBetweenByExperimentId(
            experimentTaskStatRequest.getExperimentId(),
            experimentTaskStatRequest.getStartTime(),
            experimentTaskStatRequest.getEndTime()
        );

        ExperimentTaskStat experimentTaskStat = experimentTaskStatUtil.doStatistic(experimentTaskDOS);
        experimentTaskStat.setExperimentId(experimentTaskStatRequest.getExperimentId());
        return Response.okWithData(experimentTaskStat);
    }

    @Override
    public Response<BaseExperimentTask> getBaseExperimentTaskByTaskId(String experimentTaskId) {
        Response<ExperimentTaskDO> response = experimentChecker.assertExperimentTaskExist(experimentTaskId);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        BaseExperimentTask baseExperimentTask = new BaseExperimentTask();
        experimentTaskUtil.fillBaseInfo(baseExperimentTask, response.getResult(), null);
        return Response.okWithData(baseExperimentTask);
    }

}
