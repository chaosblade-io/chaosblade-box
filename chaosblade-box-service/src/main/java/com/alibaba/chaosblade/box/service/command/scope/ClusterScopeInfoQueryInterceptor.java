package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.common.annotation.KubernetesExtensionPoint;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.ApplicationDeviceRepository;
import com.alibaba.chaosblade.box.dao.infrastructure.utils.CloudHostUtil;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterInfo;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin.lhb
 *
 *
 */
@KubernetesExtensionPoint
public class ClusterScopeInfoQueryInterceptor implements ScopeInfoQueryInterceptor {

    @Autowired
    private ApplicationDeviceRepository applicationDeviceRepository;

    @Override
    public void doQuery(ScopeInfo nodeScopeInfo, DeviceDO nodeDeviceDO) {
        if (!CloudHostUtil.isK8sInstallMode(nodeDeviceDO.getInstallMode())) { return; }
        ClusterInfo clusterInfo = new ClusterInfo();
        clusterInfo.setClusterName(nodeDeviceDO.getClusterName());
        nodeScopeInfo.setClusterInfo(clusterInfo);
        clusterInfo.setPodCount(
            applicationDeviceRepository.countDeviceByClusterNode(nodeDeviceDO.getConfigurationId()));
    }
}
