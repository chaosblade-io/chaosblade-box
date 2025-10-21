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

package com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

/** @author sunju */
public class SceneSqlProvider {

  private static final String TABLE_NAME = "t_chaos_scene";

  public String search() {
    return new SQL() {
      {
        SELECT("*");
        FROM(TABLE_NAME);
        WHERE("name LIKE CONCAT('%', #{key}, '%')");
        OR();
        WHERE("code LIKE CONCAT('%', #{key}, '%')");
        AND();
        WHERE("state = ${state}");
        AND();
        WHERE("is_delete = 0");
      }
    }.toString();
  }
}
