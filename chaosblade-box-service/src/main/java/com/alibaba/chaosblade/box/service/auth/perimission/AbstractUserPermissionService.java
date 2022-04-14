package com.alibaba.chaosblade.box.service.auth.perimission;

import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.domain.workspace.WorkspaceRelationQuery;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.constant.PermissionTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.WorkspaceRelationTypes;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneAuthorizedQueryRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PayPackAuthorizationExpiredException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.model.SceneAuthorizedDO;
import com.alibaba.chaosblade.box.dao.model.SceneDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.WorkspaceRelationDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.*;
import com.alibaba.chaosblade.box.service.WorkspaceService;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sunju
 *
 */
public abstract class AbstractUserPermissionService implements UserPermissionService {

    @Resource
    private SceneRepository sceneRepository;

    @Resource
    private SceneFunctionRepository sceneFunctionRepository;

    @Resource
    private SceneAuthorizedRepository sceneAuthorizedRepository;

//    @Resource
//    protected AdministratorManager administratorManager;

    @Autowired
    protected ExperimentRepository experimentRepository;

    @Autowired
    private WorkspaceRelationRepository workspaceRelationRepository;

    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    protected PermissionResult getUserExperimentTaskPermission(ChaosUser chaosUser, String experimentTaskId) {
        if (experimentTaskRepository.existByUserIdAndTaskId(chaosUser, experimentTaskId)) {
            return PermissionResult.ALL;
        }
        return workspaceService.getWorkspaceExperimentTaskPermission(chaosUser, experimentTaskId);
    }

    protected PermissionResult getUserExperimentPermission(ChaosUser chaosUser, String experimentId, String nameSpace) {
        if (experimentRepository.existByUserIdAndExperimentId(chaosUser, experimentId)) {
            return PermissionResult.ALL;
        }
        return workspaceService.getWorkspacePermission(chaosUser, experimentId);
    }

    protected PermissionResult hasScenePermission(ChaosUser user, String sceneId) {

        Optional<SceneDO> optional = sceneRepository.findBySceneId(sceneId);
        if (optional.isPresent()) {
            SceneDO scene = optional.get();
            if (scene.getUserId().equals(user.getUserId())) {
                return PermissionResult.ALL;
            }
        }
        return PermissionResult.NONE;
    }

    protected PermissionResult hasSceneFunctionPermission(ChaosUser user, String functionId) {
//        if (administratorManager.isAdmin(user)) {
//            return PermissionResult.ALL;
//        }

        Optional<SceneFunctionDO> optional = sceneFunctionRepository.findByFunctionId(functionId);
        if (optional.isPresent()) {
            SceneAuthorizedQueryRequest queryRequest = new SceneAuthorizedQueryRequest();
            queryRequest.setGrantTo(user.getCurrentUserId());
            queryRequest.setFunctionId(functionId);
            queryRequest.setIsPublic(true);

            List<SceneAuthorizedDO> records = sceneAuthorizedRepository.getAuthorizedRecords(queryRequest);
            if (!CollectionUtil.isNullOrEmpty(records)) {
                Optional<SceneAuthorizedDO> maxPermissionRecord = records.stream()
                    .max(Comparator.comparing(SceneAuthorizedDO::getPermission));
                if (maxPermissionRecord.isPresent()) {
                    return PermissionResult.of(maxPermissionRecord.get().getPermission());
                }
            }
        }
        return PermissionResult.NONE;
    }

    @Override
    public void checkScenePermission(int permissionType, ChaosUser chaosUser, String sceneId)
        throws PermissionDeniedException {
        PermissionResult result = hasScenePermission(chaosUser, sceneId);
        if (!result.contains(permissionType)) {
            throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED);
        }
    }

    @Override
    public void checkSceneFunctionPermission(int permissionType, ChaosUser chaosUser, String functionId)
        throws PermissionDeniedException {
        PermissionResult result = hasSceneFunctionPermission(chaosUser, functionId);
        if (!result.contains(permissionType)) {
            throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED);
        }
    }

    @Override
    public void checkUserBalance(ChaosUser chaosUser) {
    }

    @Override
    public void checkUserAuthorizedExpired(ExperimentRunRequest experimentRunRequest)
        throws PayPackAuthorizationExpiredException {

    }

    @Override
    public void filterApplicationPair(ChaosUser chaosUser, Response<PageableResponse<AppNameAndIdPair>> pageableResponseResponse) {
    }

    @Override
    public void checkCreateExperimentPermission(ChaosUser chaosUser, ExperimentCreateRequest request) {
    }

    @Override
    public Response<PageQueryResponse<UserExperiment>> getExperimentListByStsUserLogin(ChaosUser chaosUser,
                                                                                       UserExperimentPageableQueryRequest userExperimentPageableQueryRequest) {
        return null;
    }

    @Override
    public void checkCreateWorkspacePermission(ChaosUser chaosUser) {

    }

    @Override
    public <R> void filterSceneSearchPermission(ChaosUser chaosUser, PageableResponse<R> sceneFunctions) {

    }

    @Override
    public void checkDeleteWorkspaceExperimentPermission(ChaosUser user, String workspaceId,
                                                         List<WorkspaceExperiment> workspaceExperimentList) {
        WorkspaceRelationQuery workspaceRelationQuery = new WorkspaceRelationQuery();
        workspaceRelationQuery.setWorkspaceIds(Lists.newArrayList(workspaceId));
        workspaceRelationQuery.setRelationType(WorkspaceRelationTypes.USER);
        workspaceRelationQuery.setOuterIds(Lists.newArrayList(user.getCurrentUserId()));
        List<WorkspaceRelationDO> relationDOList = workspaceRelationRepository.find(workspaceRelationQuery);
        boolean hasEditPermission = relationDOList.stream().anyMatch(
            workspaceRelationDO -> PermissionTypes.contains(workspaceRelationDO.getPermission(), PermissionTypes.W));
        if (hasEditPermission) { return; }
        boolean isExperimentCreator = workspaceExperimentList.stream().allMatch(
            workspaceExperiment -> experimentRepository.findById(workspaceExperiment.getExperimentId()).map(
                experimentDO -> Objects.equals(experimentDO.getCreator().getCurrentUserId(),
                    user.getCurrentUserId())).orElse(true));
        if (!isExperimentCreator) {
            throw new ChaosException(CommonErrorCode.P_USER_PERMISSION_DENIED);
        }
    }
}
