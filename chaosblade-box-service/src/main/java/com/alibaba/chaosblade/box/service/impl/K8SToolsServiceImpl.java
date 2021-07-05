package com.alibaba.chaosblade.box.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.exception.BizException;
import com.alibaba.chaosblade.box.common.exception.ExceptionMessageEnum;
import com.alibaba.chaosblade.box.dao.model.ToolsDO;
import com.alibaba.chaosblade.box.dao.repository.ToolsRepository;
import com.alibaba.chaosblade.box.service.ClusterService;
import com.alibaba.chaosblade.box.service.K8SToolsService;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterBO;
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
import java.util.StringJoiner;

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

    @Value("${chaos.helm.repo.url}")
    private String helmRepoUrl;

    @Autowired
    private ToolsRepository toolsRepository;

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ClusterService clusterService;

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

        ToolsDO toolsDO = toolsRepository.selectByNameAndVersion(
                toolsRequest.getMachineId(),
                toolsRequest.getName(),
                toolsRequest.getVersion());
        if (toolsDO != null) {
            throw new BizException(ExceptionMessageEnum.CHAOS_TOOLS_EXISTS, toolsRequest.getName() + ":" + toolsRequest.getVersion());
        }

        HelmRequest helmRequest = new HelmRequest();
        helmRequest.setName(TGZ_NAME.get(toolsRequest.getName()));
        helmRequest.setToolsName(helmRepoName + "/" + helmRequest.getName());
        helmRequest.setToolsVersion(toolsRequest.getVersion());

        String kubeconfig = clusterService.getKubeconfig(ClusterBO.builder().id(toolsRequest.getMachineId()).build());
        helmRequest.setKubeconfig(kubeconfig);

        StringJoiner commandOptions = new StringJoiner(" ");

        //append helmValues if existed
        String helmValues = toolsRequest.getHelmValues();
        if (!StrUtil.isBlank(helmValues)) {
            Yaml yaml = new Yaml();
            Map<String, Object> yamlMap = yaml.load(helmValues);
            Map<String, String> params = new HashMap<>();
            trans(yamlMap, params, null);
            StringBuilder setOptions = new StringBuilder("--set ");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                setOptions.append(entry.getKey() + "=" + entry.getValue() + ",");
            }
            commandOptions.add(setOptions.substring(0, setOptions.length() - 1));
        }

        //append namespace if existed
        if (!StrUtil.isBlank(toolsRequest.getNamespace())) {
            commandOptions.add("-n " + toolsRequest.getNamespace());
        }

        helmRequest.setCommandOptions(commandOptions.toString());

        Response<String> response = helmChaosToolsMgr.deployTools(helmRequest);
        if (response.isSuccess()) {
            toolsRepository.insert(ToolsDO.builder()
                    .deviceId(toolsRequest.getMachineId())
                    .deviceType(DeviceType.CLUSTER.getCode())
                    .name(toolsRequest.getName())
                    .version(toolsRequest.getVersion())
                    .url(helmRepoUrl)
                    .build());
            return response.getResult();
        } else {
            throw new BizException(response.getMessage());
        }
    }

    @Override
    public String undeployChaostoolsToK8S(ToolsRequest toolsRequest) {

        HelmRequest helmRequest = new HelmRequest();
        helmRequest.setName(TGZ_NAME.get(toolsRequest.getName()));
        helmRequest.setToolsName(helmRepoName + "/" + helmRequest.getName());
        helmRequest.setToolsVersion(toolsRequest.getVersion());
        helmRequest.setNamespace(toolsRequest.getNamespace());

        String kubeconfig = clusterService.getKubeconfig(ClusterBO.builder().id(toolsRequest.getMachineId()).build());
        helmRequest.setKubeconfig(kubeconfig);

        Response<String> response = helmChaosToolsMgr.unDeployTools(helmRequest);
        if (response.isSuccess()) {
            toolsRepository.deleteByMachineIdAndName(toolsRequest.getMachineId(), toolsRequest.getName());
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
