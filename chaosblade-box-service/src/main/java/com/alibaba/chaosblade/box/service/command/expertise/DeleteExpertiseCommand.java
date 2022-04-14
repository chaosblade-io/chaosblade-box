package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExpertiseDeletedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class DeleteExpertiseCommand extends SpringBeanCommand<ExperimentExpertiseQueryRequest, Response> {
    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private TagManager tagManager;

    @Override
    public Response execute(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
        ExpertiseDO expertiseDO = expertiseRepository.findById(experimentExpertiseQueryRequest.getExpertiseId()).orElse(
            null);
        if (expertiseDO == null) {
            return Response.failedWith(CommonErrorCode.P_NOT_FOUND_EXPERTISE);
        } else {
            expertiseDO.setState(ExpertiseConstant.STATE_DELETED);
            expertiseRepository.update(expertiseDO);
            tagManager.unbindTagsByExpertiseId(expertiseDO.getExpertiseId());
            chaosEventDispatcher.fireEvent(new ExpertiseDeletedEvent(expertiseDO));
        }
        return Response.ok();
    }
}
