package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.annotation.ChaosComponent;
import com.alibaba.chaosblade.box.common.common.constant.PublicCloudConstant;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.*;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.MiniAppInvocationRecord;
import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.MiniAppInvocationRecordBuilder;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.common.sdk.ChaosBladeForCloud;
import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.entity.*;
import com.alibaba.chaosblade.box.common.sdk.util.CliUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeCreateExpRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladeInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade.ChaosBladePrepareExpRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.domain.K8sStatusQueryRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.log.RecordsRepo;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.PluginTypeUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.ProxyHelper;
import com.alibaba.chaosblade.box.dao.model.*;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.dao.repository.LicenseRepository;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author haibin
 *
 *
 */
@ChaosComponent
@Slf4j
public class PublicCloudChaosBladeInvoker implements ChaosBladeInvoker {

    @Autowired
    private ChaosBladeForCloud chaosBladeForCloud;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ExperimentTaskRepository experimentTaskRepository;

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    private static String SCOPE_DOCKER = "docker";

    private static String SCOPE_WINDOWS = "windows";

    @Override
    public ChaosModels getBladeModels() throws Exception {
        return chaosBladeForCloud.getChaosModels();
    }

    @Override
    public ModelArgs buildModelArgs(ChaosAppRequest chaosAppRequest, ChaosBladeAction chaosBladeAction) {
        ModelArgs modelArgs = new ModelArgs();
        fillModelArgs(chaosAppRequest, modelArgs, chaosBladeAction);
        return modelArgs;
    }



    @Override
    public String getScope(String target) {
        String[] splits = target.split("-");
        if (splits.length >= 2) {
            return splits[0];
        }
        return null;
    }

    public String getWinTarget(String target) {
        int i = target.indexOf("-");
        if(i <= 0) {
            return null;
        }
        return target.substring(i + 1);
    }

    @Override
    public PrepareArgs buildPrepareArgs(MiniAppInvokeContext miniAppInvokeContext, ChaosBladeAction chaosBladeAction) {
        ChaosAppRequest chaosAppRequest = miniAppInvokeContext.getChaosAppRequest();
        PrepareArgs modelArgs = new PrepareArgs();
        modelArgs
            .setType(chaosBladeAction.getPrepareType())
            .setScope(getScope(chaosBladeAction.getTarget()))
            .setFlags(chaosAppRequest.getAction())
            .setPort(String.valueOf(chaosAppRequest.getScope().getPort()))
            .setMachine(chaosAppRequest.getScope().getTargetIp());
        return modelArgs;

    }

    @Override
    public Response<String> prepareExp(
        ChaosBladePrepareExpRequest chaosBladePrepareExpRequest) {
        MiniAppInvokeContext miniAppInvokeContext = chaosBladePrepareExpRequest.getAppInvokeContext();
        String ak = miniAppInvokeContext.getActivityInvokeContext().getUserAk();
        String sk = miniAppInvokeContext.getActivityInvokeContext().getUserSk();
        UserIdentification userIdentification = getUserIdentification(miniAppInvokeContext.getHost(), false, null);
        PrepareArgs prepareArgs = buildPrepareArgs(miniAppInvokeContext,
            chaosBladePrepareExpRequest.getChaosBladeAction());
        return internalInvoke(() -> chaosBladeForCloud.prepareExpForCloud(
                prepareArgs, ak, sk, miniAppInvokeContext.getHost().getVpcId(), userIdentification.getCloudTag()), new Tracer() {
            @Override
            public void trace0(boolean success, Object response, long cost) {
                logInvocation(miniAppInvokeContext, userIdentification, prepareArgs, cost,
                    success,
                    response);
            }
        });
    }

    private void logInvocation(MiniAppInvokeContext miniAppInvokeContext, UserIdentification userIdentification,
        Object param, long cost, boolean success, Object response) {
        Map<String, Object> args = new HashMap<>();
        args.put("param", param);
        args.put("vpcId", userIdentification.getVpcId());
        args.put("tag", userIdentification.getCloudTag());
        String appCode = miniAppInvokeContext.getActivityInvokeContext().getAppCode();
        String ip = miniAppInvokeContext.getHost().getTargetIp();
        String configurationId = miniAppInvokeContext.getHost().getAppConfigurationId();
        MiniAppInvocationRecord miniAppInvocationRecord = MiniAppInvocationRecordBuilder
            .aMiniAppInvocationRecord().withRequest(args)
            .withAppCode(appCode)
            .withMiniAppType(MiniAppInvocationRecord.MINI_APP_TYPE_CHAOSBLADE)
            .withIp(ip)
            .withConfigurationId(configurationId)
            .build(success, response, cost);
        miniAppInvocationRecord.setMiniAppTaskId(miniAppInvokeContext.getMiniAppTaskId());
        ActivityTaskDO activityTaskDO = miniAppInvokeContext.getActivityInvokeContext().getActivityTask();
        miniAppInvocationRecord.setActivityTaskId(activityTaskDO.getTaskId());
        miniAppInvocationRecord.setExperimentTaskId(activityTaskDO.getExperimentTaskId());
        RecordsRepo.getMiniAppInvocationRecorder().record(miniAppInvocationRecord);
    }

    @Override
    public Response<String> createExp(
        ChaosBladeCreateExpRequest chaosBladeCreateExpRequest) {
        MiniAppInvokeContext miniAppInvokeContext = chaosBladeCreateExpRequest.getAppInvokeContext();
        String ak = miniAppInvokeContext.getActivityInvokeContext().getUserAk();
        String sk = miniAppInvokeContext.getActivityInvokeContext().getUserSk();
        ModelArgs modelArgs = buildModelArgs(miniAppInvokeContext.getChaosAppRequest(),
            chaosBladeCreateExpRequest.getChaosBladeAction());
        UserIdentification userIdentification = getUserIdentification(miniAppInvokeContext.getHost(), false, null);
        return internalInvoke(() -> {
            if (PublicCloudUtil.isK8SByScope(modelArgs.getScope())) {
                Response<K8sResultBean> response = chaosBladeForCloud
                    .createK8sExpForCloud(modelArgs, ak, sk, miniAppInvokeContext.getHost().getVpcId(),
                        userIdentification.getCloudTag());

                return new Response<>(response.getRequestId(), response.getCode(), response.isSuccess(),
                    JSON.toJSONString(response.getResult()), response.getError());
            }
            return chaosBladeForCloud.createExpForCloud(modelArgs,
                ak, sk, miniAppInvokeContext.getHost().getVpcId(), userIdentification.getCloudTag());
        }, new Tracer() {
            @Override
            public void trace0(boolean success, Object response, long cost) {
                logInvocation(miniAppInvokeContext, userIdentification, modelArgs, cost,
                    success,
                    response);
            }
        });
    }

    private <T> Response<T> internalInvoke(Supplier<Response<T>> supplier, Tracer tracer) {
        StopWatch stopWatch = StopWatch.createStarted();
        Response<T> result = supplier.get();
        stopWatch.stop();
        tracer.trace(result.isSuccess(), result, stopWatch.getTime());
        return result;
    }

    private abstract class Tracer {

        public void trace(boolean success, Object response, long cost) {
            try {
                trace0(success, response, cost);
            } catch (Exception ignore) {

            }
        }

        public abstract void trace0(boolean success, Object response, long cost);
    }

    @Override
    public Response<String> destroyExp(
        ChaosBladeDestroyExpRequest chaosBladeDestroyExpRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeDestroyExpRequest.getHost(), true,
            chaosBladeDestroyExpRequest.getExpId());
        return destroyExp0(chaosBladeDestroyExpRequest.getAppCode(), chaosBladeDestroyExpRequest.getExpId(),
            chaosBladeDestroyExpRequest.getHost().getTargetIp(), String.valueOf(chaosBladeDestroyExpRequest.getHost().getPort()),
                userIdentification);
    }

    private Response<String> destroyExp0(String appCode, String expId, String targetIp, String port,
        UserIdentification userIdentification) {

        userIdentification.setExpId(expId);
        return internalInvoke(new Supplier<Response<String>>() {
            @Override
            public Response<String> get() {
                if (PublicCloudUtil.isK8SByAppCode(appCode)) {
                    Response<K8sResultBean> response = chaosBladeForCloud
                        .destroyK8sExpForCloud(expId,
                            targetIp, port, userIdentification.ak, userIdentification.sk,
                            userIdentification.getVpcId(), userIdentification.getCloudTag());
                    return new Response<>(response.getRequestId(), response.getCode(), response.isSuccess(),
                        JSON.toJSONString(response.getResult()), response.getError());
                }
                return chaosBladeForCloud.destroyExpForCloud(expId,
                    targetIp, port, userIdentification.getAk(), userIdentification.getSk(),
                    userIdentification.getVpcId(), userIdentification.getCloudTag());
            }
        }, new Tracer() {
            @Override
            public void trace0(boolean success, Object response, long cost) {
                PublicCloudChaosBladeInvoker.this.logInvocation(success, response, cost, userIdentification, appCode,
                    targetIp);
            }
        });

    }

    private void logInvocation(boolean success, Object response, long cost, UserIdentification userIdentification,
        String appCode, String targetIp) {
        Map<String, Object> args = new HashMap<>();
        args.put("vpcId", userIdentification.getVpcId());
        args.put("tag", userIdentification.getCloudTag());
        args.put("expId", userIdentification.getExpId());
        args.put("host", userIdentification.getHost());
        MiniAppInvocationRecord miniAppInvocationRecord = MiniAppInvocationRecordBuilder
            .aMiniAppInvocationRecord().withRequest(args)
            .withAppCode(appCode)
            .withMiniAppType(MiniAppInvocationRecord.MINI_APP_TYPE_CHAOSBLADE)
            .withIp(targetIp)
            .build(success, response, cost);
        RecordsRepo.getMiniAppInvocationRecorder().record(miniAppInvocationRecord);
    }

    @Override
    public Response<String> revokeExp(
        ChaosBladeRevokeExpRequest chaosBladeRevokeExpRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeRevokeExpRequest.getHost(), true,
            chaosBladeRevokeExpRequest.getExpId());
        return internalInvoke(new Supplier<Response<String>>() {
            @Override
            public Response<String> get() {
                return chaosBladeForCloud.revokeExpForCloud(chaosBladeRevokeExpRequest.getExpId(),
                    chaosBladeRevokeExpRequest.getHost().getTargetIp(),
                        String.valueOf(chaosBladeRevokeExpRequest.getHost().getPort()),
                        userIdentification.getAk(),
                    userIdentification.getSk(),
                    userIdentification.getVpcId(), userIdentification.getCloudTag());
            }
        }, new Tracer() {
            @Override
            public void trace0(boolean success, Object response, long cost) {
                PublicCloudChaosBladeInvoker.this.logInvocation(success, response, cost, userIdentification, null,
                    chaosBladeRevokeExpRequest.getHost().getTargetIp());
            }
        });

    }

    @Override
    public Response<ExpStatusBean> getExpStatus(
        ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeExpQueryRequest.getHost(), true,
            chaosBladeExpQueryRequest.getExpId());
        return chaosBladeForCloud.getExpStatusForCloud(chaosBladeExpQueryRequest.getExpId(),
            chaosBladeExpQueryRequest.getHost().getTargetIp(),
                String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
                userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<List<ExpStatusBean>> getExpStatusList(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeHostQueryRequest.getHost(), true, null);
        return chaosBladeForCloud.getExpStatusListForCloud(chaosBladeHostQueryRequest.getHost().getTargetIp(),
            String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<String[]> getNetworkDevice(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeHostQueryRequest.getHost(), true, null);
        return chaosBladeForCloud.getNetworkDeviceForCloud(chaosBladeHostQueryRequest.getHost().getTargetIp(),
            String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<String[]> getDiskDevice(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeHostQueryRequest.getHost(), true,
            null);
        return chaosBladeForCloud.getDiskDeviceForCloud(chaosBladeHostQueryRequest.getHost().getTargetIp(),
                String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<Long> getHitCountInJvm(
        ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeExpQueryRequest.getHost(), true,
            chaosBladeExpQueryRequest.getExpId());
        return internalInvoke(new Supplier<Response<Long>>() {
            @Override
            public Response<Long> get() {
                if (chaosBladeExpQueryRequest.isK8s()) {
                    return getK8SHitCountInJvm(chaosBladeExpQueryRequest, userIdentification);
                }
                return chaosBladeForCloud.getHitCountInJvmForCloud(chaosBladeExpQueryRequest.getHost().getTargetIp(),
                        String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
                    userIdentification.getAk(), userIdentification.getSk(),
                    userIdentification.getVpcId(), userIdentification.getCloudTag(),
                    chaosBladeExpQueryRequest.getExpId());
            }
        }, new Tracer() {
            @Override
            public void trace0(boolean success, Object response, long cost) {
                PublicCloudChaosBladeInvoker.this.logInvocation(success, response, cost, userIdentification, null,
                    chaosBladeExpQueryRequest.getHost().getTargetIp());
            }
        });

    }

    private Response<Long> getK8SHitCountInJvm(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest,
        UserIdentification userIdentification) {
        Response<K8sHitCountBean> k8sHitCountBeanResponse = chaosBladeForCloud.getHitCountInK8sJvmForCloud(
            chaosBladeExpQueryRequest.getHost().getTargetIp(),
                String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag(), chaosBladeExpQueryRequest.getExpId());
        Long count = 0L;
        if (!k8sHitCountBeanResponse.isSuccess()) {
            log.error("getK8SHitCountInJvm failed,expId:{},ip:{},error:{}", chaosBladeExpQueryRequest.getExpId(),
                chaosBladeExpQueryRequest.getHost().getTargetIp(), k8sHitCountBeanResponse.getError());
        } else {
            K8sHitCountBean k8sHitCountBean = k8sHitCountBeanResponse.getResult();
            if (CollectionUtil.isNullOrEmpty(k8sHitCountBean.getMetrics())) {
                count = 0L;
            } else {
                count = k8sHitCountBean.getMetrics().get(0).getCount();
            }
        }
        return new Response<>(k8sHitCountBeanResponse.getRequestId(), k8sHitCountBeanResponse.getCode(),
            k8sHitCountBeanResponse.isSuccess(), count, k8sHitCountBeanResponse.getError());
    }

    @Override
    public Response<String> getProcessIdByKey(
        ChaosBladeProcessQueryRequest chaosBladeProcessQueryRequest) {
        return null;
    }

    @Override
    public Response<PrepareStatusBean> getPrepareStatus(
        ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeExpQueryRequest.getHost(), true,
            chaosBladeExpQueryRequest.getExpId());
        return chaosBladeForCloud.getPrepareStatusForCloud(chaosBladeExpQueryRequest.getExpId(),
            chaosBladeExpQueryRequest.getHost().getTargetIp(),
            String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(), userIdentification.getVpcId(),
            userIdentification.getCloudTag());
    }

    @Override
    public Response<List<PrepareStatusBean>> getPrepareStatusList(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(chaosBladeHostQueryRequest.getHost(), true,
            null);
        return chaosBladeForCloud.getPrepareStatusListForCloud(chaosBladeHostQueryRequest.getHost().getTargetIp(),
            String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
            userIdentification.getAk(), userIdentification.getSk(), userIdentification.getVpcId(),
            userIdentification.getCloudTag());
    }

    @Override
    public Response<String[]> getDiskBlockDevice(
        ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
        return null;
    }

    @Override
    public Response<String> destroyExpWithoutUid(ChaosBladeAction chaosBladeAction, ChaosAppRequest chaosAppRequest, String ak, String sk) {
        ModelArgs modelArgs = buildModelArgs(chaosAppRequest, chaosBladeAction);
        UserIdentification userIdentification = getUserIdentification((Host) chaosAppRequest.getScope(), true,
                null);
        return chaosBladeForCloud.destroyExpForCloudWithoutUid(modelArgs,ak,sk,
                chaosAppRequest.getScope().getVpcId(),userIdentification.getCloudTag());
    }

    @Override
    public Response<String> destroyByChaosBladeExpDO(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(chaosBladeExpUidDO.getDeviceConfigurationId());
        if (deviceDO == null) {
            throw new RuntimeException("Not find device by configurationId:" + chaosBladeExpUidDO.getConfigurationId());
        }
        UserIdentification userIdentification = getUserIdentification(deviceDO, chaosBladeExpUidDO);
        return destroyExp0(chaosBladeExpUidDO.getAppCode(), chaosBladeExpUidDO.getExpUid(),
            deviceDO.getPrivateIp(), String.valueOf(deviceDO.getPort()), userIdentification);
    }

    public Response<K8sResultBean> queryK8sExp(
        K8sStatusQueryRequest k8SStatusQueryRequest) {
        UserIdentification userIdentification = getUserIdentification(k8SStatusQueryRequest.getHost(), true,
            k8SStatusQueryRequest.getExpId());
        String cmd = k8SStatusQueryRequest.isCreate() ? CliUtil.CREATE : CliUtil.DESTROY;
        return chaosBladeForCloud.queryK8sExpForCloud(k8SStatusQueryRequest.getExpId(), cmd,
            k8SStatusQueryRequest.getHost().getTargetIp(), String.valueOf(k8SStatusQueryRequest.getHost().getPort()),
                userIdentification.getAk(), userIdentification.getSk(),
            userIdentification.getVpcId(), userIdentification.getCloudTag());
    }




    @Override
    public Response<String> checkForCloud(ChaosBladePreCheckRequest chaosBladePreCheckRequest) {
        String ak = chaosBladePreCheckRequest.getAk();
        String sk = chaosBladePreCheckRequest.getSk();
        log.info("[PublicCloudChaosBladeInvoker][checkLicenseResult] pre check env ak:{},sk:{}", ak, sk);
        UserIdentification userIdentification = getUserIdentification(chaosBladePreCheckRequest.getHost(), false, null);
        return chaosBladeForCloud.checkForCloud(
            buildModelArgs(chaosBladePreCheckRequest.getChaosAppRequest(),
                chaosBladePreCheckRequest.getChaosBladeAction()),
            ak, sk, chaosBladePreCheckRequest.getHost().getVpcId(), userIdentification.getCloudTag(),
            chaosBladePreCheckRequest.getEvnType());
    }

    @Override
    public Response<K8sResultBean> queryK8sExp(Host host, String expId,boolean isCreate) {
        UserIdentification userIdentification = getUserIdentification(host, true,
                expId);
        String cmd = isCreate ? CliUtil.CREATE : CliUtil.DESTROY;
        return chaosBladeForCloud.queryK8sExpForCloud(expId, cmd,
                host.getTargetIp(), String.valueOf(host.getPort()),userIdentification.getAk(), userIdentification.getSk(),
                userIdentification.getVpcId(), userIdentification.getCloudTag());
    }

    @Override
    public Response<String> uninstallAgent(Host host) {
        return chaosBladeForCloud.uninstallAgent(host.getTargetIp(), String.valueOf(host.getPort()));
    }

    @Override
    public Response<String> pingAgent(Host host) {
        return chaosBladeForCloud.pingAgent(host.getTargetIp(), String.valueOf(host.getPort()));
    }


    @Data
    public static class UserIdentification implements Serializable {
        private String ak;

        private String sk;

        private String vpcId;

        private String cloudTag;

        private String expId;

        private Host host;

    }

    public PaasTransportService getTransportService() {
        return chaosBladeForCloud.getTransportService();
    }

    private String buildCloudTag(Integer deviceType, String ip) {
        return ProxyHelper.buildProxyTag(PluginTypeUtil.getPluginTypeByDeviceType(deviceType), ip, null);
    }

    private UserIdentification getUserIdentification(DeviceDO deviceDO, ChaosBladeExpUidDO chaosBladeExpUidDO) {
        UserIdentification userIdentification = new UserIdentification();
        userIdentification.setVpcId(deviceDO.getVpcId());
        userIdentification.setCloudTag(buildCloudTag(deviceDO.getDeviceType(), deviceDO.getPrivateIp()));
        userIdentification.setAk(chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_AK));
        userIdentification.setSk(chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_SK));
        return userIdentification;
    }

    private UserIdentification getUserIdentification(Host host, boolean reloadAkSk, String expId) {
        UserIdentification userIdentification = new UserIdentification();
        userIdentification.setHost(host);
        if (host.getVpcId() != null) {
            userIdentification.setVpcId(host.getVpcId());
        }
        if (host.getDeviceType() == null) {
            DeviceDO deviceDO = null;
            if (host.getDeviceConfigurationId() != null) {
                deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
            }
            if (deviceDO == null) {
                deviceDO = deviceRepository.findByPrivateIp(host.getTargetIp());
            }
            if (deviceDO != null) {
                userIdentification.setVpcId(deviceDO.getVpcId());
                userIdentification.setCloudTag(buildCloudTag(deviceDO.getDeviceType(), host.getTargetIp()));
            }
        } else {
            userIdentification.setCloudTag(buildCloudTag(host.getDeviceType(), host.getTargetIp()));
        }
        if (!reloadAkSk) {
            return userIdentification;
        }

        ChaosUser chaosUser = ChaosRequestContextHolder.getApplicationContext().map(ChaosApplicationContext::getLoginUser)
            .orElse(null);
        String ak = null;
        String sk = null;
        if (chaosUser != null) {
            ak = chaosUser.getUserId();
            sk = chaosUser.getLicense();
        } else {
            if (expId != null) {
                ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findLastByExpUid(expId);
                ak = chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_AK);
                sk = chaosBladeExpUidDO.getAttribute(PublicCloudConstant.USER_SK);
                if (ak == null) {
                    UserDo userDo = findByExpId(chaosBladeExpUidDO);
                    ak = userDo.getUserId();
                    sk = userDo.getLicense();
                }
            }
        }
        userIdentification.setAk(ak);
        userIdentification.setSk(sk);

        return userIdentification;
    }

    private UserDo findByExpId(ChaosBladeExpUidDO chaosBladeExpUidDO) {
        ExperimentTaskDO experimentTaskDO = experimentTaskRepository.findById(chaosBladeExpUidDO.getExperimentTaskId())
                .get();
        return findByUserId(experimentTaskDO.getUserId());
    }

    private UserDo findByUserId(String userId) {
        return licenseRepository.getUserLicense(userId).get();
    }

    private void fillModelArgs(ChaosAppRequest chaosAppRequest, ModelArgs modelArgs, ChaosBladeAction chaosBladeAction) {
        String scope = getScope(chaosBladeAction.getTarget());
        String target = getWinTarget(chaosBladeAction.getTarget());
        String port = String.valueOf(chaosAppRequest.getScope().getPort());
        if (SCOPE_DOCKER.equals(scope)) {
            modelArgs.setTarget(scope)
                .setAction(chaosBladeAction.getAction()).setFlags(chaosAppRequest.getAction())
                .setSubTarget(chaosBladeAction.getSubTarget())
                .setMatcherFlags(chaosAppRequest.getMatcher())
                .setPort(port)
                .setMachine(chaosAppRequest.getScope().getTargetIp());
        } else if (SCOPE_WINDOWS.equals(scope)) {
            modelArgs.setTarget(target)
                .setAction(chaosBladeAction.getAction()).setFlags(chaosAppRequest.getAction())
                .setMatcherFlags(chaosAppRequest.getMatcher())
                .setScope(getScope(chaosBladeAction.getTarget()))
                .setPort(port)
                .setMachine(chaosAppRequest.getScope().getTargetIp());
        } else {
            modelArgs.setTarget(chaosBladeAction.getTarget())
                .setAction(chaosBladeAction.getAction()).setFlags(chaosAppRequest.getAction())
                .setMatcherFlags(chaosAppRequest.getMatcher())
                .setScope(getScope(chaosBladeAction.getTarget()))
                .setPort(port)
                .setMachine(chaosAppRequest.getScope().getTargetIp());
        }
    }

}
