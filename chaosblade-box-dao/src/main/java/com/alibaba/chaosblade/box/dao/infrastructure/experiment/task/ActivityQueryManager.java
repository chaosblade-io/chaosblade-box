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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.dao.model.ExperimentActivityDO;
import java.util.List;
import java.util.Optional;

/** @author haibin */
public interface ActivityQueryManager {

  /**
   * 查询然后组合活动
   *
   * @param experimentId 演练ID
   * @return
   */
  public List<ExperimentActivityDO> findActivitiesByExperimentId(String experimentId);

  /**
   * 根据activityID查询活动
   *
   * @param activityId
   * @return
   */
  public Optional<ExperimentActivityDO> findActivity(String activityId);

  /**
   * 获取指定阶段下面的所有活动
   *
   * @param experimentId 演练ID
   * @param phaseType 演练阶段名
   * @return
   */
  public List<ExperimentActivityDO> findActivitiesByPhase(String experimentId, PhaseType phaseType);
}
