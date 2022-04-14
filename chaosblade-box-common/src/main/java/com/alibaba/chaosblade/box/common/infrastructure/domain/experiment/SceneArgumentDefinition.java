package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class SceneArgumentDefinition {

    private String alias;

    private String value;

    private String name;

    private String description;

    private SceneFunctionParameterComponent component;
    /**
     * 是否有效
     */
    private boolean enabled;

    private String unit;

    private String parameterId;

    private String functionId;

    private Integer order;

    /**
     * 参数分级
     */
    private Integer grade;

}
