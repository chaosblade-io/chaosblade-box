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

package com.alibaba.chaosblade.box.common.sdk;

import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.sdk.channel.PaasTransportService;
import com.alibaba.chaosblade.box.common.sdk.constant.Blade;
import com.alibaba.chaosblade.box.common.sdk.entity.ModelArgs;
import com.alibaba.chaosblade.box.common.sdk.transport.Request;
import com.alibaba.chaosblade.box.common.sdk.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LitmusChaosForCloud extends ChaosBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChaosBladeForCloud.class);
  private PaasTransportService transportService;

  public LitmusChaosForCloud(PaasTransportService transportService) {
    this.transportService = transportService;
  }

  public PaasTransportService getTransportService() {
    return transportService;
  }

  @Override
  public String getYamlFileName() {
    return "litmuschaos.spec-cloud.yaml";
  }

  /**
   * 创建k8s混沌实验
   *
   * @param modelArgs
   * @param vpcId
   * @param tag
   * @return
   */
  public Response<String> createExpForCloud(
      ModelArgs modelArgs,
      String namespace,
      String experimentType,
      String ak,
      String sk,
      String vpcId,
      String tag) {
    Request request =
        RequestUtil.createLitmusRequest(Blade.CREATE, modelArgs, namespace, experimentType, ak, sk);
    return transportService.invoke(request, String.class);
  }

  public Response<String> destroyExpForCloud(
      String machine,
      String machineType,
      String namespace,
      String name,
      String ak,
      String sk,
      String vpcId,
      String tag) {
    Request request =
        RequestUtil.createLitmusRequest(
            Blade.DESTROY, machine, machineType, namespace, name, ak, sk);
    return transportService.invoke(request, String.class);
  }

  public Response<String> installLitmusOperatorForCloud(
      String machine,
      String machineType,
      String ak,
      String sk,
      String vpcId,
      String tag,
      String version) {
    Request request = RequestUtil.createLitmusRequest(machine, machineType, ak, sk, version);
    return transportService.invoke(request, String.class);
  }

  public Response<String> uninstallLitmusOperatorForCloud(
      String machine, String machineType, String ak, String sk, String vpcId, String tag) {
    Request request = RequestUtil.createLitmusRequest(machine, machineType, ak, sk);
    return transportService.invoke(request, String.class);
  }
}
