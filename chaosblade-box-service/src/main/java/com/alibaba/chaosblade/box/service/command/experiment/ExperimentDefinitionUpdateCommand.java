package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.StableProbeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExperimentDefinitionContext;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.ExperimentGuard;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.event.ExperimentUpdatedEvent;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowDO;
import com.alibaba.chaosblade.box.dao.model.MiniFlowGroupDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowGroupRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentMiniFlowRepository;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class ExperimentDefinitionUpdateCommand extends AbstractExperimentDefinitionCommand {

    @Autowired
    private ExperimentMiniFlowGroupRepository experimentMiniFlowGroupRepository;

    @Autowired
    private ExperimentMiniFlowRepository experimentMiniFlowRepository;

    @Autowired
    private List<StableProbeInterceptor> stableProbeInterceptors;

    @Override
    protected void handleMiniFlowGroup(ExperimentDefinitionContext experimentDefinitionContext,
                                       ExperimentDO experimentDO, MiniFlowGroup miniFlowGroup) {
        if (experimentDO.isNewExperimentDefinition()) {
            MiniFlowGroupDO miniFlowGroupDO = experimentMiniFlowGroupRepository.findById(miniFlowGroup.getGroupId())
                    .orElse(null);
            if (miniFlowGroupDO == null) {
                addMiniGroup(experimentDefinitionContext, experimentDO, miniFlowGroup);
            } else {
                updateMiniFlowGroup(experimentDefinitionContext, miniFlowGroupDO, miniFlowGroup);
            }
        } else {
            addMiniGroup(experimentDefinitionContext, experimentDO, miniFlowGroup);
        }
    }

    @Override
    protected void fireEvents(ChaosUser user, ExperimentDO experimentDO) {
        if (!experimentDO.getIsTemplate()) {
            chaosEventDispatcher.fireEvent(new ExperimentUpdatedEvent(user, experimentDO));
        }
    }

    @Override
    protected void saveExperimentActivityDefinitions(ExperimentDefinitionContext experimentDefinitionContext) {
        experimentFlowDefinitionManager.updateFlowDefinition(
                experimentDefinitionContext.getExperimentDO(),
                experimentDefinitionContext.getExperimentFlowDefinition()
        );
    }

    @Override
    protected void deleteExtraGroups(ExperimentDefinitionRequest experimentDefinitionRequest) {
        List<MiniFlowGroup> miniFlowGroups = experimentDefinitionRequest.getFlowGroups();
        Set<String> newMiniGroups = miniFlowGroups.stream()
                .map(MiniFlowGroup::getGroupId)
                .filter(s -> !Strings.isNullOrEmpty(s))
                .collect(Collectors.toSet());

        Set<String> oldGroups = experimentMiniFlowGroupRepository.findByExperimentId(
                experimentDefinitionRequest.getExperimentId()).stream()
                .map(MiniFlowGroupDO::getGroupId)
                .collect(Collectors.toSet());
        Set<String> extraGroups = Sets.difference(oldGroups, newMiniGroups);
        if (!extraGroups.isEmpty()) {
            experimentMiniFlowGroupRepository.deleteMiniGroups(extraGroups);
            experimentMiniFlowRepository.deleteByGroupIds(extraGroups);
        }
    }

    /**
     * 更新的话，先删除老的
     *
     * @param experimentDO
     * @param experimentGuards
     */
    @Override
    protected void handleGuards(ExperimentDO experimentDO, List<ExperimentGuard> experimentGuards) {
        experimentGuardRepository.deleteByExperimentId(experimentDO.getExperimentId());
        super.handleGuards(experimentDO, experimentGuards);
    }

    private void updateMiniFlowGroup(ExperimentDefinitionContext experimentDefinitionContext,
                                     MiniFlowGroupDO miniFlowGroupDO, MiniFlowGroup miniFlowGroup) {
        miniFlowGroupDO.setGroupOrder(miniFlowGroup.getOrder());
        miniFlowGroupDO.setRequired(miniFlowGroup.isRequired());
        miniFlowGroupDO.setGroupName(miniFlowGroup.getGroupName());
        miniFlowGroupDO.setHosts(extraHostsFromMiniFlowGroup(miniFlowGroup));
        //更新微流程组的相关信息
        experimentMiniFlowGroupRepository.update(miniFlowGroupDO);
        //删除掉不需要的微流程
        removeOldMiniFlowNotInNewRequest(miniFlowGroupDO, miniFlowGroup.getFlows());
        //获取到新的流程定义
        fillExperimentFlowDefinition(
                experimentDefinitionContext,
                miniFlowGroup,
                miniFlowGroupDO
        );
    }

    private void removeOldMiniFlowNotInNewRequest(MiniFlowGroupDO miniFlowGroupDO, List<MiniFlow> miniFlows) {
        List<MiniFlowDO> miniFlowDOS = experimentMiniFlowRepository.findByGroupIdAndExperimentId(
                miniFlowGroupDO.getGroupId(), miniFlowGroupDO.getExperimentId());
        List<String> deleteMiniFlowIds = new ArrayList<>();
        for (MiniFlowDO miniFlowDO : miniFlowDOS) {
            boolean exist = false;
            for (MiniFlow miniFlow : miniFlows) {
                if (miniFlowDO.getFlowId().equals(miniFlow.getFlowId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                deleteMiniFlowIds.add(miniFlowDO.getFlowId());
            }
        }
        experimentMiniFlowRepository.deleteByFlowIdIn(deleteMiniFlowIds);
    }

    @Override
    protected void handleStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest) {
        //先删除
        stableProbeInterceptors.forEach(stableProbeInterceptor -> {
            stableProbeInterceptor.deleteStableProbe(experimentDefinitionRequest);
        });
        super.handleStableProbe(experimentDefinitionRequest);
    }
}
