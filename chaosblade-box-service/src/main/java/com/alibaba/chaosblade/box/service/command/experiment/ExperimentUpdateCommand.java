package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.dao.infrastructure.checker.ExperimentChecker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentUpdateRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentUpdatedEvent;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.WorkspaceService;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExperimentUpdateCommand extends SpringBeanCommand<ExperimentUpdateRequest, Response<Boolean>> {

    @Autowired
    private ExperimentChecker experimentChecker;
    @Autowired
    private TransactionUtil transactionUtil;

    @Autowired
    private ChaosEventDispatcher chaosEventDispatcher;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private ExperimentRepository experimentRepository;

//    @Autowired
//    private WorkspaceManager workspaceManager;
    @Resource
    private WorkspaceService workspaceService;

    @Override

    public Response<Boolean> execute(ExperimentUpdateRequest experimentUpdateRequest) {
        Response<ExperimentDO> response = experimentChecker
            .assertExperimentExist(
                experimentUpdateRequest.getExperimentId());
        if (!response.isSuccess()) {
            return Response.failedWith(response.getError());
        }
        ExperimentDO experimentDO = response.getResult();
        return transactionUtil.execute(new TransactionCallback<Response<Boolean>>() {
            @Override
            public Response<Boolean> doInTransaction(TransactionStatus status) {
                try {
                    internalExecute(experimentDO, experimentUpdateRequest);
                    chaosEventDispatcher.fireEvent(
                        new ExperimentUpdatedEvent(experimentUpdateRequest.getUser(), experimentDO));
                    return Response.okWithData(true);
                } catch (Exception ex) {
                    status.setRollbackOnly();
                    log.error("update experiment failed", ex);
                    return Response.failedWith(CommonErrorCode.B_EXPERIMENT_UPDATE, ex.getMessage());
                }
            }
        });

    }

    private void internalExecute(ExperimentDO experimentDO, ExperimentUpdateRequest experimentUpdateRequest) {
        ExperimentDefinitionRequest experimentDefinitionRequest = experimentUpdateRequest.getDefinition();
        if (experimentDefinitionRequest != null) {
            BaseRequest.copy(experimentUpdateRequest, experimentDefinitionRequest);
            experimentDefinitionRequest.setExperimentDO(experimentDO);
            Response response = commandBus.syncRun(ExperimentDefinitionUpdateCommand.class,
                experimentDefinitionRequest);
            if (!response.isSuccess()) {
                throw new ChaosException(response.getError());
            }
        } else {
            updateExperimentDO(experimentUpdateRequest, experimentDO);
            handleTags(experimentUpdateRequest, experimentDO);
            bindWorkspaces(experimentDO, experimentUpdateRequest.getWorkspaces());
            experimentRepository.update(experimentDO);
        }

    }

    private void updateExperimentDO(ExperimentUpdateRequest experimentUpdateRequest, ExperimentDO experimentDO) {
        if (!Strings.isNullOrEmpty(experimentUpdateRequest.getDescription())) {
            experimentDO.setDescription(experimentUpdateRequest.getDescription());
        }
        if (!Strings.isNullOrEmpty(experimentUpdateRequest.getName())) {
            experimentDO.setName(experimentUpdateRequest.getName());
        }
        if (null != experimentUpdateRequest.getMiniAppDesc() && !experimentUpdateRequest.getMiniAppDesc().isEmpty()) {
            experimentDO.setMiniAppDesc(JSON.toJSONString(experimentUpdateRequest.getMiniAppDesc()));
        }
    }

    private void handleTags(ExperimentUpdateRequest experimentUpdateRequest, ExperimentDO experimentDO) {
        List<String> tags = experimentUpdateRequest.getTags();
        tagManager.addTags(experimentUpdateRequest.getUserId(), experimentDO.getExperimentId(),
            TagDO.TAG_TYPE_EXPERIMENT, tags);
    }

    private void bindWorkspaces(ExperimentDO experimentDO, List<String> workspaceIds) {
        workspaceService.addWorkspaceExperiments(experimentDO.getExperimentId(), experimentDO.getName(),
                experimentDO.getNamespace(), workspaceIds);
    }
}
