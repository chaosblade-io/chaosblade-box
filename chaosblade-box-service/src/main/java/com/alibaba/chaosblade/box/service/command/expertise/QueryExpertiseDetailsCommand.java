package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentExpertiseQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseEvaluationDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseEvaluationRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentFlowDefinitionQueryCommand;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.service.model.expertise.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询经验详情
 *
 * @author haibin
 *
 *
 */
@Component
public class QueryExpertiseDetailsCommand
    extends SpringBeanCommand<ExperimentExpertiseQueryRequest, Response<ExpertiseInfo>> {

    @Autowired
    private ExpertiseRepository expertiseRepository;

    @Autowired
    private TagManager tagManager;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private ExpertiseEvaluationRepository expertiseEvaluationRepository;

    @Override
    public Response<ExpertiseInfo> execute(ExperimentExpertiseQueryRequest experimentExpertiseQueryRequest) {
        ExpertiseDO expertiseDO = expertiseRepository.findById(experimentExpertiseQueryRequest.getExpertiseId()).orElse(
            null);
        if (expertiseDO == null) {
            return Response.failedWith(CommonErrorCode.P_NOT_FOUND_EXPERTISE);
        }
        ExpertiseInfo expertiseInfo = new ExpertiseInfo();
        expertiseInfo.setExpertiseId(expertiseDO.getExpertiseId());
        fillBasicInfo(expertiseInfo, expertiseDO);
        fillExecutionInfo(experimentExpertiseQueryRequest.getUser(), expertiseInfo, expertiseDO);
        fillEvaluationInfo(expertiseInfo, expertiseDO);
        return Response.okWithData(expertiseInfo);
    }

    private void fillEvaluationInfo(ExpertiseInfo expertiseInfo, ExpertiseDO expertiseDO) {
        ExpertiseEvaluationInfo expertiseEvaluationInfo = new ExpertiseEvaluationInfo();
        expertiseInfo.setEvaluationInfo(expertiseEvaluationInfo);
        List<ExpertiseEvaluationDO> expertiseEvaluationDOList = expertiseEvaluationRepository.findByExpertiseId(
            expertiseDO.getExpertiseId());
        expertiseEvaluationInfo.setItems(expertiseEvaluationDOList.stream().map(
            expertiseEvaluationDO -> {
                ExpertiseEvaluationItem expertiseEvaluationItem = new ExpertiseEvaluationItem();
                expertiseEvaluationItem.setDesc(expertiseEvaluationDO.getDescription());
                return expertiseEvaluationItem;
            }).collect(Collectors.toList()));
    }

    private void fillExecutionInfo(ChaosUser user, ExpertiseInfo expertiseInfo, ExpertiseDO expertiseDO) {
        ExpertiseExecutableInfo expertiseExecutableInfo = new ExpertiseExecutableInfo();
        expertiseInfo.setExecutableInfo(expertiseExecutableInfo);
        ExperimentDO experimentDO = experimentRepository.findById(expertiseDO.getExperimentId()).orElse(null);
        if (experimentDO == null) { return; }
        ExperimentFlowInfo experimentFlowInfo = commandBus.syncRun(ExperimentFlowDefinitionQueryCommand.class,
            experimentDO);
        //专家经验过来的节点，每个都可以进行处理
        if (experimentFlowInfo == null) { return; }
        BaseExpertiseCommand.signComponentRequiredOrNot(experimentFlowInfo, false);
        expertiseExecutableInfo.setFlow(experimentFlowInfo);
        fillRunTimeInfo(expertiseExecutableInfo, expertiseDO);
    }

    private void fillRunTimeInfo(ExpertiseExecutableInfo expertiseExecutableInfo, ExpertiseDO expertiseDO) {
        expertiseExecutableInfo.setRunTime(expertiseDO.getRunTime());
    }

    private void fillBasicInfo(ExpertiseInfo expertiseInfo, ExpertiseDO expertiseDO) {
        ExpertiseBasicInfo expertiseBasicInfo = new ExpertiseBasicInfo();
        expertiseInfo.setBasicInfo(expertiseBasicInfo);
        expertiseBasicInfo.setName(expertiseDO.getName());
        expertiseBasicInfo.setDesignConcept(expertiseDO.getDesignConcept());
        expertiseBasicInfo.setFunctionDesc(expertiseDO.getFunctionDesc());
        expertiseBasicInfo.setBackgroundDesc(expertiseDO.getBackgroundDesc());
        expertiseBasicInfo.setState(expertiseDO.getState());
        expertiseBasicInfo.setTags(tagManager.findTagsByExpertiseId(expertiseDO.getExpertiseId()).stream().map(
            TagDO::getName).collect(Collectors.toSet()));
    }
}
