package com.alibaba.chaosblade.box.service.auth.perimission;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceExperiment;
import com.alibaba.chaosblade.box.common.infrastructure.exception.InsufficientBalanceException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PayPackAuthorizationExpiredException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentCreateRequest;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.experiment.UserExperimentPageableQueryRequest;

import java.util.List;

/**
 * @author haibin
 * 
 *
 */
public interface UserPermissionService {

    /**
     * 检查是否有某个演练的权限
     *
     * @param permissionType
     * @param chaosUser         user
     * @param experimentId   experiment id
     * @param nameSpace
     * @return permission
     * @throws PermissionDeniedException
     */
    PermissionResult checkExperimentPermission(int permissionType, ChaosUser chaosUser, String experimentId, String nameSpace,
                                               List<Host> hosts) throws PermissionDeniedException;

    void checkExperimentDeletePermission(int permissionType, ChaosUser chaosUser, String experimentId, String nameSpace,
                                         List<Host> hosts) throws PermissionDeniedException;

    /**
     * 检查演练任务权限
     *
     * @param permissionType
     * @param chaosUser
     * @param experimentTaskId
     * @return
     * @throws PermissionDeniedException
     */
    PermissionResult checkExperimentTaskPermission(int permissionType, ChaosUser chaosUser, String experimentTaskId)
        throws PermissionDeniedException;

    /**
     * 检查是否有某个场景的具体权限
     *
     * @param permissionType
     * @param chaosUser
     * @param sceneId
     * @throws PermissionDeniedException
     */
    void checkScenePermission(int permissionType, ChaosUser chaosUser, String sceneId) throws PermissionDeniedException;

    /**
     * 检查是否有某个场景函数的具体权限
     *
     * @param permissionType
     * @param chaosUser
     * @param functionId
     * @throws PermissionDeniedException
     */
    void checkSceneFunctionPermission(int permissionType, ChaosUser chaosUser, String functionId)
        throws PermissionDeniedException;

    /**
     * 检查用户演练应用访问权限
     *
     * @param permissionType 权限类型
     * @param chaosUser
     * @param appId
     * @throws PermissionDeniedException
     */
    void checkUserApplicationPermission(int permissionType, ChaosUser chaosUser, Long appId) throws PermissionDeniedException;
    
    
    void checkUserApplicationPermission(int permissionType, ChaosUser chaosUser, String  namespace, String appName) throws PermissionDeniedException;
    
    
    /**
     * 检查用户余额
     *
     * @param chaosUser
     * @throws InsufficientBalanceException
     */
    void checkUserBalance(ChaosUser chaosUser) throws InsufficientBalanceException;

    /**
     * 检查用户授权是否过期
     *
     * @param experimentRunRequest
     * @throws PayPackAuthorizationExpiredException
     */
    void checkUserAuthorizedExpired(ExperimentRunRequest experimentRunRequest)
        throws PayPackAuthorizationExpiredException;

    /**
     * 检查用户演练应用访问权限
     *
     * @param chaosUser
     * @throws PermissionDeniedException
     */
    void checkOpenApiForChaosPermission(ChaosUser chaosUser) throws PermissionDeniedException;

    /**
     * 过滤 sts登录时，应用下拉框数据
     *
     * @param chaosUser
     * @param listResponse
     */
    void filterApplicationPair(ChaosUser chaosUser, Response<PageableResponse<AppNameAndIdPair>> listResponse);

    /**
     * 校验sts登录时创建演练的应用权限校验
     *
     * @param chaosUser
     * @param request
     */
    void checkCreateExperimentPermission(ChaosUser chaosUser, ExperimentCreateRequest request);

    /**
     * sts登录，分页查询演练列表
     *
     * @param chaosUser
     * @param userExperimentPageableQueryRequest
     * @return
     */
    Response<PageQueryResponse<UserExperiment>> getExperimentListByStsUserLogin(ChaosUser chaosUser,
                                                                                UserExperimentPageableQueryRequest userExperimentPageableQueryRequest);

    /**
     * 校验创建演练空间权限
     *
     * @param chaosUser
     */
    void checkCreateWorkspacePermission(ChaosUser chaosUser);

    /**
     * 过滤小程序查询结果的权限标记
     *
     * @param chaosUser
     * @param sceneFunctions
     */
    <R> void filterSceneSearchPermission(ChaosUser chaosUser, PageableResponse<R> sceneFunctions);

    void checkDeleteWorkspaceExperimentPermission(ChaosUser user, String workspaceId,
                                                  List<WorkspaceExperiment> workspaceExperimentList);
}
