package com.alibaba.chaosblade.box.dao.mapper.type;

import com.google.common.base.Strings;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author sunju
 *
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes(UUID.class)
public class UUIDTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String uuid = rs.getString(columnName);
        return fromString(uuid);
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String uuid = rs.getString(columnIndex);
        return fromString(uuid);
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String uuid = cs.getString(columnIndex);
        return fromString(uuid);
    }

    private UUID fromString(String uuid) {
        if (Strings.isNullOrEmpty(uuid)) {
            return null;
        }
        return UUID.fromString(uuid);
    }
}
