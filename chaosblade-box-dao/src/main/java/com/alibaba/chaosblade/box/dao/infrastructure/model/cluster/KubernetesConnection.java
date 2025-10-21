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

package com.alibaba.chaosblade.box.dao.infrastructure.model.cluster;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.apis.ExtensionsV1beta1Api;

/** @author haibin.lhb */
public class KubernetesConnection {

  private KubernetesCluster cluster;

  private Long lastTestTime = null;

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  private boolean isActive = false;

  public KubernetesConnection(KubernetesCluster cluster) {
    this.cluster = cluster;
  }

  public String getConnectionBasePath() {
    return connectionBasePath;
  }

  public void setConnectionBasePath(String connectionBasePath) {
    this.connectionBasePath = connectionBasePath;
  }

  private String connectionBasePath;

  public CoreV1Api getCoreV1Api() {
    return coreV1Api;
  }

  public void setCoreV1Api(CoreV1Api coreV1Api) {
    this.coreV1Api = coreV1Api;
    this.isActive = true;
    this.lastTestTime = System.currentTimeMillis();
  }

  /** 核心API,包含了Pod,Service,Node */
  private CoreV1Api coreV1Api;

  public CustomObjectsApi getCustomObjectsApi() {
    return customObjectsApi;
  }

  public void setCustomObjectsApi(CustomObjectsApi customObjectsApi) {
    this.customObjectsApi = customObjectsApi;
  }

  /** 用来处理CRD资源增删改查 */
  private CustomObjectsApi customObjectsApi;

  public ExtensionsV1beta1Api getExtensionsV1beta1Api() {
    return extensionsV1beta1Api;
  }

  public void setExtensionsV1beta1Api(ExtensionsV1beta1Api extensionsV1beta1Api) {
    this.extensionsV1beta1Api = extensionsV1beta1Api;
  }

  private ExtensionsV1beta1Api extensionsV1beta1Api;

  public void disConnect() {}

  public AppsV1Api getAppsV1Api() {
    return appsV1Api;
  }

  public void setAppsV1Api(AppsV1Api appsV1Api) {
    this.appsV1Api = appsV1Api;
  }

  private AppsV1Api appsV1Api;

  public boolean testActive() {
    if (lastTestTime == null) {
      lastTestTime = System.currentTimeMillis();
    }
    if (isActive) {
      if (System.currentTimeMillis() - lastTestTime > 3 * 1000 * 60) {
        try {
          coreV1Api.listNamespace(null, null, null, null, null, 1, null, null, null, null);
          this.isActive = true;
          return true;
        } catch (ApiException e) {
          return false;
        }
      }
      return true;
    }
    lastTestTime = System.currentTimeMillis();
    return false;
  }
}
