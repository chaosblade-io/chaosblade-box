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

package com.alibaba.chaosblade.box.dao.infrastructure.strategy;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ActivityTaskExecutionAttributes;
import com.alibaba.chaosblade.box.common.experiment.task.flow.ChaosBladeAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ExceptionHelper;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.InvokeTracer;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.concurrent.TraceCallable;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.common.infrastructure.util.PublicCloudUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ExecutorAction;
import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ExecutorUtil;
import com.alibaba.chaosblade.box.common.sdk.entity.K8sResultBean;
import com.alibaba.chaosblade.box.dao.infrastructure.app.MiniAppInvokerFactory;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.task.flow.step.ActivityInvokeContext;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.query.ActivityTargetExecutionResultQuery;
import com.alibaba.chaosblade.box.dao.repository.ActivityTargetExecutionResultRepository;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 并发跑
 *
 * @author haibin
 */
@Component
@Slf4j
public class BatchActivityTargetRunnableStrategy extends ActivityTargetsRunnableStrategy {

  @Autowired private InvokeTracer invokeTracer;

  @Autowired
  private ActivityTargetExecutionResultRepository activityTargetExecutionResultRepository;

  @Override
  protected List<ChaosAppResponse> internalRun(
      ActivityInvokeContext activityInvokeContext, List<Host> invokeHosts) {
    // 对于 K8s 场景，命令中已经包含了所有目标 pod 名称（通过 --names 参数）
    // 因此只需要调用一次，不需要为每个 host 都执行
    if (isK8sScenario(activityInvokeContext) && !invokeHosts.isEmpty()) {
      log.info(
          "[BatchActivityTargetRunnableStrategy] K8s scenario detected, using single invocation for {} hosts",
          invokeHosts.size());
      // 只使用第一个 host 调用一次，命令中已包含所有 pod
      ChaosAppResponse response =
          MiniAppInvokerFactory.createInvoker(activityInvokeContext, invokeHosts.get(0)).invoke();

      // 解析K8s返回结果，将statuses拆分为每个pod的独立响应
      return splitK8sResponse(response, invokeHosts, activityInvokeContext);
    }

    // 非 K8s 场景，正常并发执行
    int batchSize = calcBatchSize(activityInvokeContext);
    return ExecutorUtil.execute(
        new ExecutorAction<ChaosAppResponse>() {
          @Override
          public List<Callable<ChaosAppResponse>> getCallables() {
            return invokeHosts.stream()
                .map(
                    (Function<Host, Callable<ChaosAppResponse>>)
                        host ->
                            new TraceCallable<>(
                                invokeTracer,
                                new ActivityTargetCallable(host, activityInvokeContext)))
                .collect(Collectors.toList());
          }

          public ChaosAppResponse errorHandle(Callable<ChaosAppResponse> callable, Throwable e) {
            TraceCallable<ChaosAppResponse> traceCallable =
                (TraceCallable<ChaosAppResponse>) callable;
            ActivityTargetCallable activityTargetCallable =
                (ActivityTargetCallable) traceCallable.getCallable();
            ChaosAppResponse chaosAppResponse = new ChaosAppResponse(false);
            chaosAppResponse.setScope(activityTargetCallable.host);
            chaosAppResponse.setErrorMessage(ExceptionHelper.detailedMessage(e));
            return chaosAppResponse;
          }
        },
        batchSize);
  }

  @Override
  public boolean support(ActivityInvokeContext activityInvokeContext) {
    return ActivityTaskExecutionAttributes.ATTRIBUTE_VALUE_ACTIVITY_RUNNABLE_STRATEGY_BATCH.equals(
            activityInvokeContext
                .getContextData()
                .getAsString(
                    ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_STRATEGY))
        && activityInvokeContext
            .getContextData()
            .containsKey(
                ActivityTaskExecutionAttributes.ATTRIBUTE_KEY_ACTIVITY_RUNNABLE_BATCH_SIZE);
  }

  protected int calcBatchSize(ActivityInvokeContext activityInvokeContext) {
    return activityInvokeContext.getContextData().getMiniAppBatchCount();
  }

  /**
   * 判断是否为 K8s 场景
   *
   * @param activityInvokeContext 活动调用上下文
   * @return true 表示是 K8s 场景
   */
  private boolean isK8sScenario(ActivityInvokeContext activityInvokeContext) {
    String appCode = activityInvokeContext.getExecutableAppCode();
    return PublicCloudUtil.isK8SByAppCode(appCode);
  }

  /**
   * 将K8s返回结果中的statuses拆分，为每个pod创建独立的响应 从identifier中解析pod名称:
   * cms-demo/cn-hongkong.10.0.2.27/product-catalog-59758b6f5d-d84mp/...
   *
   * @param response 原始响应
   * @param invokeHosts 调用的host列表
   * @param activityInvokeContext 活动调用上下文
   * @return 每个host对应的响应列表
   */
  private List<ChaosAppResponse> splitK8sResponse(
      ChaosAppResponse response,
      List<Host> invokeHosts,
      ActivityInvokeContext activityInvokeContext) {
    if (!(response instanceof ChaosBladeAppResponse)) {
      // 如果不是ChaosBladeAppResponse,返回单个响应给第一个host
      log.warn(
          "[BatchActivityTargetRunnableStrategy] Response is not ChaosBladeAppResponse, cannot split");
      return Collections.singletonList(response);
    }

    ChaosBladeAppResponse chaosBladeResponse = (ChaosBladeAppResponse) response;
    Object chaosBladeResult = chaosBladeResponse.getChaosBladeResponse();

    if (chaosBladeResult == null) {
      log.warn("[BatchActivityTargetRunnableStrategy] ChaosBladeResponse is null, cannot split");
      return Collections.singletonList(response);
    }

    try {
      // 尝试从response中提取K8sResultBean
      K8sResultBean k8sResult = extractK8sResultBean(chaosBladeResult);

      if (k8sResult == null
          || k8sResult.getStatuses() == null
          || k8sResult.getStatuses().isEmpty()) {
        log.warn(
            "[BatchActivityTargetRunnableStrategy] No statuses found in K8s response, cannot split");
        return Collections.singletonList(response);
      }

      // 构建pod名称到status的映射
      Map<String, K8sResultBean.K8sExpStatusBean> podNameToStatus = new HashMap<>();
      for (K8sResultBean.K8sExpStatusBean status : k8sResult.getStatuses()) {
        String podName = extractPodNameFromIdentifier(status.getIdentifier());
        if (podName != null) {
          podNameToStatus.put(podName, status);
          log.info(
              "[BatchActivityTargetRunnableStrategy] Extracted pod name from identifier: podName={}, identifier={}, expId={}",
              podName,
              status.getIdentifier(),
              status.getId());
        }
      }

      log.info(
          "[BatchActivityTargetRunnableStrategy] Total {} statuses extracted, mapping: {}",
          podNameToStatus.size(),
          podNameToStatus.keySet());

      // 为每个host创建独立的响应并更新数据库
      List<ChaosAppResponse> responses = new ArrayList<>();
      String activityTaskId = activityInvokeContext.getActivityTask().getTaskId();
      String appCode = activityInvokeContext.getActivityTask().getAppCode();

      for (Host host : invokeHosts) {
        ChaosBladeAppResponse hostResponse = new ChaosBladeAppResponse();
        hostResponse.setScope(host);
        hostResponse.setChaosBladeExpId(chaosBladeResponse.getChaosBladeExpId());

        // 从host的deviceName获取pod名称
        String podName = host.getDeviceName();
        log.info(
            "[BatchActivityTargetRunnableStrategy] Processing host: deviceName={}, ip={}, deviceId={}",
            podName,
            host.getIp(),
            host.getDeviceId());

        K8sResultBean.K8sExpStatusBean podStatus = podNameToStatus.get(podName);

        if (podStatus != null) {
          // 找到对应pod的状态
          hostResponse.setSuccess(podStatus.isSuccess());
          hostResponse.setErrorMessage(podStatus.getError());
          hostResponse.addResponseData("podStatus", podStatus);
          hostResponse.addResponseData("expId", podStatus.getId());

          // 为当前pod创建独立的K8sResultBean，只包含当前pod的status
          K8sResultBean individualK8sResult = new K8sResultBean();
          individualK8sResult.setUid(k8sResult.getUid());
          individualK8sResult.setSuccess(podStatus.isSuccess());
          individualK8sResult.setError(podStatus.getError());
          List<K8sResultBean.K8sExpStatusBean> individualStatuses = new ArrayList<>();
          individualStatuses.add(podStatus);
          individualK8sResult.setStatuses(individualStatuses);

          // 创建只包含当前pod的Response
          com.alibaba.chaosblade.box.common.common.domain.response.Response<K8sResultBean>
              individualResponse =
                  new com.alibaba.chaosblade.box.common.common.domain.response.Response<>();
          individualResponse.setCode(200);
          individualResponse.setSuccess(podStatus.isSuccess());
          individualResponse.setError(podStatus.getError());
          individualResponse.setResult(individualK8sResult);

          hostResponse.setChaosBladeResponse(individualResponse);

          log.info(
              "[BatchActivityTargetRunnableStrategy] Mapped pod {} to status: success={}, expId={}",
              podName,
              podStatus.isSuccess(),
              podStatus.getId());

          // 更新数据库记录
          updateMiniAppTaskResult(activityTaskId, host.getIp(), appCode, hostResponse);
        } else {
          // 未找到对应pod的状态，使用整体结果
          hostResponse.setSuccess(k8sResult.isSuccess());
          hostResponse.setErrorMessage(k8sResult.getError());
          hostResponse.setChaosBladeResponse(chaosBladeResponse.getChaosBladeResponse());
          log.warn(
              "[BatchActivityTargetRunnableStrategy] No status found for pod {}, using overall result",
              podName);

          // 更新数据库记录
          updateMiniAppTaskResult(activityTaskId, host.getIp(), appCode, hostResponse);
        }

        responses.add(hostResponse);
      }

      log.info(
          "[BatchActivityTargetRunnableStrategy] Split K8s response into {} individual responses",
          responses.size());
      return responses;

    } catch (Exception e) {
      log.error("[BatchActivityTargetRunnableStrategy] Failed to split K8s response", e);
      return Collections.singletonList(response);
    }
  }

  /** 从不同类型的对象中提取K8sResultBean */
  private K8sResultBean extractK8sResultBean(Object obj) {
    if (obj instanceof K8sResultBean) {
      return (K8sResultBean) obj;
    }

    // 处理 Response<K8sResultBean> 类型（KubernetesChaosBladeMiniAppInterceptor 设置的）
    if (obj instanceof com.alibaba.chaosblade.box.common.common.domain.response.Response) {
      try {
        com.alibaba.chaosblade.box.common.common.domain.response.Response<?> response =
            (com.alibaba.chaosblade.box.common.common.domain.response.Response<?>) obj;
        Object result = response.getResult();
        if (result instanceof K8sResultBean) {
          return (K8sResultBean) result;
        }
        // 如果 result 是 Map 或 String，继续解析
        if (result != null) {
          return extractK8sResultBean(result);
        }
      } catch (Exception e) {
        log.debug(
            "[BatchActivityTargetRunnableStrategy] Failed to extract from Response wrapper", e);
      }
    }

    // 尝试从JSON字符串解析
    if (obj instanceof String) {
      try {
        return JSON.parseObject((String) obj, K8sResultBean.class);
      } catch (Exception e) {
        log.debug(
            "[BatchActivityTargetRunnableStrategy] Failed to parse K8sResultBean from string", e);
      }
    }

    // 尝试从Map解析
    if (obj instanceof Map) {
      try {
        String json = JSON.toJSONString(obj);
        return JSON.parseObject(json, K8sResultBean.class);
      } catch (Exception e) {
        log.debug(
            "[BatchActivityTargetRunnableStrategy] Failed to parse K8sResultBean from map", e);
      }
    }

    return null;
  }

  /**
   * 从identifier中提取pod名称 identifier格式:
   * cms-demo/cn-hongkong.10.0.2.27/product-catalog-59758b6f5d-d84mp/product-catalog/...
   * 提取第3段作为pod名称
   */
  private String extractPodNameFromIdentifier(String identifier) {
    if (identifier == null || identifier.isEmpty()) {
      return null;
    }

    String[] parts = identifier.split("/");
    if (parts.length >= 3) {
      return parts[2]; // pod名称在第3段(索引2)
    }

    return null;
  }

  /** 更新MiniAppTask的结果状态 根据host IP查找数据库记录并更新结果 */
  private void updateMiniAppTaskResult(
      String activityTaskId, String hostIp, String appCode, ChaosBladeAppResponse response) {
    try {
      ActivityTargetExecutionResultQuery query = new ActivityTargetExecutionResultQuery();
      query.setActivityTaskId(activityTaskId);
      query.setHostIp(hostIp);
      query.setAppCode(appCode);

      ExperimentMiniAppTaskDO taskDO = activityTargetExecutionResultRepository.findOne(query);
      if (taskDO != null) {
        taskDO.setState(StateEnum.FINISHED.getValue());
        taskDO.setGmtEnd(new Date());
        taskDO.setResult(
            response.isSuccess() ? ResultEnum.SUCCESS.getValue() : ResultEnum.FAILED.getValue());
        taskDO.setErrorMessage(
            response.getErrorMessage() == null ? CommonConstant.BLANK : response.getErrorMessage());

        // 更新 originResponse - 保存完整的response对象
        taskDO.setOriginResponse(JSON.toJSONString(response));

        // 更新 data 字段 - 保存 chaosBladeResponse（只包含当前pod的信息）
        if (response.getChaosBladeResponse() != null) {
          taskDO.setData(JSON.toJSONString(response.getChaosBladeResponse()));
        }

        boolean updated = activityTargetExecutionResultRepository.update(taskDO);
        if (updated) {
          log.info(
              "[BatchActivityTargetRunnableStrategy] Updated MiniAppTask for host {}: result={}, taskId={}",
              hostIp,
              response.isSuccess() ? "SUCCESS" : "FAILED",
              taskDO.getTaskId());
        } else {
          log.warn(
              "[BatchActivityTargetRunnableStrategy] Failed to update MiniAppTask for host {}",
              hostIp);
        }
      } else {
        log.warn(
            "[BatchActivityTargetRunnableStrategy] MiniAppTask not found for host {}, activityTaskId={}, appCode={}",
            hostIp,
            activityTaskId,
            appCode);
      }
    } catch (Exception e) {
      log.error(
          "[BatchActivityTargetRunnableStrategy] Error updating MiniAppTask for host " + hostIp, e);
    }
  }

  private static class ActivityTargetCallable implements Callable<ChaosAppResponse> {

    private Host host;

    private ActivityInvokeContext activityInvokeContext;

    public ActivityTargetCallable(Host host, ActivityInvokeContext activityInvokeContext) {
      this.host = host;
      this.activityInvokeContext = activityInvokeContext;
    }

    @Override
    public ChaosAppResponse call() throws Exception {
      return MiniAppInvokerFactory.createInvoker(this.activityInvokeContext, host).invoke();
    }
  }
}
