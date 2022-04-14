package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckResponse;

/**
 * @author haibin.lhb
 *
 *
 */
public interface ActivityService {

    /**
     * 校验微流程组的参数
     *
     * @param activityGroupDefinitionCheckRequest
     * @return
     */
    public Response<ActivityGroupDefinitionCheckResponse> checkActivityGroupDefinition(
        ActivityGroupDefinitionCheckRequest activityGroupDefinitionCheckRequest);

}
