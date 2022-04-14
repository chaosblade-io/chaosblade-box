package com.alibaba.chaosblade.box.service.command.cluster;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.annotation.KubernetesExtensionPoint;
import com.alibaba.chaosblade.box.common.infrastructure.constant.ApplicationDimension;
import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesDataQuery;
import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesPod;
import com.alibaba.chaosblade.box.dao.model.ApplicationDeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ApplicationDeviceQuery;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.KubernetesDataRepository;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.service.model.agent.ExperimentClusterQueryRequest;
import com.alibaba.chaosblade.box.service.model.cluster.ExperimentPodVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
@KubernetesExtensionPoint
public class ExperimentPodsSearchCommand extends
        SpringBeanCommand<ExperimentClusterQueryRequest, PageableResponse<ExperimentPodVO>> {

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Autowired
    private KubernetesDataRepository kubernetesDataRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public PageableResponse<ExperimentPodVO> execute(ExperimentClusterQueryRequest experimentClusterQueryRequest) {
        String nodeConfigurationId = experimentClusterQueryRequest.getNodeConfigurationId();
        DeviceDO deviceDO = deviceRepository.findByConfigurationId(nodeConfigurationId);
        if (deviceDO == null) {
            return PageableResponse.empty();
        }
        if (!CloudHostUtil.isK8sInstallMode(deviceDO.getInstallMode())) { return PageableResponse.empty(); }
        ApplicationDeviceQuery applicationDeviceQuery = new ApplicationDeviceQuery();
        applicationDeviceQuery.setHostConfigurationId(nodeConfigurationId);
        applicationDeviceQuery.setPartName(experimentClusterQueryRequest.getKey());
        applicationDeviceQuery.setDimensions(Lists.newArrayList(ApplicationDimension.POD.getValue()));
        IPage<ApplicationDeviceDO> applicationDeviceDOIPage = applicationDeviceRepository.searchLiveDevice(
            applicationDeviceQuery,
            experimentClusterQueryRequest.getPage(), experimentClusterQueryRequest.getSize());
        return PageableResponse.of(applicationDeviceDOIPage.getCurrent(), applicationDeviceDOIPage.getSize(),
            applicationDeviceDOIPage.getRecords().stream().map(applicationDeviceDO -> {
                ExperimentPodVO experimentPodVO = new ExperimentPodVO();
                experimentPodVO.setAppId(applicationDeviceDO.getAppId());
                experimentPodVO.setPodIp(applicationDeviceDO.getPrivateIp());
                experimentPodVO.setAppName(applicationDeviceDO.getAppName());
                experimentPodVO.setPodName(applicationDeviceDO.getDeviceName());
                String kubNamespace = applicationDeviceDO.getKubNamespace();
                if (Strings.isNullOrEmpty(kubNamespace)) {
                    kubNamespace = getKubNamespace(experimentClusterQueryRequest.getUserId(), deviceDO.getClusterId(),
                        applicationDeviceDO.getConfigurationId(), applicationDeviceDO.getNamespace());
                }
                experimentPodVO.setHeartTime(applicationDeviceDO.getLastHealthPingTime());
                experimentPodVO.setKubNamespace(kubNamespace);
                return experimentPodVO;
            }).collect(Collectors.toList()), applicationDeviceDOIPage.getPages(), applicationDeviceDOIPage.getTotal());
    }

    private String getKubNamespace(String userId, String clusterId, String configurationId, String namespace) {
        Optional<KubernetesPod> kubernetesPodOptional = kubernetesDataRepository.findPodInfo(
            new KubernetesDataQuery(userId, clusterId, configurationId, namespace, null, null));
        return kubernetesPodOptional.map(KubernetesPod::getKubNamespace).orElse(null);
    }
}
