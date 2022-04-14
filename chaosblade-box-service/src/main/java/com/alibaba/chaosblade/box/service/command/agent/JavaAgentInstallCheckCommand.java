package com.alibaba.chaosblade.box.service.command.agent;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.service.command.task.JavaAgentInstallCallback;
import com.alibaba.chaosblade.box.dao.model.ChaosBladeExpUidDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentMiniAppTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ChaosBladeExpUidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
public class JavaAgentInstallCheckCommand extends SpringBeanCommand<ExperimentMiniAppTaskDO, Boolean> {

    @Autowired
    private ChaosBladeExpUidRepository chaosBladeExpUidRepository;

    @Autowired
    private JavaAgentInstallCallback javaAgentInstallCallback;

    @Override
    public Boolean execute(ExperimentMiniAppTaskDO experimentMiniAppTaskDO) {
        Host host = new Host();
        host.setIp(experimentMiniAppTaskDO.getHostIp());
        host.setDeviceConfigurationId(experimentMiniAppTaskDO.getDeviceConfigurationId());
        ChaosBladeExpUidDO chaosBladeExpUidDO = chaosBladeExpUidRepository.findByActivityTargetTaskId(
            experimentMiniAppTaskDO.getTaskId());
        if (chaosBladeExpUidDO == null) { return false; }
        if (chaosBladeExpUidDO.getExpired()) {
            return false;
        }
        javaAgentInstallCallback.execute(experimentMiniAppTaskDO, chaosBladeExpUidDO, null);
        return null;
    }

}
