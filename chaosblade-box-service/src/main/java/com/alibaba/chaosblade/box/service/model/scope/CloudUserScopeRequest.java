package com.alibaba.chaosblade.box.service.model.scope;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class CloudUserScopeRequest extends PageableRequest {

  private Integer scopeType;
  private Integer osType;
  private String key;

  private List<String> tags;
}
