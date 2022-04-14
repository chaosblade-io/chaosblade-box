package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;


import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;

public interface StableProbeInterceptor {

    /**
     * 处理稳态监控
     * @param experimentDefinitionRequest
     */
    void handleStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest);

    /**
     * 删除稳态监控
     * @param experimentDefinitionRequest
     */
    void deleteStableProbe(ExperimentDefinitionRequest experimentDefinitionRequest);


}
