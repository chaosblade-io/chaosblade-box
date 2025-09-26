package com.alibaba.chaosblade.box.service.model.expertise;

import com.alibaba.chaosblade.box.common.common.domain.PageableRequest;
import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class ExpertiseSearchRequest extends PageableRequest {

  private String key;

  private List<String> tagNames;

  private String scopeType;
}
