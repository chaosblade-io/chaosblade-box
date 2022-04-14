package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: sunju
 *
 * Date:   2019/11/20
 */
@MappedTypes(JSONArray.class)
public class JSONArrayTypeHandler extends BaseTypeHandler<JSONArray> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, JSONArray objects, JdbcType jdbcType) throws SQLException {
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
    public JSONArray getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getJSONArray(callableStatement.getString(i));
    }

    private JSONArray getJSONArray(String value) {
        return JSON.parseArray(value);
    }

}
