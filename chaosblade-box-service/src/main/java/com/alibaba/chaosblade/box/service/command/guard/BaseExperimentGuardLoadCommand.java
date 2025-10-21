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

package com.alibaba.chaosblade.box.service.command.guard;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.guard.ExperimentGuardResultLoadRequest;
import com.alibaba.chaosblade.box.dao.mapper.ExperimentGuardInstanceMapper;
import com.alibaba.chaosblade.box.dao.repository.ExperimentGuardInstanceRepository;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
@Slf4j
public abstract class BaseExperimentGuardLoadCommand<T>
    extends SpringBeanCommand<ExperimentGuardResultLoadRequest, T> {

  @Autowired protected ExperimentGuardInstanceRepository experimentGuardInstanceRepository;

  @Autowired protected ExperimentGuardInstanceMapper experimentGuardInstanceMapper;

  @Override
  public T execute(ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest) {
    try {
      return internalExecute(experimentGuardResultLoadRequest);
    } catch (Throwable throwable) {
      log.error(
          "handle guard instance failed,experiment taskId:{}",
          experimentGuardResultLoadRequest.getExperimentTaskDO().getTaskId(),
          experimentGuardResultLoadRequest.getExperimentGuardInstanceDO().getInstanceId(),
          throwable);
    }
    return null;
  }

  /**
   * 内部执行流程
   *
   * @param experimentGuardResultLoadRequest
   * @return
   */
  protected abstract T internalExecute(
      ExperimentGuardResultLoadRequest experimentGuardResultLoadRequest);

  /**
   * 计算时间区间
   *
   * @param offsetInSeconds 时间偏移量
   * @return
   */
  protected static Pair<Date, Date> calcTimeRange(int offsetInSeconds) {
    Calendar calendar = Calendar.getInstance();
    Date endTime = calendar.getTime();
    calendar.add(Calendar.SECOND, -offsetInSeconds);
    Date startTime = calendar.getTime();
    return Pair.of(startTime, endTime);
  }
}
