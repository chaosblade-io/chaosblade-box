package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.service.infrastructure.convertor.ExperimentToBasicInfoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentBasicInfoQueryCommand
    extends SpringBeanCommand<BaseExperimentRequest, Response<ExperimentBasicInfo>> {

    @Autowired
    private ExperimentChecker experimentChecker;

    @Autowired
    private ExperimentToBasicInfoConverter experimentToBasicInfoConverter;

    @Override
    public Response<ExperimentBasicInfo> execute(BaseExperimentRequest baseExperimentRequest) {
        String experimentId = baseExperimentRequest.getExperimentId();
        Response<ExperimentDO> experimentDOResponse = experimentChecker.assertExperimentExist(experimentId);
        if (!experimentDOResponse.isSuccess()) {
            return Response.failedWith(experimentDOResponse.getError());
        }
        ExperimentDO experimentDO = experimentDOResponse.getResult();
        return Response.okWithData(experimentToBasicInfoConverter.convert(experimentDO));
    }
}
