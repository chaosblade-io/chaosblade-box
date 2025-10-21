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

package com.alibaba.chaosblade.box.common.common.domain.experiment;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityRunParam {

  private String activityId;

  ExperimentNodeArgumentsDefinition arguments;

  List<Host> scope;

  /** 当前节点失败后是否立刻终止演练,只在非手动推进才生效,true 为开启，false为关闭, */
  boolean interruptedIfFailed = false;

  /** 失败容忍度,当下面的机器或者子任务运行失败占比超过指定值,当前节点才会认定为失败,值在[0-100] */
  int failedTolerance = 0;

  Boolean needUserCheck = null;
}
