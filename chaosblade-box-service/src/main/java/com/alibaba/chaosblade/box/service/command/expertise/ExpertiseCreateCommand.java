package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExpertiseCreateEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentDefinitionCreateCommand;
import com.alibaba.chaosblade.box.service.model.expertise.ExperimentExpertiseCreateRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseBasicInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseExecutableInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExpertiseCreateCommand extends BaseExpertiseCommand<ExperimentExpertiseCreateRequest> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Override
    protected void handleExpertise(ExperimentExpertiseCreateRequest experimentExpertiseCreateRequest,
        ExpertiseDO expertiseDO) {
        expertiseRepository.add(expertiseDO);
        if (!experimentExpertiseCreateRequest.isConfigSync()) {
            chaosEventDispatcher.fireEvent(new ExpertiseCreateEvent(expertiseDO));
        }
    }

    @Override
    protected Response<String> handleThrowable(ExperimentExpertiseCreateRequest expertiseOperationRequest,
                                               Throwable throwable) {
        return Response.failedWith(throwableChaosErrorWrappers.wrapper(throwable, "创建经验失败"));
    }

    @Override
    protected ChaosError handleExecutionInfo(ExperimentExpertiseCreateRequest ex,
                                             ExpertiseDO expertiseDO, ExpertiseExecutableInfo expertiseExecutableInfo) {
        fillRuntimeInfo(expertiseDO, expertiseExecutableInfo);
        ExperimentFlowInfo experimentFlowInfo = expertiseExecutableInfo.getFlow();
        ExperimentDO experimentDO = addExperimentDO(expertiseDO.getExpertiseId());
        expertiseDO.setExperimentId(experimentDO.getExperimentId());
        ExperimentDefinitionRequest experimentDefinitionRequest = buildExperimentDefinitionRequest(
            ex, experimentDO.getExperimentId(), experimentFlowInfo);
        return commandBus.syncRun(ExperimentDefinitionCreateCommand.class,
            experimentDefinitionRequest).getError();
    }

    private ExperimentDO addExperimentDO(String expertiseId) {
        ExperimentDO experimentDO = new ExperimentDO();
        experimentDO.setName("expertise_template_" + expertiseId);
        experimentDO.setOuterId(expertiseId);
        experimentDO.setIsTemplate(true);
        experimentDO.setUserId(ChaosUser.SYSTEM.getUserId());
        experimentDO.setState(ExperimentStateEnum.READY.getValue());
        experimentRepository.add(experimentDO);
        return experimentDO;
    }

    @Override
    protected ExpertiseDO handleBasicInfo(ExperimentExpertiseCreateRequest experimentExpertiseCreateRequest,
        ExpertiseBasicInfo basicInfo) {
        ExpertiseDO expertiseDO = new ExpertiseDO();
        if (experimentExpertiseCreateRequest.isConfigSync()) {
            expertiseDO.setExpertiseId(experimentExpertiseCreateRequest.getExpertiseId());
        } else {
            expertiseDO.setExpertiseId(IdWorker.getIdStr());
        }
        expertiseDO.setNamespace(experimentExpertiseCreateRequest.getNamespace());
        expertiseDO.setBackgroundDesc(basicInfo.getBackgroundDesc());
        expertiseDO.setDesignConcept(basicInfo.getDesignConcept());
        expertiseDO.setName(basicInfo.getName());
        expertiseDO.setFunctionDesc(basicInfo.getFunctionDesc());
        int state = basicInfo.getState() == null ? ExpertiseConstant.STATE_DISABLE : basicInfo.getState();
        expertiseDO.setState(state);
        ChaosUser user = experimentExpertiseCreateRequest.getUser();
        if (null != user) {
            expertiseDO.setUserId(experimentExpertiseCreateRequest.getUser().getUserId());
        }
        return expertiseDO;
    }

}
