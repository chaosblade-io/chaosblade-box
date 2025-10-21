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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.feedback;

import com.alibaba.chaosblade.box.common.common.domain.feedback.ExtraFeedbackComponent;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/** @author haibin.lhb */
public interface ExtraFeedbackComponentPostProcessor {

  /**
   * 获取第三方的反馈组件
   *
   * @param experimentTaskDO 演练任务
   * @return 组件描述
   */
  public ExtraFeedbackComponent acquireExtraFeedbackComponent(ExperimentTaskDO experimentTaskDO);

  /**
   * 提交新的反馈
   *
   * @param experimentTaskDO
   * @param extraFeedbackComponent
   * @return true or false,第三方反馈是否提交成功
   * @throws Exception
   */
  public boolean submitExtraFeedbackComponent(
      ExperimentTaskDO experimentTaskDO, ExtraFeedbackComponent extraFeedbackComponent)
      throws Throwable;
}
