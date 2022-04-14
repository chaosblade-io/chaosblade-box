package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ActivityRetryRequest extends BaseRequest {


    private ActivityTaskDO activityTaskDO;
}
