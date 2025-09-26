package com.alibaba.chaosblade.box.common.infrastructure.domain.scene;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class SceneFunctionDesc {

  private String name;

  private String appCode;

  private List<SceneFunctionParamDesc> params;
}
