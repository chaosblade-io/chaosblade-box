package com.alibaba.chaosblade.box.common.infrastructure.constant;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import lombok.experimental.UtilityClass;

@UtilityClass
public class WorkspaceUserRoles {

    public static final Integer MAIN = 0;

    public Integer getWorkspaceUserRole(ChaosUser user) {

        return WorkspaceUserRoles.MAIN;
    }

}
