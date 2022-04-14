package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Scope;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.domain.scope.ScopeQuery;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.CloudDeviceQuery;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.CloudScopeService;
import com.alibaba.chaosblade.box.service.ScopeService;
import com.alibaba.chaosblade.box.service.command.scope.*;
import com.alibaba.chaosblade.box.service.infrastructure.scope.AbstractScopeService;
import com.alibaba.chaosblade.box.service.model.device.CloudDevice;
import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import com.alibaba.chaosblade.box.service.model.scope.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Component
public class CloudScopeServiceImpl extends AbstractScopeService implements ScopeService, CloudScopeService {

    @Resource
    private DeviceRepository deviceRepository;

    @Autowired
    private CommandBus commandBus;

    @SuppressWarnings("unchecked")
    @Override
    public List<CloudDevice> queryAliveScopes(ScopeQuery query) {
        CloudDeviceQuery deviceQuery = (CloudDeviceQuery)query;
        return deviceRepository.getAliveDevices(deviceQuery)
            .stream()
            .map(CloudDevice::from)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Scope, Q extends ScopeQuery> PageableResponse<T> queryAliveScopesByPage(ChaosUser user,
                                                                                              PageableQueryWrapper<Q> pageableQueryWrapper, String nameSpace) {
        PageableResponse<DeviceDO> pageableResponse = deviceRepository.getAliveDevicesByKeyAndTags(
                (PageableQueryWrapper<CloudDeviceQuery>)pageableQueryWrapper);
        return (PageableResponse<T>)PageableResponse.clone(
                pageableResponse,
                pageableResponse.data()
                        .stream()
                        .map(CloudDevice::from)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Response<String[]> checkProcessExistByName(Host host, String s) {
        return null;
    }

    @Override
    public Response<String[]> checkProcessExistByPid(Host host, String s) {
        return null;
    }

    @Override
    public Response<ScopeInfo> findOneScope(ScopeInfoQueryRequest scopeQueryRequest) {
        return commandBus.syncRun(ScopeInfoQueryCommand.class, scopeQueryRequest);
    }

    @Override
    public Response<PageableResponse<ExperimentScope>> pageableQueryExperimentScopes(
        ExperimentScopePageableRequest experimentScopePageableRequest) {
        return Response.okWithData(
            commandBus.syncRun(ExperimentHostsSearchCommand.class, experimentScopePageableRequest));
    }

    @Override
    public Response<List<ExperimentScopeInvokeCount>> countExperimentScopeInvocation(
        ScopeInfoQueryRequest scopeQueryRequest) {
        return Response.okWithData(
            commandBus.syncRun(ExperimentScopeInvokeCountCommand.class, scopeQueryRequest));
    }

    @Override
    public Response<List<FunctionInvocationCount>> countExperimentScopeSceneFunctionCount(
        ScopeInfoQueryRequest scopeQueryRequest) {
        return Response.okWithData(
            commandBus.syncRun(ExperimentScopeSceneFunctionCountCommand.class, scopeQueryRequest));
    }

    @Override
    public Response<PageableResponse<ScopeExperimentTask>> pageableQueryExperimentTaskByScope(
        PageableScopeExperimentTaskQueryRequest scopeInfoQueryRequest) {
        return Response.okWithData(
            commandBus.syncRun(PageableQueryExperimentTaskByScopeCommand.class, scopeInfoQueryRequest));
    }

}
