package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ChaosToolsRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.chaostools.ToolsOverview;

import java.util.List;

/**
 * @author yefei
 */
public interface ChaosToolsService {

    /**
     *
     * @return
     */
    List<ToolsOverview> toolsOverviewList(ChaosToolsRequest chaosToolsRequest);

    /**
     *
     * @param chaosToolsRequest
     */
    Response<Boolean> installChaosTools(ChaosToolsRequest chaosToolsRequest);

    /**
     *
     * @param chaosToolsRequest
     */
    Response<Boolean> uninstallChaosTools(ChaosToolsRequest chaosToolsRequest);
}
