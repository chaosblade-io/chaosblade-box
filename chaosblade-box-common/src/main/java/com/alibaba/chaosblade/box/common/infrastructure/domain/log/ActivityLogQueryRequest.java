package com.alibaba.chaosblade.box.common.infrastructure.domain.log;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import java.util.Date;
import lombok.Data;

/** @author haibin */
@Data
public class ActivityLogQueryRequest extends BaseRequest {

  private String activityTaskId;

  private Date from;
}
