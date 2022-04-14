package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentCloneRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentCloneCommand extends SpringBeanCommand<ExperimentCloneRequest, Response<String>> {

    @Autowired
    protected TransactionUtil transactionUtil;

    @Override
    public Response<String> execute(ExperimentCloneRequest experimentCloneRequest) {
        String experimentId = experimentCloneRequest.getExperimentId();
        BaseExperimentRequest request = new BaseExperimentRequest();
        request.setExperimentId(experimentId);
        request.setNamespace(experimentCloneRequest.getNamespace());
        Response<ExperimentInfo> experimentDOResponse = commandBus.syncRun(ExperimentInfoQueryCommand.class,
                request);
        if (!experimentDOResponse.isSuccess()) {
            return Response.failedWith(experimentDOResponse.getError());
        }
        ExperimentInfo experimentInfo = experimentDOResponse.getResult();
        ExperimentCreateRequest experimentCreateRequest = new ExperimentCreateRequest();
        BaseRequest.copy(experimentCloneRequest, experimentCreateRequest);
        fillExperimentCreateRequest(experimentCloneRequest, experimentCreateRequest, experimentInfo);
        return commandBus.syncRun(ExperimentCreateCommand.class, experimentCreateRequest);
    }

    private void fillExperimentCreateRequest(ExperimentCloneRequest experimentCloneRequest,
        ExperimentCreateRequest experimentCreateRequest, ExperimentInfo experimentInfo) {
        //填充基本信息
        ExperimentBasicInfo experimentBasicInfo = experimentInfo.getBasicInfo();
        String name = Strings.isNullOrEmpty(experimentBasicInfo.getName())
            ? experimentBasicInfo.getName() + "-副本"
            : experimentCloneRequest.getName();
        experimentCreateRequest.setName(name);
        experimentCreateRequest.setDescription(experimentBasicInfo.getDescription());
        experimentCreateRequest.setTags(experimentBasicInfo.getTags());
        experimentCreateRequest.setMiniAppDesc(experimentBasicInfo.getMiniAppDesc());
        experimentCreateRequest.setRelations(experimentBasicInfo.getRelations());
        //填充流程信息
        ExperimentFlowInfo experimentFlowInfo = experimentInfo.getFlowInfo();
        if (experimentFlowInfo != null) {
            ExperimentFlowInfoClear.clearAllIds(experimentFlowInfo);
            ExperimentDefinitionRequest experimentDefinitionRequest = new ExperimentDefinitionRequest();
            experimentDefinitionRequest.setFlowGroups(experimentFlowInfo.getFlowGroups());
            experimentDefinitionRequest.setRunMode(experimentFlowInfo.getRunMode());
            experimentDefinitionRequest.setGuardConf(experimentFlowInfo.getGuardConf());
            experimentDefinitionRequest.setSchedulerConfig(experimentFlowInfo.getSchedulerConfig());
            experimentDefinitionRequest.setDuration(experimentFlowInfo.getDuration());
            experimentCreateRequest.setDefinition(experimentDefinitionRequest);
        }
    }

}
