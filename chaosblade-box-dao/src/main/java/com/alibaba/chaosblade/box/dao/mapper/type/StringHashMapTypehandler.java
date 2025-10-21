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

package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/** @author haibin */
public class StringHashMapTypehandler extends BaseTypeHandler<HashMap<String, String>> {
  @Override
  public void setNonNullParameter(
      PreparedStatement ps, int i, HashMap<String, String> parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter == null) {
      ps.setString(i, null);
    } else {
      ps.setString(i, JSON.toJSONString(parameter));
    }
  }

  @Override
  public HashMap<String, String> getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return getMap(rs.getString(columnName));
  }

  @Override
  public HashMap<String, String> getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return getMap(rs.getString(columnIndex));
  }

  @Override
  public HashMap<String, String> getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return getMap(cs.getString(columnIndex));
  }

  private HashMap<String, String> getMap(String columnValue) {
    if (columnValue == null) {
      return new HashMap<String, String>();
    }
    return JSON.parseObject(columnValue, new TypeReference<HashMap<String, String>>() {});
  }
}
