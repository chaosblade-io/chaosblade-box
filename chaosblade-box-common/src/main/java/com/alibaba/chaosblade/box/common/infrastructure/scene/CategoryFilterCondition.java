package com.alibaba.chaosblade.box.common.infrastructure.scene;

import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class CategoryFilterCondition {


    private Boolean filterNoChild;

    private String cloudServiceType;

    private Integer phase;


}
