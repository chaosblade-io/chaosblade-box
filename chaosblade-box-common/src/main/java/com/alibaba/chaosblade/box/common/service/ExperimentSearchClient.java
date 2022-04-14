package com.alibaba.chaosblade.box.common.service;

import com.alibaba.chaosblade.box.common.common.domain.search.CommonExperimentSearchRequest;
import com.alibaba.chaosblade.box.common.common.domain.search.ExperimentSearchResult;

/**
 * @author haibin
 *
 *
 */
public interface ExperimentSearchClient {

    /**
     * search experiment
     *
     * @param experimentSearchRequest
     * @return
     */
    public ExperimentSearchResult search(CommonExperimentSearchRequest experimentSearchRequest);
}
