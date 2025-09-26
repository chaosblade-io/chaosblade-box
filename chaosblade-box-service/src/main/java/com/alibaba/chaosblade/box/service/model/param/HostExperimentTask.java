package com.alibaba.chaosblade.box.service.model.param;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class HostExperimentTask implements Serializable {

  private String user;

  private String experimentTaskId;
}
