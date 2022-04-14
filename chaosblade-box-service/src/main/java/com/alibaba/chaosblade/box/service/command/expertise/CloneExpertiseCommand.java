package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.experiment.clear.ExperimentFlowInfoClear;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseCloneRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseInfo;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class CloneExpertiseCommand extends SpringBeanCommand<ExpertiseCloneRequest, Response<ExpertiseInfo>> {

    @Override
    public Response<ExpertiseInfo> execute(ExpertiseCloneRequest expertiseCloneRequest) {
        ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest = new ExperimentExpertiseQueryRequest();
        experimentExpertiseQueryRequest.setExpertiseId(expertiseCloneRequest.getExpertiseId());
        Response<ExpertiseInfo> response = commandBus.syncRun(QueryExpertiseDetailsCommand.class,
            experimentExpertiseQueryRequest);
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        ExperimentFlowInfoClear.clearAllIds(response.getResult().getExecutableInfo().getFlow());
        ExpertiseInfo expertiseInfo = response.getResult();
        expertiseInfo.setExpertiseId(null);
        return Response.okWithData(expertiseInfo);
    }
}
