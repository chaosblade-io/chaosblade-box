package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.common.util.MiniAppUtils;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.scenario.ChaosTools;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskPusher;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Slf4j
@Component
public class LitmusChaosActivityAsyncCallback implements ActivityAsyncCallback {

    @Autowired
    private LitmusChaosInvoker litmusChaosInvoker;

    private static Integer MAX_INSTALL_TIME_MINUTES = 5;

    @Autowired
    private ExperimentTaskPusher experimentTaskPusher;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Override
    public void execute(AsyncCallBackContext asyncCallBackContext) {
        log.info("[LitmusChaosActivityAsyncCallback] start");
        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = asyncCallBackContext.getExperimentMiniAppTaskDO();
        ChaosBladeExpUidDO chaosBladeExpUidDO = asyncCallBackContext.getChaosBladeExpUidDO();
        String status = asyncCallBackContext.getStatus();

        log.info("[LitmusChaosActivityAsyncCallback] start, status: {}", status);
        if (Strings.isNullOrEmpty(status)) {
            if (installTimeOut(experimentMiniAppTaskDO)) {
                markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.name(),
                        CommonErrorCode.B_CHAOS_BLADE_ASYNC_TIMEOUT.getReadableMessage() + ",超时时间:"
                                + MAX_INSTALL_TIME_MINUTES + " 分钟");
            }
        }
        if ("Error".equals(status)) {
            markRunFailed(experimentMiniAppTaskDO, CommonErrorCode.B_CHAOS_BLADE_ASYNC_ERROR.name(),
                    asyncCallBackContext.getError());
        }
        if ("Success".equals(status)) {
            markRunSuccess(experimentMiniAppTaskDO);
        }
        JSONObject expStatusBean = new JSONObject();
        expStatusBean.put("status", status);
        expStatusBean.put("uid", Strings.isNullOrEmpty(asyncCallBackContext.getUid()) ? chaosBladeExpUidDO.getExpUid() : asyncCallBackContext.getUid());
        expStatusBean.put("error", asyncCallBackContext.getError());
        expStatusBean.put("toolType", asyncCallBackContext.getToolType());
        experimentMiniAppTaskDO.setData(expStatusBean.toJSONString());
        if (experimentMiniAppTaskDO.isFinished()) {
            log.info("[LitmusChaosActivityAsyncCallback] ActivityAsyncCallback: {}", experimentMiniAppTaskDO.getData());
            boolean success = activityTargetExecutionResultRepository.update(experimentMiniAppTaskDO);
            if (success) {
                experimentTaskPusher.pushByAsyncWay(experimentMiniAppTaskDO);
            }
        }
    }

    @Override
    public void expired(AsyncCallBackContext asyncCallBackContext) {
        litmusChaosInvoker.destroyByChaosBladeExpDO(asyncCallBackContext.getChaosBladeExpUidDO());
    }

    @Override
    public boolean support(String toolType) {
        return ChaosTools.CHAOS_BLADE.name().equals(toolType);
    }

    @Override
    public boolean supportByAppCode(String appCode) {
        return MiniAppUtils.isFromLitmusChaos(appCode);
    }

    private boolean installTimeOut(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        return System.currentTimeMillis() - experimentMiniAppTaskDO.getStartTime().getTime()
                > MAX_INSTALL_TIME_MINUTES * 60 * 1000;
    }

    public void markRunFailed(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String errorCode,
                              String errorMessage) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.FAILED.getValue());
        experimentMiniAppTaskDO.setErrorCode(errorCode);
        experimentMiniAppTaskDO.setErrorMessage(errorMessage);
    }

    public void markRunSuccess(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.SUCCESS.getValue());
        experimentMiniAppTaskDO.setErrorCode("");
        experimentMiniAppTaskDO.setErrorMessage("");
    }

    private boolean isNotK8sScene(String appCode) {
        return !PublicCloudUtil.isK8SByAppCode(appCode);
    }

}
