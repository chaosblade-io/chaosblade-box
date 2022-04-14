package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author haibin
 *
 * 
 */
public abstract class BaseFastJsonTypeHandler<T> extends BaseTypeHandler<T> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter, SerializerFeature.WriteNullStringAsEmpty));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getObject(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getObject(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getObject(cs.getString(columnIndex));
    }

    private T getObject(String columnValue) {
        return JSON.parseObject(columnValue, getObjectClass());
    }

    public abstract Class<T> getObjectClass();
}
