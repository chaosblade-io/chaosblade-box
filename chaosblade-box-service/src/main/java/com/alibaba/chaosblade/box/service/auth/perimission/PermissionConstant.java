package com.alibaba.chaosblade.box.service.auth.perimission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 * 
 *
 */
public class PermissionConstant {

    public final static String PERMISSION_EXPERIMENT_RUN = "experiment_run";

    /**
     * 应用访问权限
     */
    public final static String PERMISSION_APP_VISIT = "app_visit";

    public final static String PERMISSION_WORKSPACE_VISIT = "workspace_visit";

    public static List<String> PermissionConstants = new ArrayList<>();

    public static boolean existPermission(String permission) {
        return PermissionConstants.contains(permission);
    }

    static {
        PermissionConstants.add(PERMISSION_EXPERIMENT_RUN);
        PermissionConstants.add(PERMISSION_APP_VISIT);
        PermissionConstants.add(PERMISSION_WORKSPACE_VISIT);
    }

}
