package com.alibaba.chaosblade.box.common.common.domain.experiment;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentRelation {

    /**
     * GOC应急场景
     */
    public static final String RELATION_TYPE_GOC_EMERGENCY_SCENE = "emergency_scene";

    /**
     * 关联ID，如果是创建则可以不用填
     */
    String relationId;

    /**
     * 外部ID,比如goc的ref
     */
    String outerId;
    /**
     * 关系描述
     */
    String outerDescription;

    /**
     * 关系类型
     */
    String relationType;

}
