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

import com.alibaba.chaosblade.box.common.infrastructure.util.MybatisMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.entity.ExperimentTaskEntity;
import com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider.HostExperimentCompoundSqlProvider;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

/** @author haibin */
@MybatisMapper
public interface HostExperimentMapper {

  @SelectProvider(type = HostExperimentCompoundSqlProvider.class, method = "hostExperiment")
  @Results(
      value = {
        @Result(property = "userId", column = "user_id"),
        @Result(property = "experimentTaskId", column = "experiment_task_id")
      })
  public List<ExperimentTaskEntity> queryExperimentTaskIdByHost(@Param("hostIp") String host);
}
