package com.alibaba.chaosblade.box.service.model.application;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import java.util.List;
import lombok.Data;

/** @author sunpeng */
@Data
public class ApplicationHostsCountRequest extends BaseRequest {

  private String appId;

  private List<String> groupNames;
}
