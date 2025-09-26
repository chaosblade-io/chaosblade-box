package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ActivityParamPreCheckContext {

  private List<Host> hosts;

  private String appId;
}
