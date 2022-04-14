package com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author sunju
 *
 */
public class SceneSqlProvider {

    private static final String TABLE_NAME = "t_chaos_scene";

    public String search() {
        return new SQL() {{
            SELECT("*");
            FROM(TABLE_NAME);
            WHERE("name LIKE CONCAT('%', #{key}, '%')");
            OR();
            WHERE("code LIKE CONCAT('%', #{key}, '%')");
            AND();
            WHERE("state = ${state}");
            AND();
            WHERE("is_delete = 0");
        }}.toString();
    }

}
