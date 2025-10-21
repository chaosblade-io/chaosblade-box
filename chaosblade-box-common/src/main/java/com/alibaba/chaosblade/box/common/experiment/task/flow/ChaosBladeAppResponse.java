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

package com.alibaba.chaosblade.box.common.experiment.task.flow;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/** @author haibin */
@Data
public class ChaosBladeAppResponse extends ChaosAppResponse {

  // 只有是数字和字母组合才算有效的ExpId
  public void setChaosBladeExpId(String chaosBladeExpId) {
    if (StringUtils.isAlphanumeric(chaosBladeExpId)) {
      this.chaosBladeExpId = chaosBladeExpId;
    }
  }

  /** chaosBlade调用ID，warning: 只有调用chaosBlade才需要，自定义小程序不需要的 */
  private String chaosBladeExpId;

  /** chaosBlade执行结果，warning: 只有调用chaosBlade才需要，自定义小程序不需要的 */
  private Response chaosBladeResponse;

  /** status */
  private Integer status;
}
