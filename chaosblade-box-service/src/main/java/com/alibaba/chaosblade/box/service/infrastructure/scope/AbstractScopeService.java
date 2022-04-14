package com.alibaba.chaosblade.box.service.infrastructure.scope;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.dao.mapper.HostExperimentMapper;
import com.alibaba.chaosblade.box.dao.infrastructure.entity.ExperimentTaskEntity;
import com.alibaba.chaosblade.box.service.infrastructure.service.ScopeService;
import com.alibaba.chaosblade.box.service.model.param.HostExperimentTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
public abstract class AbstractScopeService implements ScopeService {

    @Autowired
    private HostExperimentMapper hostExperimentMapper;

    @Override
    public List<HostExperimentTask> checkHostInRunningExperiment(Host host) {
        if (host == null || host.getIp() == null) { return new ArrayList<>(); }
        return hostExperimentMapper.queryExperimentTaskIdByHost(host.getIp()).stream().map(
            new Function<ExperimentTaskEntity, HostExperimentTask>() {
                @Override
                public HostExperimentTask apply(ExperimentTaskEntity experimentTaskEntity) {
                    HostExperimentTask hostExperimentTask = new HostExperimentTask();
                    hostExperimentTask.setExperimentTaskId(experimentTaskEntity.getExperimentTaskId());
                    hostExperimentTask.setUser(experimentTaskEntity.getUserId());
                    return hostExperimentTask;
                }
            }).collect(Collectors.toList());
    }
}
