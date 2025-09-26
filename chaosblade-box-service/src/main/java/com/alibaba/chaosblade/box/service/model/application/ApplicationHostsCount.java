package com.alibaba.chaosblade.box.service.model.application;

import java.io.Serializable;
import lombok.Data;

/** @author sunpeng */
@Data
public class ApplicationHostsCount implements Serializable {

  private Integer total;

  private Integer permissionCount;

  private String message;
}
