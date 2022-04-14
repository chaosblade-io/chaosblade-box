package com.alibaba.chaosblade.box.dao.infrastructure.tunnel.mapper.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author sunju
 *
 */
public class TagSqlProvider {

    private static final String TABLE_NAME = "t_chaos_tag";

    public String search() {
        return new SQL() {{
            SELECT("*");
            FROM(TABLE_NAME);
            WHERE("name LIKE CONCAT('%', #{key}, '%')");
            OR();
            WHERE("code LIKE CONCAT('%', #{key}, '%')");
        }}.toString();
    }

    public static void main(String[] args) {
        System.out.println(new TagSqlProvider().search().toString());
    }

}
