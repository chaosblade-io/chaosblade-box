package com.alibaba.chaosblade.box.dao.mapper.type;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author sunju
 *
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
@MappedTypes(SceneFunctionParameterComponent.class)
public class SceneFunctionParameterComponentTypeHandler extends BaseFastJsonTypeHandler<SceneFunctionParameterComponent> {

    @Override
    public Class<SceneFunctionParameterComponent> getObjectClass() {
        return SceneFunctionParameterComponent.class;
    }
}
