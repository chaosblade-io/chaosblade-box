package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import lombok.Data;

import java.util.List;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class SceneArgumentGrade {

    /**
     * 场景参数
     */
    private List<SceneArgumentDefinition> argumentList;

    /**
     * 分级名称
     */
    private String gradeName;

    private int order;

    private boolean open;


}
