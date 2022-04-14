package com.alibaba.chaosblade.box.service.command.expertise;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentRunModeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExpertiseRepository;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentFlowDefinitionQueryCommand;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseFlowActivity;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseFlowView;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseSearchRequest;
import com.alibaba.chaosblade.box.service.model.expertise.ExpertiseView;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ExpertiseSearchCommand
    extends SpringBeanCommand<ExpertiseSearchRequest, Response<PageQueryResponse<ExpertiseView>>> {

    @Autowired
    private ExpertiseRepository expertiseRepository;
    @Autowired
    private TagManager tagManager;
    @Autowired
    private ExperimentRepository experimentRepository;

    @Override
    public Response<PageQueryResponse<ExpertiseView>> execute(ExpertiseSearchRequest expertiseSearchRequest) {
        IPage<ExpertiseDO> expertiseDOIPage = expertiseRepository.userFindPageable(
                expertiseSearchRequest.getUser(),
                expertiseSearchRequest.getKey(),
                expertiseSearchRequest.getTagNames(),
                expertiseSearchRequest.getScopeType(),
                expertiseSearchRequest.getPage(),
                expertiseSearchRequest.getSize());
        PageQueryResponse<ExpertiseView> pageQueryResponse = new PageQueryResponse<>();
        pageQueryResponse.setPageSize(expertiseDOIPage.getSize());
        pageQueryResponse.setPages(expertiseDOIPage.getPages());
        pageQueryResponse.setTotal(expertiseDOIPage.getTotal());
        pageQueryResponse.setCurrentPage(expertiseDOIPage.getCurrent());
        pageQueryResponse.setContent(
            expertiseDOIPage.getRecords().stream().map(new Function<ExpertiseDO, ExpertiseView>() {
                @Override
                public ExpertiseView apply(ExpertiseDO expertiseDO) {
                    return convertExpertiseView(expertiseDO);
                }
            }).collect(
                Collectors.toList()));
        return Response.okWithData(pageQueryResponse);
    }

    private ExpertiseView convertExpertiseView(ExpertiseDO expertiseDO) {
        ExpertiseView expertiseView = new ExpertiseView();
        expertiseView.setTags(tagManager.findTagsByExpertiseId(expertiseDO.getExpertiseId()).stream().map(
            TagDO::getName).collect(Collectors.toSet()));
        expertiseView.setName(expertiseDO.getName());
        expertiseView.setFunctionDesc(expertiseDO.getFunctionDesc());
        expertiseView.setExpertiseId(expertiseDO.getExpertiseId());
        expertiseView.setFlow(buildExpertiseFlowView(expertiseDO));
        expertiseView.setType(expertiseDO.getType());
        //更新经验库支持的机器类型
        updateScopeType(expertiseDO,expertiseView);
        expertiseView.setScopeType(expertiseView.getFlow().getScopeType());
        return expertiseView;
    }

    private void updateScopeType(ExpertiseDO expertiseDO,ExpertiseView expertiseView) {
        if(null == expertiseDO.getScopeType()) {
            if(!CollectionUtil.isNullOrEmpty(expertiseView.getFlow().getScopeType())) {
                StringJoiner subStr = new StringJoiner(",");
                expertiseView.getFlow().getScopeType().forEach(integer -> {
                    subStr.add(String.valueOf(integer));
                });
                expertiseDO.setScopeType(subStr.toString());
            }
            expertiseRepository.update(expertiseDO);
        }
    }

    private ExpertiseFlowView buildExpertiseFlowView(ExpertiseDO expertiseDO) {
        ExpertiseFlowView expertiseFlowView = new ExpertiseFlowView();
        ExperimentDO experimentDO = experimentRepository.findById(expertiseDO.getExperimentId()).orElse(null);
        ExperimentFlowInfo experimentFlowInfo = null;
        if (experimentDO != null) {
            experimentFlowInfo = commandBus.syncRun(
                ExperimentFlowDefinitionQueryCommand.class,
                experimentDO);
        }
        if (experimentFlowInfo == null) {
            log.info("not exist or error when find flow info by experiment by expertiseId:{},experimentId:{}",
                expertiseDO.getExpertiseId(),
                expertiseDO.getExperimentId());
            expertiseFlowView.setActivities(new ArrayList<>());
            expertiseFlowView.setScopeType(new HashSet<>());
        } else {
            expertiseFlowView.setActivities(buildActivityList(experimentFlowInfo));
            expertiseFlowView.setScopeType(buildScopeTypeList(experimentFlowInfo));
        }
        return expertiseFlowView;
    }

    private List<ExpertiseFlowActivity> buildActivityList(ExperimentFlowInfo experimentFlowInfo) {
        ExperimentRunModeEnum experimentRunModeEnum = experimentFlowInfo.getRunMode();
        List<MiniFlowGroup> miniFlowGroups = experimentFlowInfo.getFlowGroups();
        List<ExpertiseFlowActivity> expertiseFlowActivities = new ArrayList<>();
        if (ExperimentRunModeEnum.PHASE.equals(experimentRunModeEnum)) {
            Map<PhaseType, List<ExperimentActivityInfo>> flowActivitiesByPhaseType = groupFlowActivityByPhaseType(
                miniFlowGroups);
            flowActivitiesByPhaseType.getOrDefault(PhaseType.PREPARE, new ArrayList<>()).forEach(
                experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.PREPARE,
                    experimentActivityInfo));
            flowActivitiesByPhaseType.getOrDefault(PhaseType.ATTACK, new ArrayList<>()).forEach(
                experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.ATTACK,
                    experimentActivityInfo));
            flowActivitiesByPhaseType.getOrDefault(PhaseType.CHECK, new ArrayList<>()).forEach(
                experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.CHECK,
                    experimentActivityInfo));
            flowActivitiesByPhaseType.getOrDefault(PhaseType.RECOVER, new ArrayList<>()).forEach(
                experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.RECOVER,
                    experimentActivityInfo));
        }
        if (ExperimentRunModeEnum.SEQUENCE.equals(experimentRunModeEnum)) {
            for (MiniFlowGroup miniFlowGroup : miniFlowGroups) {
                for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
                    miniFlow.getPrepare().forEach(
                        experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.PREPARE,
                            experimentActivityInfo));
                    miniFlow.getAttack().forEach(
                        experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.ATTACK,
                            experimentActivityInfo));
                    miniFlow.getCheck().forEach(
                        experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.CHECK,
                            experimentActivityInfo));
                    miniFlow.getRecover().forEach(
                        experimentActivityInfo -> appendActivity(expertiseFlowActivities, PhaseType.RECOVER,
                            experimentActivityInfo));
                }
            }
        }
        return expertiseFlowActivities;
    }

    private Map<PhaseType, List<ExperimentActivityInfo>> groupFlowActivityByPhaseType(
        List<MiniFlowGroup> miniFlowGroups) {
        Map<PhaseType, List<ExperimentActivityInfo>> phaseTypeListMap = new HashMap<>();
        for (MiniFlowGroup miniFlowGroup : miniFlowGroups) {
            for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
                phaseTypeListMap.computeIfAbsent(PhaseType.PREPARE,
                    phaseType -> new ArrayList<>()).addAll(miniFlow.getPrepare());
                phaseTypeListMap.computeIfAbsent(PhaseType.ATTACK,
                    phaseType -> new ArrayList<>()).addAll(miniFlow.getAttack());
                phaseTypeListMap.computeIfAbsent(PhaseType.CHECK,
                    phaseType -> new ArrayList<>()).addAll(miniFlow.getCheck());
                phaseTypeListMap.computeIfAbsent(PhaseType.RECOVER,
                    phaseType -> new ArrayList<>()).addAll(miniFlow.getRecover());
            }
        }
        return phaseTypeListMap;
    }

    private void appendActivity(List<ExpertiseFlowActivity> expertiseFlowActivities, PhaseType phaseType,
        ExperimentActivityInfo experimentActivityInfo) {
        ExpertiseFlowActivity expertiseFlowActivity = new ExpertiseFlowActivity();
        expertiseFlowActivity.setPhase(phaseType);
        expertiseFlowActivity.setName(experimentActivityInfo.getActivityName());
        expertiseFlowActivities.add(expertiseFlowActivity);
    }

    private Set<Integer> buildScopeTypeList(ExperimentFlowInfo experimentFlowInfo) {
        Set<Integer> scopeTypes = new HashSet<>();
        List<MiniFlowGroup> miniFlowGroups = experimentFlowInfo.getFlowGroups();
        for (MiniFlowGroup miniFlowGroup : miniFlowGroups) {
            scopeTypes.add(miniFlowGroup.getScopeType());
        }
        return scopeTypes;
    }


}
