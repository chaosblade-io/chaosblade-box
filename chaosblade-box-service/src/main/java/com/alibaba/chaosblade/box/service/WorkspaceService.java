package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.WorkspaceShortInfo;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.clientobject.ExperimentStat;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.AddExperimentResponse;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperimentPageableQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperimentRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceBaseInfo;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.workspace.WorkspaceExperimentPageableQueryResponse;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;

import java.util.List;

public interface WorkspaceService {
    Response<ExperimentStat> getGeneralWorkSpaceStatInfo(ChaosUser user, WorkspaceQueryRequest request);

    PermissionResult getWorkspacePermission(ChaosUser user, String experimentid);

    WorkspaceExperimentPageableQueryResponse pageableGeneralExperiments(
            ChaosUser user, WorkspaceExperimentPageableQueryRequest request, Integer permission);

    AddExperimentResponse addWorkspaceExperiment(ChaosUser user, WorkspaceExperimentRequest request);

    PermissionResult getWorkspaceExperimentTaskPermission(ChaosUser chaosUser, String experimentTaskId);

    List<String> listExperimentTagsInWorkspace(String workspaceId, String namespace);

    List<WorkspaceBaseInfo> getWorkspaceByExperimentId(ChaosUser user, WorkspaceQueryRequest request);

    /**
     * 增加空间演练
     *
     * @param experimentId
     * @param name
     * @param namespace
     * @param workspaceIds
     */
    void addWorkspaceExperiments(String experimentId, String name, String namespace, List<String> workspaceIds);

    List<WorkspaceShortInfo> getWorkspacesShortInfoByExperimentId(String experimentId);
}
