package com.alibaba.chaosblade.box.service.command.task;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.ChaosBladeStatus;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.sdk.entity.PrepareStatusBean;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.ExperimentTaskPusher;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Component
public class JavaAgentInstallCallback {

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    private static Integer MAX_INSTALL_TIME_MINUTES = 5;

    @Autowired
    private ExperimentTaskPusher experimentTaskPusher;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    static String TIMEOUT_REASON = "应用当前load过高";

    public void execute(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, ChaosBladeExpUidDO chaosBladeExpUidDO,
                        PrepareStatusBean prepareStatusBean) {
        if (prepareStatusBean == null) {
            Host host = new Host();
            host.setIp(experimentMiniAppTaskDO.getHostIp());
            host.setDeviceConfigurationId(experimentMiniAppTaskDO.getDeviceConfigurationId());
            Response<PrepareStatusBean> prepareStatusBeanResponse = chaosBladeInvoker.getPrepareStatus(
                    new ChaosBladeExpQueryRequest(host, chaosBladeExpUidDO.getExpUid()));
            prepareStatusBean = prepareStatusBeanResponse.getResult();
            if (!prepareStatusBeanResponse.isSuccess()) {
                markInstallFailed(experimentMiniAppTaskDO, CommonErrorCode.B_JAVA_AGENT_CREATED_ERROR.name(),
                        prepareStatusBeanResponse.getError());
            }
        }
        if (prepareStatusBean != null) {
            if (prepareStatusBean.isRunning()) {
                markInstallSuccess(experimentMiniAppTaskDO);
            }
            if (ChaosBladeStatus.Created.name().equals(prepareStatusBean.getStatus())) {
                if (installTimeOut(experimentMiniAppTaskDO)) {
                    markInstallFailed(experimentMiniAppTaskDO, CommonErrorCode.B_JAVA_AGENT_CREATED_TIMEOUT.name(),
                            CommonErrorCode.B_JAVA_AGENT_CREATED_TIMEOUT.getReadableMessage() + ",超时时间:"
                                    + MAX_INSTALL_TIME_MINUTES + " 分钟,可能原因:" + TIMEOUT_REASON);
                }
            }
            if (ChaosBladeStatus.Revoked.name().equals(prepareStatusBean.getStatus())) {
                markInstallFailed(experimentMiniAppTaskDO, CommonErrorCode.B_JAVA_AGENT_REVOKED.name(),
                        CommonErrorCode.B_JAVA_AGENT_REVOKED.getReadableMessage());
            }
            if (ChaosBladeStatus.Error.name().equals(prepareStatusBean.getStatus())) {
                markInstallFailed(experimentMiniAppTaskDO, CommonErrorCode.B_JAVA_AGENT_CREATED_ERROR.name(),
                        prepareStatusBean.getError());
            }
            chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_PID, prepareStatusBean.getPid());
            chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_PROCESS_NAME,
                    prepareStatusBean.getProcess());
            chaosBladeExpUidDO.addAttribute(ChaosBladeExpUidDO.ATTRIBUTE_PORT, prepareStatusBean.getPort());
            experimentMiniAppTaskDO.setData(JSON.toJSONString(prepareStatusBean));
        }
        if (experimentMiniAppTaskDO.isFinished()) {
            chaosBladeExpUidRepository.update(chaosBladeExpUidDO);
            boolean success = activityTargetExecutionResultRepository.update(experimentMiniAppTaskDO);
            if (success) {
                experimentTaskPusher.pushByAsyncWay(experimentMiniAppTaskDO);
            }
        }
    }

    private boolean installTimeOut(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        return System.currentTimeMillis() - experimentMiniAppTaskDO.getStartTime().getTime()
                > MAX_INSTALL_TIME_MINUTES * 60 * 1000;
    }

    public void markInstallFailed(ExperimentMiniAppTaskDO experimentMiniAppTaskDO, String errorCode,
                                  String errorMessage) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.FAILED.getValue());
        experimentMiniAppTaskDO.setErrorCode(errorCode);
        experimentMiniAppTaskDO.setErrorMessage(errorMessage);
        experimentMiniAppTaskDO.setGmtEnd(new Date());
    }

    public void markInstallSuccess(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        experimentMiniAppTaskDO.setState(StateEnum.FINISHED.getValue());
        experimentMiniAppTaskDO.setResult(ResultEnum.SUCCESS.getValue());
        experimentMiniAppTaskDO.setGmtEnd(new Date());
    }

}
