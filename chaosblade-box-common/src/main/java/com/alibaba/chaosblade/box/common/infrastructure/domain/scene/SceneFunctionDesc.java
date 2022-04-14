package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class SceneFunctionDesc {

    private String name;

    private String appCode;

    private List<SceneFunctionParamDesc> params;

}
