package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentFlowInitByExpertiseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentFlowDefinitionQueryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class InitFlowByExpertiseCommand
    extends SpringBeanCommand<ExperimentFlowInitByExpertiseRequest, Response<ExperimentFlowInfo>> {

    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public Response<ExperimentFlowInfo> execute(ExperimentFlowInitByExpertiseRequest experimentExpertiseQueryRequest) {
        ExpertiseDO expertiseDO = expertiseRepository.findById(experimentExpertiseQueryRequest.getExpertiseId()).orElse(
            null);
        if (expertiseDO == null) {
            return Response.failedWith(CommonErrorCode.P_NOT_FOUND_EXPERTISE);
        }
        ExperimentDO experimentDO = experimentRepository.findById(expertiseDO.getExperimentId()).orElse(null);
        ExperimentFlowInfo experimentFlowInfo = commandBus.syncRun(ExperimentFlowDefinitionQueryCommand.class,
            experimentDO);
        ExperimentFlowInfoClear.clearAllIds(experimentFlowInfo);
        experimentFlowInfo.setExperimentId(null);
        return Response.okWithData(experimentFlowInfo);
    }

}
