package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class RecordObject implements Serializable {

  private Map<String, Object> extraInfo = new HashMap<>();

  public void addExtra(String key, Object value) {
    extraInfo.put(key, value);
  }
}
