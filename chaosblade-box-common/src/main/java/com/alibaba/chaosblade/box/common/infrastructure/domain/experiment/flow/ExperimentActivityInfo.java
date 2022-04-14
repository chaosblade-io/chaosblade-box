package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import com.alibaba.chaosblade.box.common.common.domain.definition.BaseExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentGrade;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentActivityInfo extends BaseExperimentActivityDefinition {

    /**
     * 前端界面的ID
     */
    @JSONField(name = "id")
    private String fontId;

    /**
     * 活动的参数
     */
    @ApiParam(required = false)
    List<SceneArgumentGrade> arguments;

}
