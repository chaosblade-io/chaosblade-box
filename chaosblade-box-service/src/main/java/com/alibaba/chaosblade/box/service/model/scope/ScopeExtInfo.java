package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ScopeExtInfo {

  @JSONField(name = "app_name")
  private String appName;

  @JSONField(name = "labels")
  private List<String> labels;
}
