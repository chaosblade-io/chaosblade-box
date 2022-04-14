package com.alibaba.chaosblade.box.service.command.agent;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.infrastructure.DomainFactory;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.command.scope.PrivateScope;
import com.alibaba.chaosblade.box.service.infrastructure.UserApplicationRegister;
import com.alibaba.chaosblade.box.service.model.agent.ChaosAgentRegisterResultEntity;
import com.alibaba.chaosblade.box.service.model.agent.RegisteredCallbackRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class RegisteredRequestCommand
    extends SpringBeanCommand<RegisteredCallbackRequest, Response<ChaosAgentRegisterResultEntity>> {

    private static Logger LOGGER = LoggerFactory.getLogger("agent-register");

    @Autowired
    private DomainFactory domainFactory;

    @Autowired
    private UserApplicationRegister userApplicationRegister;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Response<ChaosAgentRegisterResultEntity> execute(RegisteredCallbackRequest registeredCallbackRequest) {
        log.info("start handle client register request,{}" + JSON.toJSONString(registeredCallbackRequest));
        try {
            // 1. check parameter license
            if (Strings.isNullOrEmpty(registeredCallbackRequest.getAk())) {
                return Response.ofFailure(Response.Code.Parameter_Empty, "license Required");
            }
            // 2. get userId by license
            String userId = userApplicationRegister.getUserId(registeredCallbackRequest.getUserId(), registeredCallbackRequest.getAk());
            if (Strings.isNullOrEmpty(userId)){
                return Response.ofFailure(Response.Code.Parameter_Empty, "uid illegal");
            }
            registeredCallbackRequest.setUserId(userId);

            // 3. check namespace
            if (StringUtils.isBlank(registeredCallbackRequest.getNamespace()) || !userApplicationRegister.checkNamespace(userId, registeredCallbackRequest.getNamespace())){
                return Response.ofFailure(Response.Code.Parameter_Empty, "namespace illegal");
            }

            // 4. register agent
            DeviceDO requestDeviceDO = domainFactory.getBean(PrivateScope.class).register(registeredCallbackRequest);
            Long appId = userApplicationRegister.registerApplicationByHost(requestDeviceDO,
                registeredCallbackRequest.getAppName(),
                registeredCallbackRequest.getAppGroup());
            if (appId == null) {
                return Response.ofFailure(Response.Code.SERVER_ERROR, "register application failed");
            }
            ChaosAgentRegisterResultEntity chaosAgentRegisterResultEntity = new ChaosAgentRegisterResultEntity();
            chaosAgentRegisterResultEntity.setConfigurationId(requestDeviceDO.getConfigurationId());
            chaosAgentRegisterResultEntity.setAk(registeredCallbackRequest.getAk());
            chaosAgentRegisterResultEntity.setSk(registeredCallbackRequest.getUserId());
            chaosAgentRegisterResultEntity.setUid(registeredCallbackRequest.getUserId());
            return Response.ofSuccess(chaosAgentRegisterResultEntity);
        } catch (Exception ex) {
            LOGGER.error("execute agent register request failed", ex);
            return Response.ofFailure(Response.Code.SERVER_ERROR, "execute agent register request failed:" + ex.getMessage());
        }
    }
}
