package com.alibaba.chaosblade.box.common.common.domain.activity;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseActivityTaskRequest extends BaseRequest {

    public BaseActivityTaskRequest(String activityTaskId) {
        this.activityTaskId = activityTaskId;
    }

    private String activityTaskId;
}
