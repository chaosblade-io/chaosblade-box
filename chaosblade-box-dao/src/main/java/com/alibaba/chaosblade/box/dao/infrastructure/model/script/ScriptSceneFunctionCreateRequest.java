package com.alibaba.chaosblade.box.dao.infrastructure.model.script;

import com.alibaba.chaosblade.box.common.infrastructure.domain.scene.SceneFunctionCreateRequest;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ScriptSceneFunctionCreateRequest extends SceneFunctionCreateRequest {

  private String scriptContent;

  private String language;

  private List<SceneFunctionParameterDO> parameters;
}
