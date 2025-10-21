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

package com.alibaba.chaosblade.box.common.common.domain.user;

import java.io.Serializable;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChaosUser implements Serializable {
  /** 系统管理员 */
  public static final ChaosUser SYSTEM = new ChaosUser("-1");

  private Long id;
  /** 用户ID */
  String userId;

  /** 用户名字 */
  String userName;

  String userNick;

  String password;

  String license;

  public ChaosUser(String userId) {
    this.userId = userId;
  }

  public ChaosUser(String userName, String password) { // , Role role
    this.userName = userName;
    this.password = password;
    this.license = genRandomId();
    this.userId = genRandomLong();
  }

  private String genRandomId() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  private String genRandomLong() {
    int hash = UUID.randomUUID().hashCode();
    if (hash < 0) {
      hash = -hash;
    }
    return Integer.toString(hash);
  }

  public String getCurrentUserId() {
    return this.userId;
  }

  public Long getId() {
    return this.id;
  }
}
