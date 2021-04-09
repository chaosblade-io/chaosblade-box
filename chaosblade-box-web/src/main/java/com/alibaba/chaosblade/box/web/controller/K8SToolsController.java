package com.alibaba.chaosblade.box.web.controller;

import com.alibaba.chaosblade.box.service.K8SToolsService;
import com.alibaba.chaosblade.box.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.box.web.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class K8SToolsController {

    @Autowired
    private K8SToolsService k8SToolsService;

    @RequestMapping("/GetHelmValueYaml")
    public Response<String> getHelmValueYaml(@RequestBody ToolsRequest toolsRequest, HttpServletResponse response) throws Exception {
        String helmValuesYaml = k8SToolsService.getHelmValuesYaml(toolsRequest);
        return Response.ofSuccess(helmValuesYaml);
    }

    @RequestMapping("/DeployChaostoolsToK8S")
    public Response<String> deployChaostoolsToK8S(@RequestBody ToolsRequest toolsRequest) {
        return Response.ofSuccess(k8SToolsService.deployChaostoolsToK8S(toolsRequest));
    }

}
