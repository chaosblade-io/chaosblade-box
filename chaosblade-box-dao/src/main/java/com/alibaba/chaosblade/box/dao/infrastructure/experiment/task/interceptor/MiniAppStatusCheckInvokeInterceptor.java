package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity.ActivityExecuteResult;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ActivityInvokeInterruptException;
import com.alibaba.chaosblade.box.dao.infrastructure.app.function.UserSceneFunctionFilter;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 检查小程序的状态
 *
 * @author haibin
 *
 *
 */
@Component
public class MiniAppStatusCheckInvokeInterceptor extends BaseActivityInvokeInterceptor {

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private UserSceneFunctionFilter userSceneFunctionFilter;

    @Override
    public boolean preHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult) {
        PhaseType phaseType = activityInvokeContext.getStepExecuteContext().getPhase();
        //恢复阶段就算下架了也还是可以执行
        if (PhaseType.RECOVER.equals(phaseType)) { return true; }
        String appCode = activityInvokeContext.getAppCode();
        Optional<SceneFunctionDO> sceneFunctionDOOptional = sceneFunctionRepository.findByCode(appCode);
        if (!sceneFunctionDOOptional.isPresent()) {
            throw new ActivityInvokeInterruptException(CommonErrorCode.B_MINIAPP_NOT_EXIST.getFullMessage());
        } else {
            if (!userSceneFunctionFilter.filter(activityInvokeContext.getContextData().getUser(),
                sceneFunctionDOOptional.get())) {
                throw new ActivityInvokeInterruptException(CommonErrorCode.B_ERROR_APP_DISABLED.getFullMessage());
            }
        }
        return true;
    }

    @Override
    public void afterHandle(ActivityInvokeContext activityInvokeContext, ActivityExecuteResult activityExecuteResult,
        Throwable throwable) {
    }

    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
