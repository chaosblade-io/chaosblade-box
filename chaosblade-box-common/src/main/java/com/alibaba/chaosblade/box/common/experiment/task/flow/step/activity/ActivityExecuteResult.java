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

package com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.StepExecuteResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityExecuteResult extends StepExecuteResult {

  private String appCode;

  /** 小程序每次调用的结果 */
  private List<ChaosAppResponse> appResponses = new ArrayList<>();

  private Map<String, Object> miniAppContextData;

  private String errorMessage;

  public void addChaosAppResponse(ChaosAppResponse chaosAppResponse) {
    this.appResponses.add(chaosAppResponse);
  }
}
