/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppRequest;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.annotation.ChaosComponent;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.ChaosBladeAction;
import com.alibaba.chaosblade.box.common.common.domain.chaosblade.requst.*;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
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
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.fastjson.JSON;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
@ChaosComponent
@Slf4j
public class PublicCloudChaosBladeInvoker implements ChaosBladeInvoker {

  @Autowired private ChaosBladeForCloud chaosBladeForCloud;

  @Autowired private DeviceRepository deviceRepository;

  private static String SCOPE_DOCKER = "docker";

  private static String SCOPE_WINDOWS = "windows";

  @Override
  public ChaosModels getBladeModels() throws Exception {
    return chaosBladeForCloud.getChaosModels();
  }

  @Override
  public ModelArgs buildModelArgs(
      ChaosAppRequest chaosAppRequest, ChaosBladeAction chaosBladeAction) {
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
    if (i <= 0) {
      return null;
    }
    return target.substring(i + 1);
  }

  @Override
  public PrepareArgs buildPrepareArgs(
      MiniAppInvokeContext miniAppInvokeContext, ChaosBladeAction chaosBladeAction) {
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
  public Response<String> prepareExp(ChaosBladePrepareExpRequest chaosBladePrepareExpRequest) {
    MiniAppInvokeContext miniAppInvokeContext = chaosBladePrepareExpRequest.getAppInvokeContext();
    UserIdentification userIdentification = getUserIdentification(miniAppInvokeContext.getHost());
    PrepareArgs prepareArgs =
        buildPrepareArgs(miniAppInvokeContext, chaosBladePrepareExpRequest.getChaosBladeAction());
    return internalInvoke(
        () ->
            chaosBladeForCloud.prepareExpForCloud(
                prepareArgs,
                userIdentification.getAk(),
                userIdentification.getSk(),
                miniAppInvokeContext.getHost().getVpcId(),
                userIdentification.getCloudTag()),
        new Tracer() {
          @Override
          public void trace0(boolean success, Object response, long cost) {
            logInvocation(
                miniAppInvokeContext, userIdentification, prepareArgs, cost, success, response);
          }
        });
  }

  private void logInvocation(
      MiniAppInvokeContext miniAppInvokeContext,
      UserIdentification userIdentification,
      Object param,
      long cost,
      boolean success,
      Object response) {
    Map<String, Object> args = new HashMap<>();
    args.put("param", param);
    args.put("vpcId", userIdentification.getVpcId());
    args.put("tag", userIdentification.getCloudTag());
    String appCode = miniAppInvokeContext.getActivityInvokeContext().getAppCode();
    String ip = miniAppInvokeContext.getHost().getTargetIp();
    String configurationId = miniAppInvokeContext.getHost().getAppConfigurationId();
    MiniAppInvocationRecord miniAppInvocationRecord =
        MiniAppInvocationRecordBuilder.aMiniAppInvocationRecord()
            .withRequest(args)
            .withAppCode(appCode)
            .withMiniAppType(MiniAppInvocationRecord.MINI_APP_TYPE_CHAOSBLADE)
            .withIp(ip)
            .withConfigurationId(configurationId)
            .build(success, response, cost);
    miniAppInvocationRecord.setMiniAppTaskId(miniAppInvokeContext.getMiniAppTaskId());
    ActivityTaskDO activityTaskDO =
        miniAppInvokeContext.getActivityInvokeContext().getActivityTask();
    miniAppInvocationRecord.setActivityTaskId(activityTaskDO.getTaskId());
    miniAppInvocationRecord.setExperimentTaskId(activityTaskDO.getExperimentTaskId());
    RecordsRepo.getMiniAppInvocationRecorder().record(miniAppInvocationRecord);
  }

  @Override
  public Response<String> createExp(ChaosBladeCreateExpRequest chaosBladeCreateExpRequest) {
    MiniAppInvokeContext miniAppInvokeContext = chaosBladeCreateExpRequest.getAppInvokeContext();
    ModelArgs modelArgs =
        buildModelArgs(
            miniAppInvokeContext.getChaosAppRequest(),
            chaosBladeCreateExpRequest.getChaosBladeAction());
    UserIdentification userIdentification = getUserIdentification(miniAppInvokeContext.getHost());
    return internalInvoke(
        () -> {
          if (PublicCloudUtil.isK8SByScope(modelArgs.getScope())) {
            Response<K8sResultBean> response =
                chaosBladeForCloud.createK8sExpForCloud(
                    modelArgs,
                    userIdentification.getAk(),
                    userIdentification.getSk(),
                    miniAppInvokeContext.getHost().getVpcId(),
                    userIdentification.getCloudTag());

            return new Response<>(
                response.getRequestId(),
                response.getCode(),
                response.isSuccess(),
                JSON.toJSONString(response.getResult()),
                response.getError());
          }
          return chaosBladeForCloud.createExpForCloud(
              modelArgs,
              userIdentification.getAk(),
              userIdentification.getSk(),
              miniAppInvokeContext.getHost().getVpcId(),
              userIdentification.getCloudTag());
        },
        new Tracer() {
          @Override
          public void trace0(boolean success, Object response, long cost) {
            logInvocation(
                miniAppInvokeContext, userIdentification, modelArgs, cost, success, response);
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
  public Response<String> destroyExp(ChaosBladeDestroyExpRequest chaosBladeDestroyExpRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeDestroyExpRequest.getHost());
    return destroyExp0(
        chaosBladeDestroyExpRequest.getAppCode(),
        chaosBladeDestroyExpRequest.getExpId(),
        chaosBladeDestroyExpRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeDestroyExpRequest.getHost().getPort()),
        userIdentification);
  }

  private Response<String> destroyExp0(
      String appCode,
      String expId,
      String targetIp,
      String port,
      UserIdentification userIdentification) {

    userIdentification.setExpId(expId);
    return internalInvoke(
        new Supplier<Response<String>>() {
          @Override
          public Response<String> get() {
            if (PublicCloudUtil.isK8SByAppCode(appCode)) {
              Response<K8sResultBean> response =
                  chaosBladeForCloud.destroyK8sExpForCloud(
                      expId,
                      targetIp,
                      port,
                      userIdentification.ak,
                      userIdentification.sk,
                      userIdentification.getVpcId(),
                      userIdentification.getCloudTag());
              return new Response<>(
                  response.getRequestId(),
                  response.getCode(),
                  response.isSuccess(),
                  JSON.toJSONString(response.getResult()),
                  response.getError());
            }
            return chaosBladeForCloud.destroyExpForCloud(
                expId,
                targetIp,
                port,
                userIdentification.getAk(),
                userIdentification.getSk(),
                userIdentification.getVpcId(),
                userIdentification.getCloudTag());
          }
        },
        new Tracer() {
          @Override
          public void trace0(boolean success, Object response, long cost) {
            PublicCloudChaosBladeInvoker.this.logInvocation(
                success, response, cost, userIdentification, appCode, targetIp);
          }
        });
  }

  private void logInvocation(
      boolean success,
      Object response,
      long cost,
      UserIdentification userIdentification,
      String appCode,
      String targetIp) {
    Map<String, Object> args = new HashMap<>();
    args.put("vpcId", userIdentification.getVpcId());
    args.put("tag", userIdentification.getCloudTag());
    args.put("expId", userIdentification.getExpId());
    args.put("host", userIdentification.getHost());
    MiniAppInvocationRecord miniAppInvocationRecord =
        MiniAppInvocationRecordBuilder.aMiniAppInvocationRecord()
            .withRequest(args)
            .withAppCode(appCode)
            .withMiniAppType(MiniAppInvocationRecord.MINI_APP_TYPE_CHAOSBLADE)
            .withIp(targetIp)
            .build(success, response, cost);
    RecordsRepo.getMiniAppInvocationRecorder().record(miniAppInvocationRecord);
  }

  @Override
  public Response<String> revokeExp(ChaosBladeRevokeExpRequest chaosBladeRevokeExpRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeRevokeExpRequest.getHost());
    return internalInvoke(
        new Supplier<Response<String>>() {
          @Override
          public Response<String> get() {
            return chaosBladeForCloud.revokeExpForCloud(
                chaosBladeRevokeExpRequest.getExpId(),
                chaosBladeRevokeExpRequest.getHost().getTargetIp(),
                String.valueOf(chaosBladeRevokeExpRequest.getHost().getPort()),
                userIdentification.getAk(),
                userIdentification.getSk(),
                userIdentification.getVpcId(),
                userIdentification.getCloudTag());
          }
        },
        new Tracer() {
          @Override
          public void trace0(boolean success, Object response, long cost) {
            PublicCloudChaosBladeInvoker.this.logInvocation(
                success,
                response,
                cost,
                userIdentification,
                null,
                chaosBladeRevokeExpRequest.getHost().getTargetIp());
          }
        });
  }

  @Override
  public Response<ExpStatusBean> getExpStatus(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeExpQueryRequest.getHost());
    return chaosBladeForCloud.getExpStatusForCloud(
        chaosBladeExpQueryRequest.getExpId(),
        chaosBladeExpQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<List<ExpStatusBean>> getExpStatusList(
      ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeHostQueryRequest.getHost());
    return chaosBladeForCloud.getExpStatusListForCloud(
        chaosBladeHostQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String[]> getNetworkDevice(
      ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeHostQueryRequest.getHost());
    return chaosBladeForCloud.getNetworkDeviceForCloud(
        chaosBladeHostQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String[]> getDiskDevice(ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeHostQueryRequest.getHost());
    return chaosBladeForCloud.getDiskDeviceForCloud(
        chaosBladeHostQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<Long> getHitCountInJvm(ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeExpQueryRequest.getHost());
    return internalInvoke(
        new Supplier<Response<Long>>() {
          @Override
          public Response<Long> get() {
            if (chaosBladeExpQueryRequest.isK8s()) {
              return getK8SHitCountInJvm(chaosBladeExpQueryRequest, userIdentification);
            }
            return chaosBladeForCloud.getHitCountInJvmForCloud(
                chaosBladeExpQueryRequest.getHost().getTargetIp(),
                String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
                userIdentification.getAk(),
                userIdentification.getSk(),
                userIdentification.getVpcId(),
                userIdentification.getCloudTag(),
                chaosBladeExpQueryRequest.getExpId());
          }
        },
        new Tracer() {
          @Override
          public void trace0(boolean success, Object response, long cost) {
            PublicCloudChaosBladeInvoker.this.logInvocation(
                success,
                response,
                cost,
                userIdentification,
                null,
                chaosBladeExpQueryRequest.getHost().getTargetIp());
          }
        });
  }

  private Response<Long> getK8SHitCountInJvm(
      ChaosBladeExpQueryRequest chaosBladeExpQueryRequest, UserIdentification userIdentification) {
    Response<K8sHitCountBean> k8sHitCountBeanResponse =
        chaosBladeForCloud.getHitCountInK8sJvmForCloud(
            chaosBladeExpQueryRequest.getHost().getTargetIp(),
            String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
            userIdentification.getAk(),
            userIdentification.getSk(),
            userIdentification.getVpcId(),
            userIdentification.getCloudTag(),
            chaosBladeExpQueryRequest.getExpId());
    Long count = 0L;
    if (!k8sHitCountBeanResponse.isSuccess()) {
      log.error(
          "getK8SHitCountInJvm failed,expId:{},ip:{},error:{}",
          chaosBladeExpQueryRequest.getExpId(),
          chaosBladeExpQueryRequest.getHost().getTargetIp(),
          k8sHitCountBeanResponse.getError());
    } else {
      K8sHitCountBean k8sHitCountBean = k8sHitCountBeanResponse.getResult();
      if (CollectionUtil.isNullOrEmpty(k8sHitCountBean.getMetrics())) {
        count = 0L;
      } else {
        count = k8sHitCountBean.getMetrics().get(0).getCount();
      }
    }
    return new Response<>(
        k8sHitCountBeanResponse.getRequestId(),
        k8sHitCountBeanResponse.getCode(),
        k8sHitCountBeanResponse.isSuccess(),
        count,
        k8sHitCountBeanResponse.getError());
  }

  @Override
  public Response<String> getProcessIdByKey(
      ChaosBladeProcessQueryRequest chaosBladeProcessQueryRequest) {
    return null;
  }

  @Override
  public Response<PrepareStatusBean> getPrepareStatus(
      ChaosBladeExpQueryRequest chaosBladeExpQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeExpQueryRequest.getHost());
    return chaosBladeForCloud.getPrepareStatusForCloud(
        chaosBladeExpQueryRequest.getExpId(),
        chaosBladeExpQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeExpQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<List<PrepareStatusBean>> getPrepareStatusList(
      ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladeHostQueryRequest.getHost());
    return chaosBladeForCloud.getPrepareStatusListForCloud(
        chaosBladeHostQueryRequest.getHost().getTargetIp(),
        String.valueOf(chaosBladeHostQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String[]> getDiskBlockDevice(
      ChaosBladeHostQueryRequest chaosBladeHostQueryRequest) {
    return null;
  }

  @Override
  public Response<String> destroyExpWithoutUid(
      ChaosBladeAction chaosBladeAction, ChaosAppRequest chaosAppRequest) {
    ModelArgs modelArgs = buildModelArgs(chaosAppRequest, chaosBladeAction);
    UserIdentification userIdentification =
        getUserIdentification((Host) chaosAppRequest.getScope());
    return chaosBladeForCloud.destroyExpForCloudWithoutUid(
        modelArgs,
        userIdentification.getAk(),
        userIdentification.getSk(),
        chaosAppRequest.getScope().getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String> destroyByChaosBladeExpDO(ChaosBladeExpUidDO chaosBladeExpUidDO) {
    DeviceDO deviceDO =
        deviceRepository.findByConfigurationId(chaosBladeExpUidDO.getDeviceConfigurationId());
    if (deviceDO == null) {
      throw new RuntimeException(
          "Not find device by configurationId:" + chaosBladeExpUidDO.getConfigurationId());
    }
    UserIdentification userIdentification = getUserIdentification(deviceDO);
    return destroyExp0(
        chaosBladeExpUidDO.getAppCode(),
        chaosBladeExpUidDO.getExpUid(),
        deviceDO.getPrivateIp(),
        String.valueOf(deviceDO.getPort()),
        userIdentification);
  }

  public Response<K8sResultBean> queryK8sExp(K8sStatusQueryRequest k8SStatusQueryRequest) {
    UserIdentification userIdentification = getUserIdentification(k8SStatusQueryRequest.getHost());
    String cmd = k8SStatusQueryRequest.isCreate() ? CliUtil.CREATE : CliUtil.DESTROY;
    return chaosBladeForCloud.queryK8sExpForCloud(
        k8SStatusQueryRequest.getExpId(),
        cmd,
        k8SStatusQueryRequest.getHost().getTargetIp(),
        String.valueOf(k8SStatusQueryRequest.getHost().getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String> checkForCloud(ChaosBladePreCheckRequest chaosBladePreCheckRequest) {
    UserIdentification userIdentification =
        getUserIdentification(chaosBladePreCheckRequest.getHost());
    return chaosBladeForCloud.checkForCloud(
        buildModelArgs(
            chaosBladePreCheckRequest.getChaosAppRequest(),
            chaosBladePreCheckRequest.getChaosBladeAction()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        chaosBladePreCheckRequest.getHost().getVpcId(),
        userIdentification.getCloudTag(),
        chaosBladePreCheckRequest.getEvnType());
  }

  @Override
  public Response<K8sResultBean> queryK8sExp(Host host, String expId, boolean isCreate) {
    UserIdentification userIdentification = getUserIdentification(host);
    String cmd = isCreate ? CliUtil.CREATE : CliUtil.DESTROY;
    return chaosBladeForCloud.queryK8sExpForCloud(
        expId,
        cmd,
        host.getTargetIp(),
        String.valueOf(host.getPort()),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String> uninstallAgent(Host host) {
    return chaosBladeForCloud.uninstallAgent(
        host.getTargetIp(), String.valueOf(host.getPort()), host.getUserId());
  }

  @Override
  public Response<String> pingAgent(Host host) {
    return chaosBladeForCloud.pingAgent(
        host.getTargetIp(), String.valueOf(host.getPort()), host.getUserId());
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
    return ProxyHelper.buildProxyTag(
        PluginTypeUtil.getPluginTypeByDeviceType(deviceType), ip, null);
  }

  private UserIdentification getUserIdentification(DeviceDO deviceDO) {
    UserIdentification userIdentification = new UserIdentification();
    userIdentification.setVpcId(deviceDO.getVpcId());
    userIdentification.setCloudTag(
        buildCloudTag(deviceDO.getDeviceType(), deviceDO.getPrivateIp()));
    userIdentification.setSk(deviceDO.getUserId());
    return userIdentification;
  }

  /**
   * Resolve what this server needs in order to talk to the agent running on {@code host}.
   *
   * <p>The agent authenticates every server-to-agent request with the credential this server handed
   * out when that agent registered, which is the device userId (see RegisteredRequestCommand, which
   * answers a registration with sk = request.getUserId()). So the device userId is the only value
   * that can serve as the signing key here.
   *
   * <p>ak is deliberately left unset: DeviceDO carries no license column, so this side has no copy
   * of the agent access key, and the agent skips its access key check when the header is absent.
   */
  private UserIdentification getUserIdentification(Host host) {
    UserIdentification userIdentification = new UserIdentification();
    userIdentification.setHost(host);
    if (host.getVpcId() != null) {
      userIdentification.setVpcId(host.getVpcId());
    }
    DeviceDO deviceDO = findDevice(host);
    if (host.getDeviceType() == null) {
      if (deviceDO != null) {
        userIdentification.setVpcId(deviceDO.getVpcId());
        userIdentification.setCloudTag(buildCloudTag(deviceDO.getDeviceType(), host.getTargetIp()));
      }
    } else {
      userIdentification.setCloudTag(buildCloudTag(host.getDeviceType(), host.getTargetIp()));
    }
    userIdentification.setSk(deviceDO != null ? deviceDO.getUserId() : host.getUserId());
    return userIdentification;
  }

  /**
   * Look up the device the agent on {@code host} registered as. Its userId is the credential this
   * server issued at registration, so it is the only value that can sign a request to that agent.
   *
   * <p>targetIp is tried against both the private and the public IP column because the app scope
   * resolver deliberately prefers the public IP when the device has one (see
   * ApplicationHostMiniAppInvokeInterceptor).
   */
  private DeviceDO findDevice(Host host) {
    DeviceDO deviceDO = null;
    if (host.getDeviceConfigurationId() != null) {
      deviceDO = deviceRepository.findByConfigurationId(host.getDeviceConfigurationId());
    }
    if (deviceDO == null && host.getTargetIp() != null) {
      deviceDO = deviceRepository.findByPrivateIp(host.getTargetIp());
    }
    if (deviceDO == null && host.getTargetIp() != null) {
      deviceDO = deviceRepository.findByPublicIp(host.getTargetIp());
    }
    return deviceDO;
  }

  private void fillModelArgs(
      ChaosAppRequest chaosAppRequest, ModelArgs modelArgs, ChaosBladeAction chaosBladeAction) {
    String scope = getScope(chaosBladeAction.getTarget());
    String target = getWinTarget(chaosBladeAction.getTarget());
    String port = String.valueOf(chaosAppRequest.getScope().getPort());
    if (SCOPE_DOCKER.equals(scope)) {
      modelArgs
          .setTarget(scope)
          .setAction(chaosBladeAction.getAction())
          .setFlags(chaosAppRequest.getAction())
          .setSubTarget(chaosBladeAction.getSubTarget())
          .setMatcherFlags(chaosAppRequest.getMatcher())
          .setPort(port)
          .setMachine(chaosAppRequest.getScope().getTargetIp());
    } else if (SCOPE_WINDOWS.equals(scope)) {
      modelArgs
          .setTarget(target)
          .setAction(chaosBladeAction.getAction())
          .setFlags(chaosAppRequest.getAction())
          .setMatcherFlags(chaosAppRequest.getMatcher())
          .setScope(getScope(chaosBladeAction.getTarget()))
          .setPort(port)
          .setMachine(chaosAppRequest.getScope().getTargetIp());
    } else {
      modelArgs
          .setTarget(chaosBladeAction.getTarget())
          .setAction(chaosBladeAction.getAction())
          .setFlags(chaosAppRequest.getAction())
          .setMatcherFlags(chaosAppRequest.getMatcher())
          .setScope(getScope(chaosBladeAction.getTarget()))
          .setPort(port)
          .setMachine(chaosAppRequest.getScope().getTargetIp());
    }
  }
}
