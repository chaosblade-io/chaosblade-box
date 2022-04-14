package com.alibaba.chaosblade.box.common.experiment.task.flow.step.activity;


import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppResponse;
import com.alibaba.chaosblade.box.common.experiment.task.flow.step.StepExecuteResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author haibin
 * 
 *
 */
@Data
public class ActivityExecuteResult extends StepExecuteResult {

    private String appCode;

    /**
     * 小程序每次调用的结果
     */
    private List<ChaosAppResponse> appResponses = new ArrayList<>();

    private Map<String, Object> miniAppContextData;

    private String errorMessage;

    public void addChaosAppResponse(ChaosAppResponse chaosAppResponse) {
        this.appResponses.add(chaosAppResponse);
    }

}

