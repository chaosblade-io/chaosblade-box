package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.common.common.domain.search.CommonExperimentSearchRequest;
import com.alibaba.chaosblade.box.common.common.domain.search.ExperimentSearchResult;
import com.alibaba.chaosblade.box.common.service.ExperimentSearchClient;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTagRepository;
import com.alibaba.chaosblade.box.dao.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin
 *
 *
 */
public class DbExperimentSearchClient implements ExperimentSearchClient {
    @Autowired
    private ExperimentRepository experimentRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ExperimentTagRepository experimentTagRepository;

    @Override
    public ExperimentSearchResult search(CommonExperimentSearchRequest experimentSearchRequest) {
        return new ExperimentSearchResult();
    }

}
