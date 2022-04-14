package com.alibaba.chaosblade.box.service.command.cluster;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.command.scope.ExperimentHostsSearchCommand;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.agent.ExperimentClusterQueryRequest;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author haibin.lhb
 *
 *
 */
@Component
public class ExperimentNodesQueryCommand
    extends SpringBeanCommand<ExperimentClusterQueryRequest, PageableResponse<ExperimentScope>> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ExperimentHostsSearchCommand experimentHostsSearchCommand;

    @Override
    public PageableResponse<ExperimentScope> execute(ExperimentClusterQueryRequest experimentClusterQueryRequest) {
        String clusterId = experimentClusterQueryRequest.getClusterId();
        if (Strings.isNullOrEmpty(clusterId)) {
            return PageableResponse.empty();
        }
        IPage<DeviceDO> deviceDOIPage = deviceRepository.getDevicesByClusterId(clusterId,
            experimentClusterQueryRequest.getPage(), experimentClusterQueryRequest.getSize());
        return PageableResponse.of(deviceDOIPage.getCurrent(), deviceDOIPage.getSize(),
            deviceDOIPage.getRecords().stream()
                .map(deviceDO -> experimentHostsSearchCommand.buildExperimentScope(deviceDO)).collect(
                Collectors.toList()), deviceDOIPage.getPages(), deviceDOIPage.getTotal());
    }
}
