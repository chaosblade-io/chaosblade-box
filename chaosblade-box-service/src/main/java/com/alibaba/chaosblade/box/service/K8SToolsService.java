package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.service.model.tools.ToolsRequest;

/**
 * @author yefei
 */
public interface K8SToolsService {

    /**
     *
     * @param request
     * @return
     */
    String getHelmValuesYaml(ToolsRequest request);

    /**
     *
     * @param toolsRequest
     */
    String deployChaostoolsToK8S(ToolsRequest toolsRequest);
}
