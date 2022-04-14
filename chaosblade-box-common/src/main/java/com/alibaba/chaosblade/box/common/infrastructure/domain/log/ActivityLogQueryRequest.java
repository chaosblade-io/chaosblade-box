package com.alibaba.chaosblade.box.common.infrastructure.domain.log;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityLogQueryRequest extends BaseRequest {

    private String activityTaskId;

    private Date from;

}
