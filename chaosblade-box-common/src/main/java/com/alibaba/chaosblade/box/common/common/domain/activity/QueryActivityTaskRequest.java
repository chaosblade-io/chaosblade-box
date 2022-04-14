package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class QueryActivityTaskRequest extends BaseRequest {

    private String activityTaskId;
}
