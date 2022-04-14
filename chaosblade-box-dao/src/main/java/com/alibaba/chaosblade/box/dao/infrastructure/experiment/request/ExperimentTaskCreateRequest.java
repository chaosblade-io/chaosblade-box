package com.alibaba.chaosblade.box.dao.infrastructure.experiment.request;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunParam;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.request.ExperimentRunRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ExperimentTaskCreateRequest extends BaseRequest {

    private ExperimentDO experimentDO;

    private String outerId;

    /**
     * 如果为null，那么就会生成一个
     */
    private String experimentTaskId;

    private ExperimentRunParam experimentRunParam;

    /**
     * 是否定时任务触发
     */
    private boolean triggeredByScheduler;

    public static ExperimentTaskCreateRequest buildExperimentTaskCreateRequest(
        ExperimentRunRequest experimentRunRequest,
        ExperimentDO experimentDO) {
        ExperimentTaskCreateRequest experimentTaskCreateRequest = new ExperimentTaskCreateRequest();
        experimentTaskCreateRequest.setExperimentDO(experimentDO);
        experimentTaskCreateRequest.setUser(experimentRunRequest.getUser());
        experimentTaskCreateRequest.setNamespace(experimentRunRequest.getNamespace());
        experimentTaskCreateRequest.setExperimentRunParam(experimentRunRequest.getParam());
        String outerId = experimentRunRequest.getParam() != null ? experimentRunRequest.getParam().getOuterId() : null;
        experimentTaskCreateRequest.setOuterId(outerId);
        experimentTaskCreateRequest.setTriggeredByScheduler(experimentRunRequest.isTriggeredByScheduler());
        return experimentTaskCreateRequest;
    }
}
