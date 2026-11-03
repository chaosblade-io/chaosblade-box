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
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.LitmusChaosForCloud;
import com.alibaba.chaosblade.box.common.sdk.entity.ChaosModels;
import com.alibaba.chaosblade.box.common.sdk.entity.ModelArgs;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.LitmusChaosInvoker;
import com.alibaba.chaosblade.box.dao.infrastructure.app.litmuschaos.request.LitmusChaosRequest;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.PluginTypeUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.ProxyHelper;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/** @author yefei */
@ChaosComponent
public class PublicLitmusChaosInvoker implements LitmusChaosInvoker {

  @Autowired private LitmusChaosForCloud litmusChaosForCloud;

  @Autowired private DeviceRepository deviceRepository;

  @Override
  public ChaosModels getBladeModels() throws Exception {
    return litmusChaosForCloud.getChaosModels();
  }

  @Override
  public Response<String> createExp(LitmusChaosRequest litmusChaosRequest) {

    MiniAppInvokeContext appInvokeContext = litmusChaosRequest.getAppInvokeContext();
    ModelArgs modelArgs =
        buildModelArgs(
            appInvokeContext.getChaosAppRequest(), litmusChaosRequest.getChaosBladeAction());
    PublicCloudChaosBladeInvoker.UserIdentification userIdentification =
        getUserIdentification(appInvokeContext.getHost());

    return litmusChaosForCloud.createExpForCloud(
        modelArgs,
        "default",
        "generic",
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  @Override
  public Response<String> destroyExp(LitmusChaosRequest litmusChaosRequest, String expId) {

    MiniAppInvokeContext appInvokeContext = litmusChaosRequest.getAppInvokeContext();
    ModelArgs modelArgs =
        buildModelArgs(
            appInvokeContext.getChaosAppRequest(), litmusChaosRequest.getChaosBladeAction());
    PublicCloudChaosBladeInvoker.UserIdentification userIdentification =
        getUserIdentification(appInvokeContext.getHost());
    return litmusChaosForCloud.destroyExpForCloud(
        modelArgs.getMachine(),
        modelArgs.getMachineType(),
        "default",
        expId,
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
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
    PublicCloudChaosBladeInvoker.UserIdentification userIdentification =
        getUserIdentification(deviceDO);
    return litmusChaosForCloud.destroyExpForCloud(
        chaosBladeExpUidDO.getTargetIp(),
        null,
        "default",
        chaosBladeExpUidDO.getExpUid(),
        userIdentification.getAk(),
        userIdentification.getSk(),
        userIdentification.getVpcId(),
        userIdentification.getCloudTag());
  }

  private PublicCloudChaosBladeInvoker.UserIdentification getUserIdentification(DeviceDO deviceDO) {
    PublicCloudChaosBladeInvoker.UserIdentification userIdentification =
        new PublicCloudChaosBladeInvoker.UserIdentification();
    userIdentification.setVpcId(deviceDO.getVpcId());
    userIdentification.setCloudTag(
        buildCloudTag(deviceDO.getDeviceType(), deviceDO.getPrivateIp()));
    userIdentification.setSk(deviceDO.getUserId());
    return userIdentification;
  }

  /**
   * See PublicCloudChaosBladeInvoker#getUserIdentification(Host): the agent authenticates with the
   * credential issued to it at registration, which is the device userId, so that is the only value
   * usable as the signing key. ak stays unset because DeviceDO carries no license column and the
   * agent skips its access key check when the header is absent.
   */
  private PublicCloudChaosBladeInvoker.UserIdentification getUserIdentification(Host host) {
    PublicCloudChaosBladeInvoker.UserIdentification userIdentification =
        new PublicCloudChaosBladeInvoker.UserIdentification();
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

  private String buildCloudTag(Integer deviceType, String ip) {
    return ProxyHelper.buildProxyTag(
        PluginTypeUtil.getPluginTypeByDeviceType(deviceType), ip, null);
  }

  public ModelArgs buildModelArgs(
      ChaosAppRequest chaosAppRequest, ChaosBladeAction chaosBladeAction) {
    ModelArgs modelArgs = new ModelArgs();
    fillModelArgs(chaosAppRequest, modelArgs, chaosBladeAction);
    return modelArgs;
  }

  private void fillModelArgs(
      ChaosAppRequest chaosAppRequest, ModelArgs modelArgs, ChaosBladeAction chaosBladeAction) {
    Map<String, String> action = chaosAppRequest.getAction();
    Map<String, String> matcher = new HashMap<>();
    if (action != null) {
      List<String> keys =
          action.keySet().stream()
              .filter(key -> "appns".equals(key) || "applabel".equals(key) || "appkind".equals(key))
              .collect(Collectors.toList());
      keys.forEach(
          key -> {
            matcher.put(key, action.get(key));
            action.remove(key);
          });
    }

    modelArgs
        .setTarget(getTarget(chaosBladeAction.getTarget()))
        .setAction(chaosBladeAction.getAction())
        .setFlags(chaosAppRequest.getAction())
        .setMatcherFlags(matcher)
        .setScope(getScope(chaosBladeAction.getTarget()))
        .setMachine(chaosAppRequest.getScope().getTargetIp());
  }

  public String getTarget(String target) {
    String[] splits = target.split("-");
    if (splits.length == 2) {
      return splits[1];
    }
    return target;
  }

  public String getScope(String target) {
    String[] splits = target.split("-");
    if (splits.length >= 2) {
      return splits[0];
    }
    return null;
  }
}
