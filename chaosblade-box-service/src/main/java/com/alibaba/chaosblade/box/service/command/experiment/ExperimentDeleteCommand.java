package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentDeleteRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentDeletedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.query.ExperimentQuery;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * @author haibin
 * 
 * 
 */
@Component
public class ExperimentDeleteCommand extends SpringBeanCommand<ExperimentDeleteRequest, Response<Void>> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Override
    public String getCommandExecutorName() {
        return CommandExecutorConstant.EXECUTOR_EXPERIMENT;
    }

    @Override
    public Response<Void> execute(ExperimentDeleteRequest experimentDeleteRequest) {
        String experimentId = experimentDeleteRequest.getExperimentId();
        Optional<ExperimentDO> experimentDOOptional = experimentRepository.findById(experimentId);
        if (!experimentDOOptional.isPresent()) {
            return Response.failedWith(new ChaosError(CommonErrorCode.P_EXPERIMENT_NOT_FOUND));
        }

        ExperimentDO experimentDO = experimentDOOptional.get();
        if (experimentDO.isRunning()) {
            return Response.failedWith(new ChaosError(CommonErrorCode.B_EXPERIMENT_ALREADY_RUNNING));
        }

        if (Strings.isNullOrEmpty(experimentId)) {
            return Response.ok();
        }

        ExperimentQuery query = new ExperimentQuery();
        query.setExperimentId(experimentId);
        boolean success = experimentRepository.logicDelete(query);
        if (success && !experimentDO.getIsTemplate()) {
            chaosEventDispatcher.fireEvent(
                new ExperimentDeletedEvent(experimentDeleteRequest.getUser(), experimentDO.getExperimentId()));
            tagManager.unbindTagsByExperimentId(experimentId);
        }
        return Response.ok();
    }

}
