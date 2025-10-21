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

package com.alibaba.chaosblade.box.service.infrastructure.service;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.service.model.param.HostExperimentTask;
import java.util.List;

/** @author haibin */
public interface ScopeService {

  /**
   * 根据进程名字判断进程是否存在
   *
   * @param host
   * @param processName
   * @return
   */
  public Response<String[]> checkProcessExistByName(Host host, String processName);

  /**
   * 根据进程号判断进程是否存在
   *
   * @param host
   * @param pid
   * @return
   */
  public Response<String[]> checkProcessExistByPid(Host host, String pid);

  /**
   * 检查机器是否属于某个演练当中
   *
   * @param host
   * @return 返回演练任务信息
   */
  public List<HostExperimentTask> checkHostInRunningExperiment(Host host);
}
