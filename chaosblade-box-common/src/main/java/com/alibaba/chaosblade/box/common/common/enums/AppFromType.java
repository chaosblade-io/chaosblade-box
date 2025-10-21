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

package com.alibaba.chaosblade.box.common.common.enums;

/**
 * APP名称聚合方式
 *
 * @author: xinyuan
 * @create: 2021-04-20 9:50 上午
 */
public enum AppFromType {
  /** k8s标签 */
  LABEL,
  /** k8s注解，暂时不支持 */
  ANNOTATION,
  /** 进程命令行 */
  COMMANDLINE,
  /** 主机安装时基本信息 */
  HOST_BASE,
}
