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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.invoker;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.common.annotation.KubernetesExtensionPoint;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.OnceInvoke;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.common.sdk.entity.K8sResultBean;
import com.alibaba.chaosblade.box.dao.infrastructure.app.ChaosAppResponseReference;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.MiniAppInvokeContext;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.interceptor.BaseMiniAppInvokeInterceptor;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

/** @author haibin */
@KubernetesExtensionPoint
@Slf4j
public class K8sMiniAppInvokeInterceptor extends BaseMiniAppInvokeInterceptor {

  @Override
  public void postHandle(
      OnceInvoke<MiniAppInvokeContext, ChaosAppResponseReference> onceInvoke,
      MiniAppInvokeContext miniAppInvokeContext,
      ChaosAppResponseReference chaosAppResponseReference) {
    if (isNotK8sScene(miniAppInvokeContext)) {
      return;
    }
    ChaosAppResponse mkAppResponse = chaosAppResponseReference.getChaosAppResponse();
    if (mkAppResponse instanceof ChaosBladeAppResponse) {
      ChaosBladeAppResponse chaosBladeMkAppResponse = (ChaosBladeAppResponse) mkAppResponse;
      if (chaosBladeMkAppResponse.getChaosBladeResponse() == null) {
        return;
      }
      String result = (String) chaosBladeMkAppResponse.getChaosBladeResponse().getResult();
      if (Strings.isNullOrEmpty(result)) {
        return;
      }
      K8sResultBean k8sResultBean =
          JSON.parseObject(
              (String) chaosBladeMkAppResponse.getChaosBladeResponse().getResult(),
              K8sResultBean.class);
      if (k8sResultBean == null) {
        return;
      }
      chaosBladeMkAppResponse.setChaosBladeExpId(k8sResultBean.getUid());
      chaosBladeMkAppResponse.addResponseData("response", k8sResultBean);
    }
  }

  private boolean isNotK8sScene(MiniAppInvokeContext miniAppInvokeContext) {
    String appCode = miniAppInvokeContext.getActivityInvokeContext().getAppCode();
    return !PublicCloudUtil.isK8SByAppCode(appCode) || !appCode.startsWith("chaos.");
  }

  @Override
  public Integer getOrder() {
    return Integer.MIN_VALUE + 2;
  }
}
