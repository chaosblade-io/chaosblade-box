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

package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

/** @author haibin */
public enum ExceptionCode {

  /** 创建演练失败 */
  CREATE_EXPERIMENT_FAILED,
  /** 演练解析失败 */
  EXPERIMENT_DEFINITION_PARSER_FAILED,
  /** 演练活动执行失败 */
  ACTIVITY_EXECUTE_FAILED,
  /** 插件调用失败 */
  PLUGIN_EXECUTE_FAILED,
  /** 无效的演练 */
  INVALID_EXPERIMENT,
  /** 创建演练活动失败 */
  CREATE_ACTIVITY_FAILED,
  /** 无效的活动 */
  INVALID_ACTIVITY,
  ACTION_NOT_ALLOW
}
