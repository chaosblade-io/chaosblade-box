/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosblade.platform.litmus.kubeapi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.apis.RbacAuthorizationV1Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author yefei
 */
@Slf4j
@ChaosInvokerStrategy(value = ChaosTools.LITMUS_CHAOS,
        deviceType = {
                DeviceType.NODE, DeviceType.POD
        },
        phase = ChaosConstant.PHASE_RECOVER)
@Component
public class LitmusRecoverChaosInvoker extends AbstractLitmusChaosInvoker {

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        if (StrUtil.isBlank(requestCommand.getNamespace())) {
            requestCommand.setNamespace("default");
        }

        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();
        try {
            apiInstance.deleteNamespacedCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    requestCommand.getNamespace(),
                    Constants.ENGINE_PLURAL,
                    requestCommand.getName(),
                    10,
                    false,
                    null,
                    null,
                    null,
                    new ApiCallback() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand;
                            if (statusCode == 404) {
                                responseCommand = ResponseCommand.builder().success(true)
                                        .result(requestCommand.getName()).build();
                                clean(requestCommand);
                            } else {
                                responseCommand = ResponseCommand.builder()
                                        .success(false)
                                        .code(String.valueOf(statusCode))
                                        .result(e.getMessage())
                                        .error(e.getResponseBody())
                                        .build();
                            }
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand = ResponseCommand.builder().success(true)
                                    .code(String.valueOf(statusCode))
                                    .result(requestCommand.getName()).build();
                            clean(requestCommand);
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
                            log.warn("onUploadProgress");
                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
                            log.warn("onDownloadProgress");
                        }
                    }
            );
        } catch (ApiException e) {
            ResponseCommand responseCommand = ResponseCommand.builder()
                    .success(false)
                    .code(String.valueOf(e.getCode()))
                    .result(e.getMessage())
                    .error(e.getResponseBody())
                    .build();
            completableFuture.complete(responseCommand);
        }
        return completableFuture;
    }

    private void clean(RequestCommand requestCommand) {
        String serviceAccount = requestCommand.getSceneCode().replace(ChaosConstant.CHAOS_DESTROY_SUFFIX, "") + SA_SUFFIX;

        String experimentName;
        String target = SceneCodeParseUtil.getTarget(requestCommand.getSceneCode());
        String action = SceneCodeParseUtil.getAction(requestCommand.getSceneCode());
        String[] split = StrUtil.split(target, "-");
        if (split[0].equals(split[1])) {
            experimentName = split[0] + "-" + action;
        } else {
            experimentName = target + "-" + action;
        }

        try {
            // delete experiment
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            customObjectsApi.deleteNamespacedCustomObject(
                    Constants.GROUP,
                    Constants.VERSION,
                    requestCommand.getNamespace(),
                    Constants.EXPERIMENT_PLURAL,
                    experimentName,
                    10,
                    null,
                    null,
                    null,
                    null);

        } catch (ApiException e) {
            log.warn("delete experiments:[{}] fail", requestCommand.getSceneCode(), e);
        }
        try {
            // delete sa
            CoreV1Api apiInstance = new CoreV1Api(client);
            apiInstance.deleteNamespacedServiceAccount(
                    serviceAccount,
                    requestCommand.getNamespace(),
                    "true",
                    null,
                    10,
                    null,
                    null,
                    null
            );
        } catch (ApiException e) {
            log.warn("delete service account:[{}] fail", serviceAccount, e);
        }
        RbacAuthorizationV1Api rbacAuthorizationV1Api = new RbacAuthorizationV1Api(client);
        try {
            // delete role
            rbacAuthorizationV1Api.deleteClusterRole(serviceAccount, "true",
                    null, 10,
                    null, null, null);
        } catch (ApiException e) {
            log.warn("delete role:[{}] fail", serviceAccount, e);
        }
        try {
            // delete role binding
            rbacAuthorizationV1Api.deleteClusterRoleBinding(serviceAccount, "true",
                    null, 10, null, null, null);
        } catch (ApiException e) {
            log.warn("delete role binding :[{}] fail", serviceAccount, e);
        }
    }

}
