package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.enums.InstallMode;
import com.alibaba.chaosblade.box.common.common.enums.ScopeTypeEnum;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.PluginType;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.EnumUtil;
import com.alibaba.chaosblade.box.dao.model.ChaosToolsDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.alibaba.chaosblade.box.dao.repository.ChaosToolsRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.alibaba.chaosblade.box.service.model.scope.ExperimentScopeFilter;
import com.alibaba.chaosblade.box.service.model.scope.ExperimentScopePageableRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ExperimentHostsSearchCommand
        extends SpringBeanCommand<ExperimentScopePageableRequest, PageableResponse<ExperimentScope>> {

    private static final String cloudAssistSupportTime = "2017-12-01";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ChaosToolsRepository chaosToolsRepository;

    @Autowired
    private ExperimentHostRelationRepository experimentHostRelationRepository;

    @Override
    public PageableResponse<ExperimentScope> execute(ExperimentScopePageableRequest experimentScopePageableRequest) {
        PageableResponse<DeviceDO> pageableResponse = findAliveMachines(experimentScopePageableRequest);
        return PageableResponse.clone(pageableResponse,
                pageableResponse.getData().stream().map(
                        this::buildExperimentScope).collect(Collectors.toList()));
    }

    public ExperimentScope buildExperimentScope(DeviceDO deviceDO) {
        ExperimentScope experimentScope = new ExperimentScope();
        experimentScope.setConfigurationId(deviceDO.getConfigurationId());
        experimentScope.setAgentVersion(deviceDO.getVersion());
        experimentScope.setAgentStatus(deviceDO.getStatus());
        experimentScope.setPrivateIp(deviceDO.getPrivateIp());
        experimentScope.setPublicIp(deviceDO.getPublicIp());
        experimentScope.setHostName(deviceDO.getDeviceName());
        experimentScope.setDeviceId(deviceDO.getDeviceId());
        experimentScope.setConnectTime(deviceDO.getConnectTime());
        experimentScope.setClusterName(deviceDO.getClusterName());
        experimentScope.setExperimentTaskCount(experimentHostRelationRepository.countExperimentTaskByHost(deviceDO.getConfigurationId()));
        experimentScope.setIsExperimented(experimentScope.getExperimentTaskCount() > 0);
        experimentScope.setOsType(deviceDO.getOsType());
        experimentScope.setPluginType(PluginType.CHAOS_AGENT.name());
        experimentScope.setPluginStatus(deviceDO.getStatus());
        experimentScope.setEnable(deviceDO.getEnable());
        experimentScope.setInstallMode(deviceDO.getInstallMode());
        canAutoInstall(experimentScope, deviceDO);
        addChaosToolsList(experimentScope, deviceDO);
        return experimentScope;
    }

    //todo. 这里应该改成如果是host,再通过ping的方式，如果能通才能返回true
    private void canAutoInstall(ExperimentScope experimentScope, DeviceDO deviceDO) {
        if (StringUtils.isBlank(deviceDO.getInstallMode()) || InstallMode.host.name().equalsIgnoreCase(deviceDO.getInstallMode())) {
            experimentScope.setCanAutoInstall(Boolean.TRUE);
        } else {
            experimentScope.setCanAutoInstall(Boolean.FALSE);
        }
    }


    private void addChaosToolsList(ExperimentScope experimentScope, DeviceDO deviceDO) {
        List<ChaosToolsDO> chaosToolsList = chaosToolsRepository.findByConfigurationId(deviceDO.getConfigurationId());
        List<String> chaosTools = new ArrayList<>();
        chaosTools.add("chaosblade");
        if (!CollectionUtil.isNullOrEmpty(chaosToolsList)) {
            chaosTools.addAll(chaosToolsList.stream().map(ChaosToolsDO::getName).collect(Collectors.toList()));
        }
        experimentScope.setChaosTools(chaosTools);
    }


    private PageableResponse<DeviceDO> findAliveMachines(
            ExperimentScopePageableRequest experimentScopePageableRequest) {
        CloudDeviceQuery cloudDeviceQuery = new CloudDeviceQuery();
        cloudDeviceQuery.setNamespace(experimentScopePageableRequest.getNamespace());
        cloudDeviceQuery.setUserId(experimentScopePageableRequest.getUserId());
        cloudDeviceQuery.setScopeType(
                EnumUtil.integerValueOf(ScopeTypeEnum.class, experimentScopePageableRequest.getScopeType()));
        if (experimentScopePageableRequest.getFilter() != null) {
            ExperimentScopeFilter experimentScopeFilter = experimentScopePageableRequest.getFilter();
            cloudDeviceQuery.setKey(experimentScopeFilter.getKey());
            if (ExperimentScopeFilter.FILTER_TYPE_EXPERIMENT.equals(experimentScopeFilter.getType())) {
                cloudDeviceQuery.setIsExperiment(true);
            }
            if (ExperimentScopeFilter.FILTER_TYPE_NOT_EXPERIMENT.equals(experimentScopeFilter.getType())) {
                cloudDeviceQuery.setIsExperiment(false);
            }
        }
        PageableQueryWrapper<CloudDeviceQuery> pageableQueryWrapper = PageableQueryWrapper.of(cloudDeviceQuery);
        pageableQueryWrapper.pageNumber(experimentScopePageableRequest.getPage());
        pageableQueryWrapper.pageSize(experimentScopePageableRequest.getSize());
        return deviceRepository.getAliveDevices(pageableQueryWrapper);
    }

}
