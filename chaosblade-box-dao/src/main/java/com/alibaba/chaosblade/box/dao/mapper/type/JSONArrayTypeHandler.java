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
import com.alibaba.fastjson.JSONArray;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

/**
 * Author: sunju
 *
 * <p>Date: 2019/11/20
 */
@MappedTypes(JSONArray.class)
public class JSONArrayTypeHandler extends BaseTypeHandler<JSONArray> {

  @Override
  public void setNonNullParameter(
      PreparedStatement preparedStatement, int i, JSONArray objects, JdbcType jdbcType)
      throws SQLException {
    preparedStatement.setString(i, JSON.toJSONString(objects));
  }

  @Override
  public JSONArray getNullableResult(ResultSet resultSet, String s) throws SQLException {
    return getJSONArray(resultSet.getString(s));
  }

  @Override
  public JSONArray getNullableResult(ResultSet resultSet, int i) throws SQLException {
    return getJSONArray(resultSet.getString(i));
  }

  @Override
  public JSONArray getNullableResult(CallableStatement callableStatement, int i)
      throws SQLException {
    return getJSONArray(callableStatement.getString(i));
  }

  private JSONArray getJSONArray(String value) {
    return JSON.parseArray(value);
  }
}
