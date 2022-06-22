package com.alibaba.chaosblade.box.common.common.util;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeActionType;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeMetaData;
import com.google.common.base.Strings;

/**
 * @author haibin
 * 
 *
 */
public final class MiniAppUtils {

    public static String JVM_AGENT_INSTALL_SUFFIX = "jvm.install";

    public static String AGENT_INSTALL = "chaos.jvm.install";

    public static String JVM_HIT_COUNT = "chaosapp.metric.hitcount";

    public static boolean isCpuFullLoad(String appCode) {
        return appCode.endsWith("cpu.fullload");
    }

    public static boolean isMemLoad(String appCode) {
        return appCode.endsWith("mem.load");
    }

    public static boolean isK8SContainerOrPod(String appCode) {
        return appCode.startsWith("chaos.container") || appCode.startsWith("chaos.pod");
    }

    public static boolean isK8SContainer(String appCode) {
        return appCode.startsWith("chaos.container-");
    }

    public static boolean isK8SPod(String appCode) {
        return appCode.startsWith("chaos.pod-");
    }

    //要对node做特殊处理
    public static boolean isK8S(String appCode) {
        return appCode.startsWith("chaos.container-") || appCode.startsWith("chaos.pod-");
    }


    public static boolean isWin(String appCode) {
        return appCode.startsWith("chaos.windows-");
    }

    public static boolean isFromChaosBlade(String appCode) {
        return appCode != null && appCode.startsWith("chaos");
    }

    public static boolean isFromLitmusChaos(String appCode) {
        return appCode != null && appCode.startsWith("litmuschaos");
    }

    public static boolean isJvm(String appCode) {
        return isFromChaosBlade(appCode) && ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode) != null
            && ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode).isJvm();
    }

    public static boolean isJvmExcludeAgent(String appCode) {
        return isFromChaosBlade(appCode) && ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode) != null
            && ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode).isJvm() && !isJvmAgentInstall(appCode)
            && !isJvmAgentUninstall(appCode);
    }

    public static boolean isChaosRecovery(String appCode) {
        ChaosBladeAction chaosBladeAction = ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);
        return chaosBladeAction != null && ChaosBladeActionType.STOP_ATTACK.equals(chaosBladeAction.getActionType());
    }

    public static boolean isJvmScript(String appCode) {
        return appCode.endsWith("jvm.script");
    }

    public static boolean isFileAppend(String appCode) {
        return appCode.endsWith("file.append");
    }

    public static boolean exist(String appCode) {
        return ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode) != null;
    }

    public static String getRecoverName(String activityName) {
        return "recovery(" + activityName + ")";
    }

    public static boolean isJvmAgentInstall(String appCode) {
        return appCode.endsWith("jvm.install");
    }

    public static boolean isJvmAgentUninstall(String appCode) {
        return appCode.endsWith("jvm.uninstall");
    }

    public static String getInstallCode(String appCode) {
        if (isChaosUninstall(appCode)) {
            return appCode.replace(".uninstall", ".install");
        }
        return null;
    }

    public static boolean isChaosUninstall(String appCode) {
        return isFromChaosBlade(appCode) && appCode.endsWith(".uninstall");
    }

    public static PhaseType getPhaseByAppCode(String appCode) {
        ChaosBladeAction action = ChaosBladeMetaData.getInstance().getChaosBladeAction(appCode);
        if (null == action) {
            return null;
        }

        ChaosBladeActionType actionType = action.getActionType();
        if (ChaosBladeActionType.STOP_ATTACK.equals(actionType) || ChaosBladeActionType.UNINSTALL_AGENT.equals(
            actionType)) {
            return PhaseType.RECOVER;
        }
        if (ChaosBladeActionType.ATTACK.equals(actionType)) {
            return PhaseType.ATTACK;
        }
        if (ChaosBladeActionType.INSTALL_AGENT.equals(actionType)) {
            return PhaseType.PREPARE;
        }
        return null;

    }

    public static String getAttackCodeByRecoveryCode(String recoveryCode) {
        return recoveryCode.replace(".stop", "").trim();
    }

    public static String getRecoverCode(String appCode) {
        if (Strings.isNullOrEmpty(appCode)) { return appCode; }
        if (appCode.endsWith(JVM_AGENT_INSTALL_SUFFIX)) { return appCode.replace("install", "uninstall"); }
        return appCode + ".stop";
    }

    public static boolean isUserMiniApp(String appCode) {
        return appCode.startsWith("chaosapp");
    }

    public static boolean isMetricMiniApp(String appCode) {
        return appCode.startsWith("chaosapp.metric");
    }

    public static boolean isHitCountApp(String appCode) {
        return appCode.startsWith("chaosapp.hits");
    }

    public static boolean isDisk(String appCode) {
        return appCode.endsWith("disk.burn") || appCode.endsWith("disk.iohang") || appCode.endsWith("disk.fill");
    }
}
