package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author haibin
 *
 *
 */
public class StringHashMapTypehandler extends BaseTypeHandler<HashMap<String, String>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, HashMap<String, String> parameter, JdbcType jdbcType)
        throws SQLException {
        if (parameter == null) {
            ps.setString(i, null);
        } else {
            ps.setString(i, JSON.toJSONString(parameter));
        }
    }

    @Override
    public HashMap<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getMap(rs.getString(columnName));
    }

    @Override
    public HashMap<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getMap(rs.getString(columnIndex));
    }

    @Override
    public HashMap<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getMap(cs.getString(columnIndex));
    }

    private HashMap<String, String> getMap(String columnValue) {
        if (columnValue == null) { return new HashMap<String, String>(); }
        return JSON.parseObject(columnValue, new TypeReference<HashMap<String, String>>() {});
    }

}
