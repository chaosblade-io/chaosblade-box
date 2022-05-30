package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.TransactionUtil;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseEvaluationDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseEvaluationRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.service.model.expertise.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
public abstract class BaseExpertiseCommand<ExpertiseOperationRequest extends BaseExpertiseOperationRequest>
    extends SpringBeanCommand<ExpertiseOperationRequest, Response<String>> {

    @Autowired
    protected ExpertiseParamChecker expertiseParamChecker;

    @Autowired
    protected TransactionUtil transactionUtil;

    @Autowired
    private TagManager tagManager;

    @Autowired
    protected ExpertiseEvaluationRepository expertiseEvaluationRepository;

    @Autowired
    protected ChaosEventDispatcher chaosEventDispatcher;

    @Override
    public Response<String> execute(ExpertiseOperationRequest expertiseOperationRequest) {
        ChaosError ChaosError = expertiseParamChecker.checkExpertiseBasicInfo(
            expertiseOperationRequest.getBasicInfo());
        if (ChaosError != null) {
            return Response.failedWith(ChaosError);
        }
        return transactionUtil.execute(
            new TransactionCallback<Response<String>>() {
                @Override
                public Response<String> doInTransaction(
                    TransactionStatus status) {
                    try {
                        ChaosError error = null;
                        ExpertiseDO expertiseDO = handleBasicInfo(expertiseOperationRequest,
                            expertiseOperationRequest.getBasicInfo());
                        handleTags(expertiseOperationRequest, expertiseDO.getExpertiseId(),
                            expertiseOperationRequest.getBasicInfo().getTags());
                        error = handleExecutionInfo(expertiseOperationRequest, expertiseDO,
                            expertiseOperationRequest.getExecutableInfo());
                        if (error != null) {
                            return Response.failedWith(error);
                        }
                        handleSupportScopeType(expertiseDO,expertiseOperationRequest.getExecutableInfo());
                        handleEvaluationInfo(expertiseDO, expertiseOperationRequest.getEvaluationInfo());
                        handleExpertise(expertiseOperationRequest, expertiseDO);
                        return Response.okWithData(expertiseDO.getExpertiseId());
                    } catch (Throwable throwable) {
                        TransactionUtil.setRollback(status);
                        log.info("{} failed", this.getClass().getName(), throwable);
                        return handleThrowable(expertiseOperationRequest, throwable);
                    }
                }
            });

    }

    protected void handleTags(ExpertiseOperationRequest expertiseOperationRequest, String expertiseId,
        Set<String> tags) {
        tagManager.addTags(expertiseOperationRequest.getUserId(), expertiseId, TagDO.TAG_TYPE_EXPERTISE,
            new ArrayList<>(tags));
    }

    protected ExperimentDefinitionRequest buildExperimentDefinitionRequest(
        ExpertiseOperationRequest expertiseOperationRequest, String experimentId,
        ExperimentFlowInfo experimentFlowInfo) {
        ExperimentDefinitionRequest experimentDefinitionRequest = new ExperimentDefinitionRequest();
        signComponentRequiredOrNot(experimentFlowInfo, true);
        experimentDefinitionRequest.setFlowGroups(experimentFlowInfo.getFlowGroups());
        experimentDefinitionRequest.setRunMode(experimentFlowInfo.getRunMode());
        experimentDefinitionRequest.setGuardConf(experimentFlowInfo.getGuardConf());
        experimentDefinitionRequest.setDuration(experimentFlowInfo.getDuration());
        experimentDefinitionRequest.setNamespace(expertiseOperationRequest.getNamespace());
        experimentDefinitionRequest.setExperimentId(experimentId);
        experimentDefinitionRequest.setUser(expertiseOperationRequest.getUser());
        experimentDefinitionRequest.setExpertise(true);
        experimentDefinitionRequest.setSchedulerConfig(experimentFlowInfo.getSchedulerConfig());
        experimentDefinitionRequest.setLang(expertiseOperationRequest.getLang());
        return experimentDefinitionRequest;
    }

    /**
     * 标记组件是否必须
     *
     * @param experimentFlowInfo
     */
    public static void signComponentRequiredOrNot(ExperimentFlowInfo experimentFlowInfo, boolean required) {
        for (MiniFlowGroup miniFlowGroup : experimentFlowInfo.getFlowGroups()) {
            miniFlowGroup.setRequired(required);
            for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
                miniFlow.setRequired(required);
                if (miniFlow.getPrepare() != null) {
                    miniFlow.getPrepare().forEach(
                        experimentActivityInfo -> experimentActivityInfo.setRequired(required));
                }
                if (miniFlow.getAttack() != null) {
                    miniFlow.getAttack().forEach(
                        experimentActivityInfo -> experimentActivityInfo.setRequired(required));
                }

                if (miniFlow.getCheck() != null) {
                    miniFlow.getCheck().forEach(experimentActivityInfo -> experimentActivityInfo.setRequired(required));
                }

                if (miniFlow.getRecover() != null) {
                    miniFlow.getRecover().forEach(
                        experimentActivityInfo -> experimentActivityInfo.setRequired(required));
                }
            }
        }
        if (experimentFlowInfo.getGuardConf() != null) {
            experimentFlowInfo.getGuardConf().getGuards().forEach(
                experimentGuard -> experimentGuard.setRequired(required));
        }
    }

    protected abstract void handleExpertise(ExpertiseOperationRequest expertiseOperationRequest,
        ExpertiseDO expertiseDO);

    protected abstract Response<String> handleThrowable(ExpertiseOperationRequest expertiseOperationRequest,
        Throwable throwable);

    protected void handleEvaluationInfo(ExpertiseDO expertiseDO, ExpertiseEvaluationInfo evaluationInfo) {
        if (evaluationInfo == null) { return; }
        if (!CollectionUtil.isNullOrEmpty(evaluationInfo.getItems())) {
            for (ExpertiseEvaluationItem expertiseEvaluationItem : evaluationInfo.getItems()) {
                ExpertiseEvaluationDO expertiseEvaluationDO = new ExpertiseEvaluationDO();
                expertiseEvaluationDO.setDescription(expertiseEvaluationItem.getDesc());
                expertiseEvaluationDO.setExpertiseId(expertiseDO.getExpertiseId());
                expertiseEvaluationRepository.add(expertiseEvaluationDO);
            }
        }
    }

    protected void fillRuntimeInfo(ExpertiseDO expertiseDO, ExpertiseExecutableInfo executableInfo) {
        expertiseDO.setRunTime(executableInfo.getRunTime());
    }

    protected abstract ChaosError handleExecutionInfo(ExpertiseOperationRequest expertiseOperationRequest,
        ExpertiseDO expertiseDO, ExpertiseExecutableInfo executableInfo);

    protected abstract ExpertiseDO handleBasicInfo(ExpertiseOperationRequest expertiseOperationRequest,
        ExpertiseBasicInfo basicInfo);


    protected void handleSupportScopeType(ExpertiseDO expertiseDO, ExpertiseExecutableInfo expertiseExecutableInfo) {
        ExperimentFlowInfo experimentFlowInfo = expertiseExecutableInfo.getFlow();
        List<MiniFlowGroup> miniFlowGroups = experimentFlowInfo.getFlowGroups();
        StringJoiner str = new StringJoiner(",");
        miniFlowGroups.forEach(miniFlowGroup -> {
            str.add(String.valueOf(miniFlowGroup.getScopeType()));
        });
        expertiseDO.setScopeType(str.toString());
    }

}
