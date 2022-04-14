package com.alibaba.chaosblade.box.service.command.agent;


import com.alibaba.chaosblade.box.common.commands.CommandExecutorConstant;
import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.infrastructure.constant.DeviceStatus;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.PrivateCloudDeviceRepository;
import com.alibaba.chaosblade.box.service.model.agent.AgentClosedCallbackRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author haibin
 *
 * 
 */
@Component
@Slf4j
public class AgentClosedCommand extends SpringBeanCommand<AgentClosedCallbackRequest, Response<Boolean>> {

    private static Logger logger = LoggerFactory.getLogger("agent-close");

    @Autowired
    private PrivateCloudDeviceRepository deviceRepository;

    @Override
    public String getCommandExecutorName() {
        return CommandExecutorConstant.EXECUTOR_DEFAULT;
    }

    @Override
    public Response<Boolean> execute(AgentClosedCallbackRequest agentClosedCallbackRequest) {
        log.info("start handle client closed request,{}" + JSON.toJSONString(agentClosedCallbackRequest));
        try {
            Preconditions.checkArgument(agentClosedCallbackRequest.getUserId() != null, "uid Required");
            Preconditions.checkArgument(agentClosedCallbackRequest.getConfigurationId() != null, "cid Required");
            String userId = agentClosedCallbackRequest.getUserId();
            deviceRepository.findByUserIdAndDeviceTypeAndConfigurationId(userId, DeviceType.HOST.getType(),
                agentClosedCallbackRequest.getConfigurationId()).forEach(new Consumer<DeviceDO>() {
                @Override
                public void accept(DeviceDO deviceDO) {
                    deviceDO.setStatus(DeviceStatus.WAIT_INSTALL.getStatus());
                    deviceRepository.updateDeviceById(deviceDO);
                }
            });
            return Response.ofSuccess(true);
        } catch (Exception ex) {
            logger.error("execute agent closed request failed", ex);
            return Response.ofFailure(Response.Code.SERVER_ERROR, "execute agent closed request failed");
        }

    }

}
