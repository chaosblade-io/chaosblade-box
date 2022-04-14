package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor;


import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentTaskCreateRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;

/**
 * @author haibin.lhb
 *
 * 
 */
public interface ExperimentTaskInterceptor {

    /**
     * 创建之前
     *
     * @param experimentTaskCreateRequest
     * @param experimentTaskDO
     */
    public void beforeSaveExperimentTaskDO(ExperimentTaskCreateRequest experimentTaskCreateRequest,
                                           ExperimentTaskDO experimentTaskDO);
}
