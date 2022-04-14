package com.alibaba.chaosblade.box.dao.mapper.type;

import com.google.common.base.Joiner;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author haibin
 *
 * 
 */
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    private static String SEP = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
        throws SQLException {
        if (parameter == null) {
            ps.setString(i, null);
        } else {
            ps.setString(i, Joiner.on(SEP).join(parameter));
        }
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getList(rs.getString(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getList(rs.getString(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getList(cs.getString(columnIndex));
    }

    private List<String> getList(String columnValue) {
        if (columnValue == null) { return new ArrayList<>(); }
        return new ArrayList<>(Arrays.asList(columnValue.split(SEP)));
    }

}
