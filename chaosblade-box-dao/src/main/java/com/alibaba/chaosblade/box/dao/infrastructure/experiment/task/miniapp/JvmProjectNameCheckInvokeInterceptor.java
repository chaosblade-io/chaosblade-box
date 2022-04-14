package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.miniapp;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.experiment.task.flow.interceptor.InterceptorDesc;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Java的场景注入，一般用户在注入环节是不会配置ProcessName的，所以我们自动根据准备阶段的进程名字填写Process，
 * 防止执行注入时候找不到进程
 *
 * @author haibin.lhb
 */
@Component
@InterceptorDesc("Check process name under java attack")
public class JvmProjectNameCheckInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Override
    protected boolean preHandle(MiniAppInvokeContext miniAppInvokeContext, ChaosAppResponse chaosAppResponse) {
        ActivityInvokeContext activityInvokeContext = miniAppInvokeContext.getActivityInvokeContext();
        String appCode = activityInvokeContext.getExecutableAppCode();
        if (MiniAppUtils.isJvmExcludeAgent(appCode) && PhaseType.ATTACK.equals(
                activityInvokeContext.getActivity().getActivityTaskDO().getPhase())) {
            ExperimentTaskDO experimentTaskDO = activityInvokeContext.getContextData().getExperimentTaskDO();
            String processName = activityInvokeContext.getActivity().getArguments().getAllArguments().get("process");
            if (Strings.isNullOrEmpty(processName)) {
                ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.
                        findLastByExperimentTaskIdAndHostAndAppCodeAndNotExpired(experimentTaskDO.getTaskId(),
                                miniAppInvokeContext.getHost().getIp(),
                                MiniAppUtils.AGENT_INSTALL);
                if (chaosBladeExpUidDO == null) { return true; }
                miniAppInvokeContext.addArgs("process", chaosBladeExpUidDO.getAttribute(
                        ChaosBladeExpUidDO.ATTRIBUTE_PROCESS_NAME));
                miniAppInvokeContext.addArgs("pid", chaosBladeExpUidDO.getAttribute(
                        ChaosBladeExpUidDO.ATTRIBUTE_PID));
            }
        }
        return true;
    }

}
