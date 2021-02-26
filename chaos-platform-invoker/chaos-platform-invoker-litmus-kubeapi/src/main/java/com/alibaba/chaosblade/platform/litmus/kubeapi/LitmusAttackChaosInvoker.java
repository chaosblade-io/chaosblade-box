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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.constants.ChaosConstant;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.SceneCodeParseUtil;
import com.alibaba.chaosblade.platform.invoker.ChaosInvokerStrategy;
import com.alibaba.chaosblade.platform.cmmon.enums.ChaosTools;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import com.alibaba.chaosblade.platform.litmus.kubeapi.crd.engine.*;
import com.alibaba.chaosblade.platform.scenario.litmus.model.ChaosExperiment;
import com.alibaba.chaosblade.platform.scenario.litmus.model.experiments.ChaosExperimentDefinitionEnv;
import io.kubernetes.client.openapi.ApiCallback;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.CustomObjectsApi;
import io.kubernetes.client.openapi.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.openapi.models.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@ChaosInvokerStrategy(value = ChaosTools.LITMUS_CHAOS,
        deviceType = {
                DeviceType.NODE, DeviceType.POD
        },
        phase = ChaosConstant.PHASE_ATTACK)
@Component
public class LitmusAttackChaosInvoker extends AbstractLitmusChaosInvoker {

    private String preExperiment(RequestCommand requestCommand) {
        CompletableFuture<ResponseCommand> future = new CompletableFuture<>();
        String serviceAccount = requestCommand.getSceneCode() + SA_SUFFIX;
        ChaosExperiment chaosExperiment = experimentMap.get(requestCommand.getSceneCode());

        try {
            CustomObjectsApi customObjectsApi = new CustomObjectsApi(client);
            customObjectsApi.createNamespacedCustomObject(
                    Constants.GROUP,
                    Constants.VERSION,
                    requestCommand.getNamespace(),
                    Constants.EXPERIMENT_PLURAL,
                    JsonUtils.writeValueAsBytes(chaosExperiment),
                    "true",
                    null,
                    null
            );
        } catch (ApiException e) {
            if (e.getCode() == 409) {
                // ignore
            } else {
                future.complete(ResponseCommand.builder()
                        .code(String.valueOf(e.getCode()))
                        .error(e.getMessage())
                        .result(e.getResponseBody())
                        .success(false).build());
            }
        }
        try {
            CoreV1Api apiInstance = new CoreV1Api(client);
            // todo
            V1ServiceAccount v1ServiceAccount = new V1ServiceAccount();
            v1ServiceAccount.metadata(new V1ObjectMeta().name(serviceAccount).namespace(requestCommand.getNamespace()));
            apiInstance.createNamespacedServiceAccount(
                    requestCommand.getNamespace(),
                    v1ServiceAccount,
                    "true",
                    null,
                    null
            );

        } catch (ApiException e) {
            if (e.getCode() == 409) {
                // ignore
            } else {
                future.complete(ResponseCommand.builder()
                        .code(String.valueOf(e.getCode()))
                        .error(e.getMessage())
                        .result(e.getResponseBody())
                        .success(false).build());
            }
        }
        RbacAuthorizationV1Api rbacAuthorizationV1Api = new RbacAuthorizationV1Api(client);
        try {
            // role
            V1ClusterRole v1ClusterRole = new V1ClusterRole();

            v1ClusterRole.metadata(new V1ObjectMeta()
                    .name(serviceAccount)
                    .namespace(requestCommand.getNamespace())
            );
            List<V1PolicyRule> policyRules = Arrays.stream(chaosExperiment.getSpec().getDefinition().getPermissions())
                    .map(permission ->
                            new V1PolicyRule()
                                    .apiGroups(Arrays.asList(permission.getApiGroups()))
                                    .resources(Arrays.asList(permission.getResources()))
                                    .verbs(Arrays.asList(permission.getVerbs()))
                    ).collect(Collectors.toList());

            v1ClusterRole.setRules(policyRules);
            rbacAuthorizationV1Api.createClusterRole(v1ClusterRole, "true", null, null);
        } catch (ApiException e) {
            if (e.getCode() == 409) {
                // ignore
            } else {
                future.complete(ResponseCommand.builder()
                        .code(String.valueOf(e.getCode()))
                        .error(e.getMessage())
                        .result(e.getResponseBody())
                        .success(false).build());
            }
        }
        try {
            // role binding
            V1ClusterRoleBinding v1RoleBinding = new V1ClusterRoleBinding();
            v1RoleBinding.metadata(new V1ObjectMeta()
                    .namespace(requestCommand.getNamespace())
                    .name(serviceAccount))
                    .roleRef(new V1RoleRef().apiGroup("rbac.authorization.k8s.io").kind("ClusterRole").name(serviceAccount))
                    .setSubjects(CollUtil.newArrayList(new V1Subject()
                            .kind("ServiceAccount").name(serviceAccount).namespace(requestCommand.getNamespace())));

            rbacAuthorizationV1Api.createClusterRoleBinding(v1RoleBinding, "true", null, null);
        } catch (ApiException e) {
            if (e.getCode() == 409) {
                // ignore
            } else {
                future.complete(ResponseCommand.builder()
                        .code(String.valueOf(e.getCode()))
                        .error(e.getMessage())
                        .result(e.getResponseBody())
                        .success(false).build());
            }
        }
        return serviceAccount;
    }

    @Override
    public CompletableFuture<ResponseCommand> invoke(RequestCommand requestCommand) {
        if (StrUtil.isBlank(requestCommand.getNamespace())) {
            requestCommand.setNamespace("default");
        }

        CompletableFuture<ResponseCommand> completableFuture = new CompletableFuture<>();

        String serviceAccount = preExperiment(requestCommand);
        CustomObjectsApi apiInstance = new CustomObjectsApi(client);

        V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
        v1ObjectMeta.setName(IdUtil.fastSimpleUUID());

        String experimentName;
        String target = SceneCodeParseUtil.getTarget(requestCommand.getSceneCode());
        String action = SceneCodeParseUtil.getAction(requestCommand.getSceneCode());
        String[] split = StrUtil.split(target, "-");
        if (split[0].equals(split[1])) {
            experimentName = split[0] + "-" + action;
        } else {
            experimentName = target + "-" + action;
        }

        ChaosEngine chaosEngine = ChaosEngine.builder()
                .apiVersion(Constants.API_VERSION)
                .kind(Constants.CHAOS_ENGINE)
                .metadata(v1ObjectMeta)
                .spec(ChaosEngineSpec.builder()
                        .chaosServiceAccount(serviceAccount)
                        .engineState("active")
                        .monitoring(false)
                        .annotationCheck("false")
                        .jobCleanUpPolicy("delete")
                        .experiments(new ChaosEngineSpecExperiment[]{
                                ChaosEngineSpecExperiment.builder()
                                        .name(experimentName)
                                        .spec(ChaosEngineSpecExperimentSpec.builder()
                                                .components(ChaosEngineSpecExperimentSpecComponents.builder()
                                                        .env(requestCommand.getArguments().keySet().stream()
                                                                .map(key -> ChaosExperimentDefinitionEnv.builder()
                                                                        .name(key)
                                                                        .value(requestCommand.getArguments().get(key))
                                                                        .build()
                                                                ).toArray(ChaosExperimentDefinitionEnv[]::new))
                                                        .build()
                                                )
                                                .build())
                                        .build()})
                        .build()).build();

        try {
            apiInstance.createNamespacedCustomObjectAsync(
                    Constants.GROUP,
                    Constants.VERSION,
                    requestCommand.getNamespace(),
                    Constants.ENGINE_PLURAL,
                    chaosEngine,
                    "ture",
                    null,
                    null,
                    new ApiCallback() {
                        @Override
                        public void onFailure(ApiException e, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand = ResponseCommand.builder()
                                    .success(false)
                                    .code(String.valueOf(statusCode))
                                    .result(e.getMessage())
                                    .error(e.getResponseBody())
                                    .build();
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onSuccess(Object result, int statusCode, Map responseHeaders) {
                            ResponseCommand responseCommand = ResponseCommand.builder()
                                    .code(String.valueOf(statusCode))
                                    .success(true)
                                    .result(v1ObjectMeta.getName())
                                    .build();
                            completableFuture.complete(responseCommand);
                        }

                        @Override
                        public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

                        }

                        @Override
                        public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

                        }
                    }
            );
        } catch (ApiException apiException) {
            ResponseCommand responseCommand = ResponseCommand.builder()
                    .success(false)
                    .code(String.valueOf(apiException.getCode()))
                    .result(apiException.getMessage())
                    .error(apiException.getResponseBody())
                    .build();
            completableFuture.complete(responseCommand);
        }

        return completableFuture;
    }

}
