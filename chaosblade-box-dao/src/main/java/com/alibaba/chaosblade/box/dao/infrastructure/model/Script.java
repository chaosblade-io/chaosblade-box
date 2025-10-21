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

package com.alibaba.chaosblade.box.dao.infrastructure.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/** @author haibin */
public class Script implements Serializable {

  /** 脚本语言类型 */
  @Getter private String language;

  /** 脚本内容 */
  @Getter private String content;

  /** 脚本名字 */
  @Getter @Setter private String name;

  /** 脚本唯一的id */
  @Getter private String id;

  /** 脚本签名 */
  @Getter private String signature;

  public Script(String id, String signature, String name, String content, String language) {
    this.id = id;
    this.signature = signature;
    this.content = content;
    this.language = language;
    this.name = name;
  }
}
