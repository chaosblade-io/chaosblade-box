package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.BaseExperimentRequest;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentCreatedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.assembler.ExperimentDOAssembler;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.WorkspaceService;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
@Slf4j
public class ExperimentCreateCommand extends SpringBeanCommand<ExperimentCreateRequest, Response<String>> {

    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private ThrowableChaosErrorWrappers throwableChaosErrorWrappers;
//    @Autowired
//    private WorkspaceManager workspaceManager;

    @Resource
    private WorkspaceService workspaceService;

    @Override
    public Response<String> execute(ExperimentCreateRequest experimentCreateRequest) {
        return transactionUtil.execute(status -> {
            try {
                ExperimentDO experimentDO = internalExecute(experimentCreateRequest);
                chaosEventDispatcher.fireEvent(
                    new ExperimentCreatedEvent(experimentCreateRequest.getUser(), experimentDO));
                return Response.okWithData(experimentDO.getExperimentId());
            } catch (Exception exception) {
                log.error("create experiment failed", exception);
                status.setRollbackOnly();
                return Response.failedWith(
                    throwableChaosErrorWrappers.wrapper(exception, CommonErrorCode.B_EXPERIMENT_CREATE_FAILED));
            }
        });
    }

    private ExperimentDO internalExecute(ExperimentCreateRequest experimentCreateRequest) {
        ExperimentDO experimentDO = createExperimentDO(experimentCreateRequest);
        experimentCreateRequest.getDefinition().setExperimentDO(experimentDO);
        handleDefinition(experimentCreateRequest);
        handleTags(experimentCreateRequest, experimentDO);
        handleWorkspaces(experimentCreateRequest, experimentDO);
        return experimentDO;
    }

    private void handleWorkspaces(ExperimentCreateRequest experimentCreateRequest, ExperimentDO experimentDO) {
        HashSet<String> workspaces = new HashSet<>();
        if (experimentCreateRequest.getWorkspaceId() != null) {
            workspaces.add(experimentCreateRequest.getWorkspaceId());
        }
        if (!CollectionUtil.isNullOrEmpty(experimentCreateRequest.getWorkspaces())) {
            workspaces.addAll(experimentCreateRequest.getWorkspaces());
        }
        if (!workspaces.isEmpty()) {
            workspaceService.addWorkspaceExperiments(experimentDO.getExperimentId(), experimentDO.getName(),
                    experimentDO.getNamespace(), new ArrayList<>(workspaces));
        }
    }

    protected void handleTags(BaseExperimentRequest baseExperimentRequest, ExperimentDO experimentDO) {
        List<String> tags = baseExperimentRequest.getTags();
        if (!CollectionUtil.isNullOrEmpty(tags)) {
            tagManager.addTags(baseExperimentRequest.getUserId(), experimentDO.getExperimentId(),
                TagDO.TAG_TYPE_EXPERIMENT, tags);
        }
    }

    protected ExperimentDO createExperimentDO(ExperimentCreateRequest experimentCreateRequest)
        throws ChaosException {
        ExperimentDO experimentDO = initExperimentDO(experimentCreateRequest);
        experimentDO.setState(ExperimentStateEnum.READY.getValue());
        experimentDO.setOuterId(experimentCreateRequest.getOuterId());
        experimentDO.setIsTemplate(false);
        experimentDO.setIsDelete(false);
        experimentDO.setAttributes(experimentCreateRequest.getAttributes());
        //保存来源
        experimentDO.setSource(experimentCreateRequest.getSource());
        experimentRepository.add(experimentDO);
        return experimentDO;
    }

    protected void handleDefinition(ExperimentCreateRequest experimentCreateRequest) {
        ExperimentDefinitionRequest experimentDefinitionRequest = experimentCreateRequest.getDefinition();
        BaseRequest.copy(experimentCreateRequest, experimentDefinitionRequest);
        experimentDefinitionRequest.setExperimentId(experimentCreateRequest.getExperimentId());
        Response<Void> response = commandBus.syncRun(ExperimentDefinitionCreateCommand.class,
            experimentDefinitionRequest);
        if (!response.isSuccess()) {
            throw new ChaosException(response.getError().getErrorCode(), response.getError().getErrorMessage());
        }
    }

    protected ExperimentDO initExperimentDO(ExperimentCreateRequest experimentCreateRequest) {
        return ExperimentDOAssembler.assembleExperimentDO(experimentCreateRequest);
    }
}
