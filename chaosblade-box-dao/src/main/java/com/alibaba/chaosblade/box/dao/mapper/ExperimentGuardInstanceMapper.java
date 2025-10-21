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

package com.alibaba.chaosblade.box.dao.mapper;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.guard.GuardRunState;
import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.model.ExperimentGuardInstanceDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/** @author haibin */
@MybatisMapper
public interface ExperimentGuardInstanceMapper extends BaseMapper<ExperimentGuardInstanceDO> {

  @Update(
      value =
          "update t_chaos_experiment_guard_instance set value=#{value} where instanceId=#{instanceId} and "
              + "state=#{state}")
  public int updateExperimentGuardMonitorMetricResultEntity(
      @Param("instanceId") String instanceId,
      @Param("value") String experimentGuardMonitorMetricResultEntity,
      @Param(value = "#{state}") GuardRunState guardRunState);
}
