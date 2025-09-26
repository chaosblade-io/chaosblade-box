package com.alibaba.chaosblade.box.common.infrastructure.domain.activity;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

/** @author haibin.lhb */
@Data
public class ActivityGroupDefinitionCheckResponse {

  @JSONField(name = "is_pass")
  private Boolean passed;

  private List<ActivityGroupDefinitionCheckItem> details;
}
