package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.StableProbeInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import org.springframework.stereotype.Component;

/**
 * @author sunpeng
 *
 *
 */
@Component
public class DefaultStableProbeInterceptor implements StableProbeInterceptor {

    @Override
    public void handleStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest) {

    }

    @Override
    public void deleteStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest) {

    }
}
