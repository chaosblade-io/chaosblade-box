package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.experiment.log.ExperimentOperationLog;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentAppRiskMessageRequest;
import com.alibaba.chaosblade.box.common.experiment.request.ExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.experiment.request.UserExperimentStatRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentAppRisk;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentSimpleInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.dao.command.ListExperimentOperationLogCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.ExperimentReadService;
import com.alibaba.chaosblade.box.service.command.experiment.*;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Service
@Order
public class ExperimentReadServiceImpl implements ExperimentReadService {

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ExperimentChecker experimentChecker;

    @Override
    public Response checkExperimentRunnable(ChaosUser user, String experimentId) {
        Response<ExperimentDO> response = experimentChecker.assertExperimentExist(experimentId);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        ExperimentDO experimentDO = response.getResult();
        ExperimentRunRequest experimentRunRequest = new ExperimentRunRequest();
        experimentRunRequest.setExperimentId(experimentId);
        experimentRunRequest.setUser(user);
        ChaosError ChaosError = experimentChecker.checkExperimentRunnable(experimentRunRequest, experimentDO);
        if (ChaosError != null) {
            return Response.failedWith(ChaosError);
        }
        return Response.ok();
    }

    @Override
    public Response<ExperimentInfo> getExperiment(BaseExperimentRequest request) {
        return commandBus.syncRun(ExperimentInfoQueryCommand.class, request);
    }

    @Override
    public Response<ExperimentStat> getUserExperimentStatistics(UserExperimentStatRequest userExperimentStatRequest) {
        return Response.okWithData(commandBus.syncRun(ExperimentStatCommand.class, userExperimentStatRequest));
    }

    @Override
    public Response<PageQueryResponse<UserExperiment>> searchExperiments(
        UserExperimentPageableQueryRequest userExperimentPageableQueryRequest) {
        return commandBus.syncRun(UserPageableQueryUserExperimentsCommand.class,
            userExperimentPageableQueryRequest);
    }

    @Override
    public Response<ExperimentBasicInfo> getExperimentBasicInfo(BaseExperimentRequest baseExperimentRequest) {
        return commandBus.syncRun(ExperimentBasicInfoQueryCommand.class, baseExperimentRequest);
    }

    @Override
    public Response<PageableResponse<ExperimentOperationLog>> listExperimentOperationLogs(
        ExperimentPageableQueryRequest experimentPageableQueryRequest) {
        return commandBus.syncRun(ListExperimentOperationLogCommand.class,
            experimentPageableQueryRequest);
    }

    @Override
    public Response<ExperimentSimpleInfo> getExperimentSimpleInfoForChaos(String experimentId) {
        return commandBus.syncRun(ExperimentSimpleInfoQueryForChaosCommand.class, experimentId);
    }

    @Override
    public Response<List<ExperimentAppRisk>> getExperimentAppRisk(ExperimentAppRiskMessageRequest request) {
        return commandBus.syncRun(ExperimentAppRiskCommand.class, request.getExperimentId());
    }
}
