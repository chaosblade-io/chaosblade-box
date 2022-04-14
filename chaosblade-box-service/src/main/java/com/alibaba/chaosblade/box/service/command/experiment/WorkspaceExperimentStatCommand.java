package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.workspace.WorkspaceRelationQuery;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceQueryRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.WorkspaceRelationDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.WorkspaceRelationRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class WorkspaceExperimentStatCommand extends SpringBeanCommand<WorkspaceQueryRequest, ExperimentStat> {

    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private WorkspaceRelationRepository workspaceRelationRepository;

    @Override
    public ExperimentStat execute(WorkspaceQueryRequest request) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(request.getWorkspaceId()));
        workspaceRelationQuery.setNamespace(request.getNamespace());
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);

        List<String> experimentIds = relationDOList.stream().map(WorkspaceRelationDO::getOuterId).collect(Collectors.toList());

        ExperimentStat experimentStat = new ExperimentStat();
        if (CollectionUtils.isEmpty(experimentIds)) {
            return experimentStat;
        }

        List<ExperimentDO> experiments = experimentRepository.findByExperimentIdsIn(experimentIds);
        experimentStat.setTotal(experiments.size());

        long active = 0L, running = 0L, failed = 0L, success = 0L, idle = 0L, finishedCount = 0L;

        for (ExperimentDO experiment : experiments) {
            if (experiment.getExperimentTaskId() == null) {
                idle++;
            } else {
                active++;
                ExperimentStateEnum experimentStateEnum = experiment.getExperimentStateEnum();
                if (ExperimentStateEnum.RUNNING.equals(experimentStateEnum)) {
                    running++;
                } else {
                    finishedCount++;
                    if (experiment.isSuccess()) {
                        success++;
                    } else {
                        failed++;
                    }
                }
            }
        }

        experimentStat.setIdle(idle);
        experimentStat.setFinished(finishedCount);
        experimentStat.setRunning(running);
        experimentStat.setActive(active);
        experimentStat.setFailure(failed);
        experimentStat.setSuccess(success);
        //老数据
        experimentStat.setTotalCount(experiments.size());
        experimentStat.setFailureCount(experimentStat.getFailure());
        experimentStat.setFinished(experimentStat.getFinishedCount());
        experimentStat.setRunningCount(experimentStat.getRunningCount());
        return experimentStat;
    }

}
