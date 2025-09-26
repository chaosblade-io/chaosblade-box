package com.alibaba.chaosblade.box.common.common.domain.notification;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExperimentNotification {

  private boolean dingDing;

  private boolean email;

  private List<String> empIds;
}
