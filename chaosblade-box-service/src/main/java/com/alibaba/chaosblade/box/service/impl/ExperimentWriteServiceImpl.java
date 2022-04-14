package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRunResult;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes.*;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.*;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInit;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.command.ExperimentExecutionCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.Trackers;
import com.alibaba.chaosblade.box.common.infrastructure.util.ChangeLogExecutor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;
import com.alibaba.chaosblade.box.service.ExperimentWriteService;
import com.alibaba.chaosblade.box.service.command.experiment.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author haibin.lhb
 *
 *
 */
@Service
public class ExperimentWriteServiceImpl implements ExperimentWriteService {

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private Trackers trackers;

    @Override
    public Response<ExperimentRunResult> runExperiment(ChaosUser user, ExperimentRunRequest experimentRunRequest)
        throws ChaosException {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentExecutionCommand.class, experimentRunRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentTaskOperation(ChangeActionType.RUN, ChangeOperatorType.USER, user,
                        experimentRunRequest.getExperimentId(), experimentRunResultResponse.getResult().getTaskId());
                }
            });
    }

    @Override
    public Response<String> createExperiment(ExperimentCreateRequest experimentCreateRequest)
        throws ChaosException {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentCreateCommand.class, experimentCreateRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangeActionType.ADD, ChangeOperatorType.USER,
                        experimentCreateRequest.getUser(),
                        experimentRunResultResponse.getResult(), null);
                }
            });
    }

    @Override
    public Response<Boolean> updateExperiment(ExperimentUpdateRequest experimentUpdateRequest)
        throws ChaosException {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentUpdateCommand.class, experimentUpdateRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangelogTypes.ChangeActionType.Update, ChangeOperatorType.USER,
                        experimentUpdateRequest.getUser(),
                        experimentUpdateRequest.getExperimentId(), null);
                }
            });
    }

    @Override
    public Response<Boolean> updateExperimentBasicInfo(ChaosUser user, ExperimentUpdateRequest experimentUpdateRequest)
        throws ChaosException {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentUpdateCommand.class, experimentUpdateRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangeActionType.Update, ChangeOperatorType.USER,
                        experimentUpdateRequest.getUser(),
                        experimentUpdateRequest.getExperimentId(), null);
                }
            });
    }

    @Override
    public Response<String> cloneExperiment(ExperimentCloneRequest experimentCloneRequest) {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentCloneCommand.class, experimentCloneRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangeActionType.ADD, ChangeOperatorType.USER,
                        experimentCloneRequest.getUser(),
                        experimentRunResultResponse.getResult(), null);
                }
            });
    }

    @Override
    public Response<Void> deleteExperiment(ExperimentDeleteRequest experimentDeleteRequest) {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentDeleteCommand.class, experimentDeleteRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangeActionType.ADD, ChangeOperatorType.USER,
                        experimentDeleteRequest.getUser(),
                        experimentDeleteRequest.getExperimentId(), null);
                }
            });
    }

    @Override
    public Response<Void> updateExperimentDefinition(ExperimentDefinitionRequest flowDefinitionCreateRequest) {
        return ChangeLogExecutor.executeWithChangeLog(
            () -> commandBus.syncRun(ExperimentDefinitionUpdateCommand.class, flowDefinitionCreateRequest),
            experimentRunResultResponse -> {
                if (experimentRunResultResponse.isSuccess()) {
                    trackers.trackExperimentOperation(ChangeActionType.Update, ChangeOperatorType.USER,
                        flowDefinitionCreateRequest.getUser(),
                        flowDefinitionCreateRequest.getExperimentId(), null);
                }
            });
    }

    @Override
    public Response<Map<String, List<ExperimentActivityInfo>>> initMiniFlowByAppCode(
        InitMiniFlowRequest initMiniFlowRequest) {
        return commandBus.syncRun(InitMiniFlowByAppCodeCommand.class, initMiniFlowRequest);
    }

    @Override
    public Response<Boolean> updateExperimentForOpenApi(ExperimentUpdateRequest updateRequest) {
        return commandBus.syncRun(ExperimentUpdateForOpenApiCommand.class, updateRequest);
    }

    @Override
    public Response<ExperimentRunResult> preCheckExperiment(ChaosUser user, ExperimentRunRequest experimentRunRequest)
        throws ChaosException {
        return commandBus.syncRun(ExperimentExecutionCommand.class, experimentRunRequest);
    }

    @Override
    public Response<ExperimentInit> initExperimentByAppCode(InitMiniFlowRequest initMiniFlowRequest) {
        commandBus.syncRun(InitMiniFlowByAppCodeCommand.class, initMiniFlowRequest);
        return null;
    }

    @Override
    public Response<Boolean> updateExperimentHost(ExperimentHostUpdateRequest experimentHostUpdateRequest) throws ChaosException {
        return commandBus.syncRun(ExperimentUpdateHostCommand.class, experimentHostUpdateRequest);
    }
}
