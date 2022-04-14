package com.alibaba.chaosblade.box.common.common.domain.chaosblade;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ChaosBladeAction {

    private ChaosBladeActionType actionType;

    public ChaosBladeActionType getActionType() {
        return actionType;
    }

    public String getAction() {
        return action;
    }

    private String action;

    private String target;

    private String subTarget;

    private String prepareType;

    /**
     * 范围
     */
    private String scope;

    private boolean hasJavaInstall;

    public boolean isJvm() {
        if (hasJavaInstall) { return true; }
        if (ChaosBladeMetaData.SUB_TARGET_JVM.equals(subTarget)) { return true; }
        if (!ChaosBladeMetaData.SCOPE_HOST.equals(scope)) {
            return ChaosBladeMetaData.SubTargetWithsJavaAgentInstall.contains(subTarget);
        }
        return false;
    }

    public void setJvm(boolean jvm) {
        isJvm = jvm;
    }

    public void setHasJavaInstall(boolean has) {
        this.hasJavaInstall = has;
    }

    private boolean isJvm;

    /**
     * 是否上架
     */
    private boolean enabled;

}
