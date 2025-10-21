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

package com.alibaba.chaosblade.box.controller;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.dao.infrastructure.service.MetricService;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.MiniAppParamOptionsService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.param.ParamOptionsQueryRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin */
@RestController
@Slf4j
public class ParamController extends BaseController {

  @Autowired private MiniAppParamOptionsService miniAppParamOptionsService;

  @Autowired
  @Resource(name = "cloudMetricService")
  private MetricService metricService;

  /**
   * 获取参数下拉选项
   *
   * @param chaosUser 登录用户
   * @param paramOptionsQueryRequest 参数选项
   * @return 下拉选项列表
   */
  @PostMapping(value = "/GetParamOptions")
  public RestResponse<Set<String>> getParamOptions(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    try {
      return RestResponseUtil.okWithData(
          miniAppParamOptionsService.queryParamOptions(paramOptionsQueryRequest));
    } catch (Exception e) {
      log.warn("GetParamOptions failed", e);
      return RestResponseUtil.okWithData(new HashSet<>());
    }
  }

  @PostMapping(value = "/GetNetworkDeviceParamOptions")
  public RestResponse<Set<String>> getNetworkDeviceParamOptions(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        miniAppParamOptionsService.queryNetworkDevice(paramOptionsQueryRequest));
  }

  @PostMapping(value = "/GetDiskDeviceParamOptions")
  public RestResponse<Set<String>> getDiskDeviceParamOptions(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        miniAppParamOptionsService.queryDiskDevice(paramOptionsQueryRequest));
  }

  @PostMapping(value = "/GetDiskBlockDevice")
  public RestResponse<Set<String>> getDiskBlockDeviceForPrivate(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setUser(chaosUser);
    return RestResponseUtil.okWithData(
        miniAppParamOptionsService.queryDiskBlockDevice(paramOptionsQueryRequest));
  }

  @PostMapping(value = "/GetMetricKeys")
  public RestResponse<List<String>> getMetricKey(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    paramOptionsQueryRequest.setUser(chaosUser);
    return RestResponseUtil.initWithServiceResponse(
        metricService.getMetricKeysByAppCode(paramOptionsQueryRequest.getAppCode()));
  }

  @PostMapping(value = "/GetCpuMetricKey")
  public RestResponse<List<String>> getCpuMetricKey(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    return RestResponseUtil.initWithServiceResponse(
        metricService.getMetricKeysByAppCode(paramOptionsQueryRequest.getAppCode()));
  }

  @PostMapping(value = "/GetJvmMetricKey")
  public RestResponse<List<String>> getJvmMetricKeys(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ParamOptionsQueryRequest paramOptionsQueryRequest) {
    return RestResponseUtil.initWithServiceResponse(
        metricService.getMetricKeysByAppCode(paramOptionsQueryRequest.getAppCode()));
  }
}
