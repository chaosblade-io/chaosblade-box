package com.alibaba.chaosblade.box.service.model.cluster;

import java.io.Serializable;
import lombok.Data;

@Data
public class CacheRefreshDTO implements Serializable {
  private static final long serialVersionUID = -1L;
  private String userId;
  private String operation;
  private String resourceType;
  private String configurationId;
}
