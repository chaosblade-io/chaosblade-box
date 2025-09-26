package com.alibaba.chaosblade.box.common.experiment.activity.task;

import com.alibaba.chaosblade.box.common.common.domain.activity.ActivityTask;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class EnhanceActivityTask extends ActivityTask {

  private List<SceneArgumentDefinition> arguments;
}
