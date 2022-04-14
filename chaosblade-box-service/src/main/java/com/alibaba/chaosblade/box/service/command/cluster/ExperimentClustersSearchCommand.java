package com.alibaba.chaosblade.box.service.command.cluster;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.annotation.KubernetesExtensionPoint;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.model.ChaosToolsDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosToolsRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.command.scope.ExperimentHostsSearchCommand;
import com.alibaba.chaosblade.box.service.model.agent.ExperimentClusterQueryRequest;
import com.alibaba.chaosblade.box.service.model.cluster.ExperimentClusterVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询演练集群
 *
 * @author haibin.lhb
 *
 * 
 */
@KubernetesExtensionPoint
public class ExperimentClustersSearchCommand extends
        SpringBeanCommand<ExperimentClusterQueryRequest, List<ExperimentClusterVO>> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ChaosToolsRepository chaosToolsRepository;

    private Integer defaultNodeSize = 10;

    @Autowired
    private ExperimentHostsSearchCommand experimentHostsSearchCommand;

//    @Resource
//    private AgentVersionManager agentVersionManager;

    @Override
    public List<ExperimentClusterVO> execute(ExperimentClusterQueryRequest experimentClusterQueryRequest) {
        List<String> clusterIds = deviceRepository.getOnlineClusterIdsByUserId(
            experimentClusterQueryRequest.getUserId(),experimentClusterQueryRequest.getNamespace());
        List<ExperimentClusterVO> experimentClusterVOS = new ArrayList<>();
        for (String clusterId : clusterIds) {
            ExperimentClusterVO experimentClusterVO = buildExperimentClusterVO(clusterId);
            experimentClusterVOS.add(experimentClusterVO);
        }
        return experimentClusterVOS;
    }

    private ExperimentClusterVO buildExperimentClusterVO(String clusterId) {
        ExperimentClusterVO experimentClusterVO = new ExperimentClusterVO();
        experimentClusterVO.setClusterId(clusterId);
        IPage<DeviceDO> deviceDOIPage = deviceRepository.getDevicesByClusterId(clusterId, 0, defaultNodeSize);
        DeviceDO deviceDO = deviceDOIPage.getRecords().get(0);
        experimentClusterVO.setInstallMode("k8s");
        experimentClusterVO.setClusterName(deviceDO.getClusterName());
        experimentClusterVO.setNodeCount(deviceDOIPage.getTotal());
        experimentClusterVO.setOnlineCount(deviceDOIPage.getTotal());
        experimentClusterVO.setPartNodes(
            deviceDOIPage.getRecords().stream().map(
                deviceDO1 -> experimentHostsSearchCommand.buildExperimentScope(deviceDO1)).collect(Collectors.toList()));
        Set<String> agentVersions = deviceDOIPage.getRecords().stream().map(DeviceDO::getVersion).collect(
            Collectors.toSet());
        experimentClusterVO.setAgentConsistency(agentVersions.size() == 1);

        List<ChaosToolsDO> chaosToolsList = chaosToolsRepository.findByConfigurationId(deviceDO.getConfigurationId());
        List<String> chaosTools = new ArrayList<>();
        chaosTools.add("chaosblade");
        if(!CollectionUtil.isNullOrEmpty(chaosToolsList)) {
            chaosTools.addAll(chaosToolsList.stream().map(ChaosToolsDO::getName).collect(Collectors.toList()));
        }
        experimentClusterVO.setChaosTools(chaosTools);

        if (DeviceType.HOST.getType() == deviceDO.getDeviceType()) {
            experimentClusterVO.setPluginType(PluginType.CHAOS_AGENT.name());
        }
        if (DeviceType.HOST_POD.getType() == deviceDO.getDeviceType()) {
            experimentClusterVO.setPluginType(PluginType.CHAOS_POD_AGENT.name());
        }
        experimentClusterVO.setVersion(deviceDO.getVersion());
        experimentClusterVO.setUpgrade(false);
        return experimentClusterVO;
    }
}
