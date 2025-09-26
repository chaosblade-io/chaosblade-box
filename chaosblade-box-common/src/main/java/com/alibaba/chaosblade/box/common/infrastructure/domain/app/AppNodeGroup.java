package com.alibaba.chaosblade.box.common.infrastructure.domain.app;

import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class AppNodeGroup {

  private String nodeGroup;

  /** 机房分布的机器数量 */
  private Map<String, Long> rooms;

  /** 单元分布 */
  private Map<String, Long> units;
}
