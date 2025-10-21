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
import com.alibaba.chaosblade.box.annotation.SwaggerDoc;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.activity.BaseActivityTaskRequest;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.app.MetricResultEntity;
import com.alibaba.chaosblade.box.model.RestResponseUtil;
import com.alibaba.chaosblade.box.service.command.task.QueryActivityTaskMetricCommand;
import com.alibaba.chaosblade.box.service.model.RestResponse;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author haibin */
@RestController
@Slf4j
@SwaggerDoc
@Api(description = "小程序指标查询")
public class MetricController extends BaseController {

  @Autowired private CommandBus commandBus;

  @PostMapping(value = {"/ReloadMetricActivity", "QueryActivityTaskMetric"})
  public RestResponse<List<MetricResultEntity>> reloadMetricActivity(
      @LoginUser ChaosUser chaosUser,
      @RequestBody BaseActivityTaskRequest baseActivityTaskRequest) {
    baseActivityTaskRequest.setUser(chaosUser);
    return RestResponseUtil.initWithServiceResponse(
        commandBus.syncRun(QueryActivityTaskMetricCommand.class, baseActivityTaskRequest));
  }
}
