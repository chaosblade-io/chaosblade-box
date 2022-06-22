package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentInfo;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentInfoQueryCommand;
import com.alibaba.chaosblade.box.service.model.expertise.ExperimentExpertiseCreateRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseBasicInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseExecutableInfo;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

/**
 * 将演练转换成专家经验库
 *
 * @author haibin.lhb
 *
 * 
 */
@Component
public class ExperimentToExpertiseConvertCommand extends SpringBeanCommand<BaseExperimentRequest, Response<String>> {

    private final static String DEFAULT = "To be added";


    @Override
    public Response<String> execute(BaseExperimentRequest baseExperimentRequest) {
        //首先查询演练详情
        String experimentId = baseExperimentRequest.getExperimentId();
        BaseExperimentRequest request = new BaseExperimentRequest();
        request.setExperimentId(experimentId);
        request.setNamespace(baseExperimentRequest.getNamespace());
        Response<ExperimentInfo> experimentDOResponse = commandBus.syncRun(ExperimentInfoQueryCommand.class,
            request);
        if (!experimentDOResponse.isSuccess()) {
            return Response.failedWith(experimentDOResponse.getError());
        }
        ExperimentInfo experimentInfo = experimentDOResponse.getResult();
        ExpertiseBasicInfo basicInfo = buildExpertiseBasicInfo(experimentInfo);
        ExpertiseExecutableInfo executableInfo = buildExpertiseExecutableInfo(experimentInfo);
        ExperimentExpertiseCreateRequest experimentExpertiseCreateRequest = new ExperimentExpertiseCreateRequest();
        experimentExpertiseCreateRequest.setUser(baseExperimentRequest.getUser());
        experimentExpertiseCreateRequest.setNamespace(baseExperimentRequest.getNamespace());
        experimentExpertiseCreateRequest.setBasicInfo(basicInfo);
        experimentExpertiseCreateRequest.setExecutableInfo(executableInfo);
        experimentExpertiseCreateRequest.setLang(baseExperimentRequest.getLang());
        return commandBus.syncRun(ExpertiseCreateCommand.class, experimentExpertiseCreateRequest);

    }

    private ExpertiseExecutableInfo buildExpertiseExecutableInfo(ExperimentInfo experimentInfo) {
        ExpertiseExecutableInfo expertiseExecutableInfo = new ExpertiseExecutableInfo();
        ExperimentFlowInfo experimentFlowInfo = experimentInfo.getFlowInfo();
        ExperimentFlowInfoClear.clearAllIds(experimentFlowInfo);
        ExperimentFlowInfoClear.clearApplicationAndHosts(experimentFlowInfo);
        expertiseExecutableInfo.setFlow(experimentFlowInfo);
        return expertiseExecutableInfo;
    }

    private ExpertiseBasicInfo buildExpertiseBasicInfo(ExperimentInfo experimentInfo) {
        ExpertiseBasicInfo expertiseBasicInfo = new ExpertiseBasicInfo();
        expertiseBasicInfo.setName(experimentInfo.getBasicInfo().getName());
        expertiseBasicInfo.setState(ExpertiseConstant.STATE_ENABLED);
        expertiseBasicInfo.setFunctionDesc(Strings.isNullOrEmpty(
                experimentInfo.getBasicInfo().getDescription())?DEFAULT:experimentInfo.getBasicInfo().getDescription());
        expertiseBasicInfo.setBackgroundDesc(DEFAULT);
        expertiseBasicInfo.setDesignConcept(DEFAULT);
        return expertiseBasicInfo;
    }
}
