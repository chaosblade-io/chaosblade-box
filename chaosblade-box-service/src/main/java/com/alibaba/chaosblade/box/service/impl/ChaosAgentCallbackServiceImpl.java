package com.alibaba.chaosblade.box.service.impl;


import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.ChaosBladeExpQueryRequest;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.common.sdk.entity.PrepareStatusBean;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async.ActivityAsyncCheckCommand;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.async.AsyncCallBackContext;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.ChaosAgentCallbackService;
import com.alibaba.chaosblade.box.service.command.agent.AgentClosedCommand;
import com.alibaba.chaosblade.box.service.command.agent.HeartBeatCommand;
import com.alibaba.chaosblade.box.service.command.agent.JavaAgentInstallCheckCommand;
import com.alibaba.chaosblade.box.service.command.agent.RegisteredRequestCommand;
import com.alibaba.chaosblade.box.service.command.agent.kubernetes.PodReportHandler;
import com.alibaba.chaosblade.box.service.model.agent.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author haibin
 *
 * 
 */
@Slf4j
@Component
public class ChaosAgentCallbackServiceImpl implements ChaosAgentCallbackService {

    protected static final String THREAD_AGENT_ASYNC_CALLBACK = "Agent-Async-Callback";

    @Autowired
    private CommandBus commandExecutorPool;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private ChaosBladeInvoker chaosBladeInvoker;

    @Autowired
    private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private PodReportHandler podReportHandler;

    @Autowired
    private CommandBus commandBus;

    @Autowired
    private ThreadPool threadPool;

    @Override
    public Response<Boolean> handleAgentHeartBeat(HeartBeatCallbackRequest heartBeatCallbackRequest) {
        return commandExecutorPool.syncRun(HeartBeatCommand.class, heartBeatCallbackRequest);
    }

    @Override
    public Response<ChaosAgentRegisterResultEntity> handleAgentRegisterRequest(
        RegisteredCallbackRequest registeredCallbackRequest) {
        return commandExecutorPool.syncRun(RegisteredRequestCommand.class, registeredCallbackRequest);
    }

    @Override
    public Response<Boolean> handleAgentClosedRequest(AgentClosedCallbackRequest agentClosedCallbackRequest) {
        return commandExecutorPool.syncRun(AgentClosedCommand.class, agentClosedCallbackRequest);
    }


    @Override
    public Response<String> handleJavaAgentInstall(AgentMetricRequest agentMetricRequest) {
        log.info("receive javaAgent install callback");
        String uid = agentMetricRequest.getUid();
        String status = agentMetricRequest.getStatus();
        String error = agentMetricRequest.getError();
        //根据uid查询当前java安装命令
        log.info("receive java agent install response,expuid:{},status:{},error:{}", uid, status, error);
        if (uid == null) {
            return Response.ofFailure(Response.Code.Parameter_Empty, "Uid Required");
        }
        if (status == null) {
            return Response.ofFailure(Response.Code.Parameter_Empty, "Status Required");
        }

        if (ChaosBladeInvoker.STATUS_CREATED.equals(status)) {
            log.info("status is created,ignore");
            return Response.ofSuccess("Agent is installing");
        }
        ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findByExpUid(uid);
        if (chaosBladeExpUidDO == null) {
            log.warn("not found chaosblade expuid record by uid:{}", uid);
            return Response.ofSuccess(null);
        }
        //查询一下状态
        if (ChaosBladeInvoker.STATUS_RUNNING.equals(status)) {
            Host host = new Host(chaosBladeExpUidDO.getHost(), -1);
            host.setDeviceConfigurationId(chaosBladeExpUidDO.getConfigurationId());
            Response<PrepareStatusBean> response = chaosBladeInvoker.getPrepareStatus(
                    new ChaosBladeExpQueryRequest(host,
                            chaosBladeExpUidDO.getExpUid()));
            if (!response.isSuccess()) {
                log.error("getPrepareStatus failed:" + JSON.toJSONString(response));
            }
        }
        //获取小程序的执行记录
        Optional<ExperimentMiniAppTaskDO> activityTargetTaskDOOptional = activityTargetExecutionResultRepository
                .findById(chaosBladeExpUidDO.getAppExecutionId());
        if (activityTargetTaskDOOptional.isPresent()) {
            commandExecutorPool.syncRun(JavaAgentInstallCheckCommand.class, activityTargetTaskDOOptional.get());
            return Response.ofSuccess(null);
        } else {
            log.warn("not found activity target task  record by taskid:{}", chaosBladeExpUidDO.getAppExecutionId());
            return Response.ofSuccess(null);
        }

    }

    @Override
    public Response<Map<String, String>>  handlerPodMetric(K8sPodsMetricRequest k8sPodsMetricRequest) {

        String userId = k8sPodsMetricRequest.getUserId();
        String configurationId = k8sPodsMetricRequest.getConfigurationId();
        if (userId.isEmpty() || configurationId.isEmpty()) {
            return Response.ofFailure(Response.Code.Parameter_Empty, "uid || cid is empty");
        }


        DeviceDO deviceDO = deviceRepository.findByConfigurationIdAndUserId(userId, configurationId);
        if (deviceDO == null){
            log.error("[pod metric handler] node not found");
            return Response.ofFailure(Response.Code.INVALID_Parameter, "uid || cid is invalid");
        }

        return podReportHandler.handler(k8sPodsMetricRequest, deviceDO);
    }

    @Override
    public Response<String>  handlerChaosbladeAsync(ChaosbladeAsyncRequest chaosbladeAsyncRequest) {
        String requestId = chaosbladeAsyncRequest.getRequestId();
        String uid = chaosbladeAsyncRequest.getUserId();
        String status = chaosbladeAsyncRequest.getStatus();
        log.info("[ActivityAsyncCallbackHandler] receive async request,requestId:{},configurationId:{}", requestId,
                chaosbladeAsyncRequest.getConfigurationId());

        if (uid.isEmpty() || status.isEmpty()) {
            log.warn("[ActivityAsyncCallbackHandler] failed, requestId:{}, error:{}", requestId, "uid || status Required");
            return Response.ofFailure(Response.Code.Parameter_Empty, "uid || status Required");
        }

        if (ChaosBladeInvoker.STATUS_CREATED.equals(status)) {
            log.info("[ActivityAsyncCallbackHandler] status is created, ignore");
            return Response.ofSuccess("Agent is installing");
        }

        ExperimentMiniAppTaskDO experimentMiniAppTaskDO = activityTargetExecutionResultRepository.findById(
                requestId).orElse(null);
        if (experimentMiniAppTaskDO == null) {
            log.warn("[ActivityAsyncCallbackHandler] Failed,requestId:{},error:{}", requestId, "not found miniAppTask record");
            return Response.ofFailure(Response.Code.Parameter_Empty, "not found injection record");
        }
        ChaosBladeExpUidDO chaosBladeExpUidDO = getChaosBladeExpUid(uid, requestId, experimentMiniAppTaskDO);
        if (chaosBladeExpUidDO == null) {
            log.warn("[ActivityAsyncCallbackHandler] Failed,requestId:{},error:{}", requestId, "not found injection record");
            return Response.ofFailure(Response.Code.Parameter_Empty, "not found injection record");
        }
        consumeRecord(uid, experimentMiniAppTaskDO, status, chaosbladeAsyncRequest.getError(), chaosbladeAsyncRequest.getToolType()).
                accept(chaosBladeExpUidDO);
        return Response.ofSuccess(null);
    }

    private Consumer<ChaosBladeExpUidDO> consumeRecord(String uid, ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                                                       String status, String error, String toolType) {
        return chaosBladeExpUidDO -> doCallback(chaosBladeExpUidDO, experimentMiniAppTaskDO, uid, status, error,
                toolType);
    }

    private void doCallback(ChaosBladeExpUidDO chaosBladeExpUidDO, ExperimentMiniAppTaskDO experimentMiniAppTaskDO,
                            String uid, String status, String error,
                            String toolType) {
        threadPool.executor(THREAD_AGENT_ASYNC_CALLBACK).execute(new Runnable() {
            @Override
            public void run() {
                commandBus.syncRun(ActivityAsyncCheckCommand.class,
                        new AsyncCallBackContext(experimentMiniAppTaskDO, chaosBladeExpUidDO, uid, status, error,
                                toolType));
            }
        });
    }

    /**
     * 1.首先根据UID查询
     * 2.根据UID查询不到,则有可能下发规则超时导致没获取到,因此要通过requestId和cid来获取
     *
     * @param uid       agent的注入ID，类似于expId
     * @param requestId 请求id
     * @return
     */
    private ChaosBladeExpUidDO getChaosBladeExpUid(String uid, String requestId,
                                                   ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findForAsyncCallback(uid, requestId).orElse(
                null);
        if (chaosBladeExpUidDO == null) {
            log.warn("not found chaosblade expId record by uid:{} or request id:{}", uid, requestId);
            DeviceDO deviceDO = getDevice(
                    experimentMiniAppTaskDO);
            if (deviceDO == null) {return null;}
            chaosBladeExpUidDO = chaosBladeExpUidRepository.createChaosBladeExpRecord(uid, experimentMiniAppTaskDO,
                    deviceDO.getPrivateIp(), true);
            log.warn("create chaosblade expId record by uid:{} or request id:{}", uid, requestId);
        }
        return chaosBladeExpUidDO;
    }

    private DeviceDO getDevice(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(
                experimentMiniAppTaskDO.getDeviceConfigurationId());
        if (deviceDO == null) {
            log.error("not found device by configurationId:{}",
                    experimentMiniAppTaskDO.getDeviceConfigurationId());
            return null;
        }
        return deviceDO;
    }

}
