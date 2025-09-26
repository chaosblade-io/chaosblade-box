package com.alibaba.chaosblade.box.common.common.domain.search;

import java.util.List;
import lombok.Data;

/** @author haibin */
@Data
public class CommonExperimentSearchRequest {

  private String experimentId;

  private String name;

  private String userId;

  /** 标签里面只要有一个满足即可以 */
  private List<String> optionalTags;
}
