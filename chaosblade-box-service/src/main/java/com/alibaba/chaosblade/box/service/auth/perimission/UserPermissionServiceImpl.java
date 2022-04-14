package com.alibaba.chaosblade.box.service.auth.perimission;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public class UserPermissionServiceImpl extends AbstractUserPermissionService {

    @Override
    public PermissionResult checkExperimentPermission(int permissionType, ChaosUser chaosUser, String experimentId,
                                                      String nameSpace,
                                                      List<Host> hosts) throws PermissionDeniedException {
        PermissionResult permissionResult = getUserExperimentPermission(chaosUser, experimentId, nameSpace);
        if (!permissionResult.contains(permissionType)) {
            throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED, null);

        }
        return permissionResult;
    }

    @Override
    public void checkExperimentDeletePermission(int permissionType, ChaosUser chaosUser, String experimentId,
                                                String nameSpace, List<Host> hosts) throws PermissionDeniedException {

    }

    @Override
    public PermissionResult checkExperimentTaskPermission(int permissionType, ChaosUser chaosUser, String experimentTaskId) {
        PermissionResult permissionResult = getUserExperimentTaskPermission(chaosUser, experimentTaskId);
        if (!permissionResult.contains(permissionType)) {
            throw new PermissionDeniedException(CommonErrorCode.P_USER_PERMISSION_DENIED, null);
        }
        return permissionResult;
    }

    @Override
    public void checkUserApplicationPermission(int permissionType, ChaosUser chaosUser, Long appId)
        throws PermissionDeniedException {

    }
    
    @Override
    public void checkUserApplicationPermission(int permissionType, ChaosUser chaosUser, String namespace, String appName)
        throws PermissionDeniedException {
        
    }
    
    @Override
    public void checkOpenApiForChaosPermission(ChaosUser chaosUser) throws PermissionDeniedException {

    }
}
