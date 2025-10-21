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
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import com.alibaba.chaosblade.box.service.model.overview.*;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author sunpeng */
@RestController
public class OverviewController extends BaseController {

  @Resource private OverviewService overviewService;

  @PostMapping("UserExperimentOverviewInfo")
  public RestResponse<OverviewExperimentCount> userExperimentOverviewInfo(
      @LoginUser ChaosUser chaosUser, @RequestBody OverviewRequest overviewRequest) {
    overviewRequest.setUser(chaosUser);
    return RestResponseUtil.initWithServiceResponse(
        overviewService.getUserExperimentCount(overviewRequest));
  }

  @PostMapping("UserExperimentByDayOverviewInfo")
  public RestResponse<OverviewExperimentTask> userExperimentByDayOverviewInfo(
      @LoginUser ChaosUser chaosUser) {
    return RestResponseUtil.initWithServiceResponse(
        overviewService.getUserExperimentDayCount(chaosUser));
  }

  @PostMapping("UserAgentOverviewInfo")
  public RestResponse<OverviewAgent> userAgentOverviewInfo(@LoginUser ChaosUser chaosUser) {
    return RestResponseUtil.initWithServiceResponse(overviewService.getUserAgentCount(chaosUser));
  }

  @PostMapping("ProductMessageOverviewInfo")
  public RestResponse<OverviewProduct> productMessageOverviewInfo(@LoginUser ChaosUser chaosUser) {
    return RestResponseUtil.initWithServiceResponse(overviewService.getProductMessage(chaosUser));
  }

  @PostMapping("UserSceneOverview")
  public RestResponse<List<OverviewScene>> userSceneOverview(
      @LoginUser ChaosUser chaosUser, @RequestBody BaseRequest baseRequest) {
    return RestResponseUtil.initWithServiceResponse(
        overviewService.getUserScene(chaosUser, baseRequest));
  }

  @PostMapping("UserExpertiseOverview")
  public RestResponse<PageQueryResponse<OverviewExpertise>> userExpertiseOverview(
      @LoginUser ChaosUser chaosUser, @RequestBody OverviewRequest overviewRequest) {
    return RestResponseUtil.initWithServiceResponse(
        overviewService.getUserExpertise(chaosUser, overviewRequest));
  }
}
