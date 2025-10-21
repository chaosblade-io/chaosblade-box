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

package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Data;

/** @author haibin */
@Data
public class InitMiniFlowRequest extends BaseRequest {

  public static Integer SOURCE_NON_APP = 0;

  public static Integer SOURCE_APP = 1;

  private String appCode;

  private PhaseType phase;

  /** 来源,默认是0，表示非应用 1表示应用 */
  @Nullable private Integer source = InitMiniFlowRequest.SOURCE_NON_APP;

  /** 应用ID */
  @Nullable private Long appId;

  private List<String> nodeGroups = new ArrayList<>();
}
