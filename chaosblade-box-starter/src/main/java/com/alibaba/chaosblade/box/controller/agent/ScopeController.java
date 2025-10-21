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

package com.alibaba.chaosblade.box.controller.agent;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.alibaba.chaosblade.box.controller.BaseController;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.CloudScopeService;
import com.alibaba.chaosblade.box.service.ScopeService;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.alibaba.chaosblade.box.service.model.scope.*;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author sunju */
@RestController
public class ScopeController extends BaseController {

  private static final Integer DEFAULT_PAGE_SIZE = 20;

  @Resource private ScopeService scopeService;

  @Autowired private CloudScopeService cloudScopeService;

  /**
   * 查询某个机器详情
   *
   * @param chaosUser
   * @param scopeQueryRequest
   * @return
   */
  @PostMapping("QueryScopeInfo")
  public RestResponse<ScopeInfo> findOneScope(
      @LoginUser ChaosUser chaosUser, @RequestBody ScopeInfoQueryRequest scopeQueryRequest) {

    return RestResponseUtil.initWithServiceResponse(
        cloudScopeService.findOneScope(scopeQueryRequest));
  }

  /**
   * 根据机器类型分页查询
   *
   * @param chaosUser
   * @param experimentScopePageableRequest
   * @return
   */
  @PostMapping("PageableQueryExperimentScopes")
  public RestResponse<PageableResponse<ExperimentScope>> pageableQueryExperimentScopes(
      @LoginUser ChaosUser chaosUser,
      @RequestBody ExperimentScopePageableRequest experimentScopePageableRequest) {
    experimentScopePageableRequest.setUser(chaosUser);
    return RestResponseUtil.initWithServiceResponse(
        cloudScopeService.pageableQueryExperimentScopes(experimentScopePageableRequest));
  }

  /**
   * 统计机器的演练数目
   *
   * @param chaosUser
   * @param scopeQueryRequest
   * @return
   */
  @PostMapping("CountExperimentScopeInvocation")
  public RestResponse<List<ExperimentScopeInvokeCount>> countExperimentScopeInvocation(
      @LoginUser ChaosUser chaosUser, @RequestBody ScopeInfoQueryRequest scopeQueryRequest) {
    return RestResponseUtil.initWithServiceResponse(
        cloudScopeService.countExperimentScopeInvocation(scopeQueryRequest));
  }

  /**
   * 统计机器涉及到的演练场景
   *
   * @param chaosUser
   * @param scopeQueryRequest
   * @return
   */
  @PostMapping("CountExperimentScopeSceneFunctionCount")
  public RestResponse<List<FunctionInvocationCount>> countExperimentScopeSceneFunctionCount(
      @LoginUser ChaosUser chaosUser, @RequestBody ScopeInfoQueryRequest scopeQueryRequest) {
    return RestResponseUtil.initWithServiceResponse(
        cloudScopeService.countExperimentScopeSceneFunctionCount(scopeQueryRequest));
  }

  /**
   * 根据机器分页查询演练任务
   *
   * @param chaosUser
   * @param scopeQueryRequest
   * @return
   */
  @PostMapping("PageableQueryExperimentTaskByScope")
  public RestResponse<PageableResponse<ScopeExperimentTask>> pageableQueryExperimentTaskByScope(
      @LoginUser ChaosUser chaosUser,
      @RequestBody PageableScopeExperimentTaskQueryRequest scopeQueryRequest) {
    return RestResponseUtil.initWithServiceResponse(
        cloudScopeService.pageableQueryExperimentTaskByScope(scopeQueryRequest));
  }

  @PostMapping("UserScope")
  public RestResponse<PageableResponse<CloudDevice>> getUserScopes(
      @LoginUser ChaosUser user, @RequestBody CloudUserScopeRequest cloudUserScopeRequest) {
    String namespace = cloudUserScopeRequest.getNamespace();
    ScopeTypeEnum scopeTypeEnum =
        EnumUtil.integerValueOf(ScopeTypeEnum.class, cloudUserScopeRequest.getScopeType());
    if (scopeTypeEnum == null) {
      return RestResponseUtil.failed(ChaosError.withCode(CommonErrorCode.B_ERR_UNKNOWN_SCOPE_TYPE));
    }
    CloudDeviceQuery query = new CloudDeviceQuery();
    query.setUserId(user.getUserId());
    query.setNamespace(namespace);
    query.setScopeType(scopeTypeEnum);
    query.setKey(cloudUserScopeRequest.getKey());
    query.setTags(cloudUserScopeRequest.getTags());
    query.setOsType(cloudUserScopeRequest.getOsType());
    return RestResponseUtil.okWithData(
        scopeService.queryAliveScopesByPage(
            user,
            PageableQueryWrapper.of(query)
                .pageNumber(cloudUserScopeRequest.getPage())
                .pageSize(cloudUserScopeRequest.getSize()),
            namespace));
  }
}
