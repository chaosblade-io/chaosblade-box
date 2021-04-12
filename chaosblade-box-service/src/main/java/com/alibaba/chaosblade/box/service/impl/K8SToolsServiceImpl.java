package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.service.K8SToolsService;
import com.alibaba.chaosblade.box.service.model.tools.ToolsRequest;
import com.alibaba.chaosblade.box.toolsmgr.api.Response;
import com.alibaba.chaosblade.box.toolsmgr.helm.HelmChaosToolsMgr;
import com.alibaba.chaosblade.box.toolsmgr.helm.HelmRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yefei
 */
@Slf4j
@Component
public class K8SToolsServiceImpl implements K8SToolsService {

    @Autowired
    private HelmChaosToolsMgr helmChaosToolsMgr;

    @Value("${chaos.helm.repo.name}")
    private String helmRepoName;

    private final static Map<String, String> TGZ_NAME = new HashMap<>();

    static {
        TGZ_NAME.put("litmuschaos", "litmus");
        TGZ_NAME.put("chaosblade", "chaosblade-operator");
    }

    @Override
    public String getHelmValuesYaml(ToolsRequest toolsRequest) {

        HelmRequest helmRequest = new HelmRequest();
        helmRequest.setToolsName(TGZ_NAME.get(toolsRequest.getName()));
        helmRequest.setToolsVersion(toolsRequest.getVersion());

        Response<String> response = helmChaosToolsMgr.getHelmValues(helmRequest);
        if (response.isSuccess()) {
            return response.getResult();
        } else {
            throw new BizException(response.getMessage());
        }
    }

    @Override
    public String deployChaostoolsToK8S(ToolsRequest toolsRequest) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(toolsRequest.getHelmValues());
        Map<String, String> params = new HashMap<>();
        trans(yamlMap, params, null);

        HelmRequest helmRequest = new HelmRequest();
        helmRequest.setName(TGZ_NAME.get(toolsRequest.getName()));
        helmRequest.setToolsName(helmRepoName + "/" + helmRequest.getName());
        helmRequest.setToolsVersion(toolsRequest.getVersion());

        StringBuilder commandOptions = new StringBuilder("--set ");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            commandOptions.append(entry.getKey() + "=" + entry.getValue() + ",");
        }
        helmRequest.setCommandOptions(commandOptions.toString().substring(0, commandOptions.length() - 1));

        Response<String> response = helmChaosToolsMgr.deployTools(helmRequest);
        if (response.isSuccess()) {
            return response.getResult();
        } else {
            throw new BizException(response.getMessage());
        }
    }

    private void trans(Map<String, Object> yamlMap, Map<String, String> params, String key) {
        for (Map.Entry<String, Object> entry : yamlMap.entrySet()) {
            Object value = entry.getValue();
            String appendKey = key == null ? entry.getKey() : key + "." + entry.getKey();
            if (value instanceof String) {
                params.put(appendKey, (String) value);
            } else if (value instanceof Map) {
                trans((Map) value, params, appendKey);
            }
        }
    }

}
