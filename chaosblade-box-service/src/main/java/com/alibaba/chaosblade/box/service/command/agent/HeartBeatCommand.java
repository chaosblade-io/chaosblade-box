package com.alibaba.chaosblade.box.service.command.agent;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.infrastructure.DomainFactory;
import com.alibaba.chaosblade.box.service.command.scope.PrivateScope;
import com.alibaba.chaosblade.box.service.model.agent.HeartBeatCallbackRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class HeartBeatCommand extends SpringBeanCommand<HeartBeatCallbackRequest, Response<Boolean>> implements
    InitializingBean {

    private static Logger logger = LoggerFactory.getLogger("heart-beat");

    @Autowired
    private DomainFactory domainFactory;

    @Override
    public Response<Boolean> execute(HeartBeatCallbackRequest heartBeatCallbackRequest) {
        StopWatch stopWatch = StopWatch.createStarted();
        boolean success = false;
        try {
            String configurationId = heartBeatCallbackRequest.getConfigurationId();
            if (logger.isDebugEnabled()) {
                log.debug("start handle client heartbeat request,{}" + JSON.toJSONString(heartBeatCallbackRequest));
                logger.debug("handle update for configuration id:" + configurationId);
            }
            Preconditions.checkArgument(configurationId != null, "cid Required");
            success = true;
            return Response.ofSuccess(
                domainFactory.getBean(new PrivateScope(configurationId)).updateHeartBeatTime(heartBeatCallbackRequest));
        } catch (Exception ex) {
            logger.error("execute heartbeat request failed", ex);
            success = false;
            return Response.ofFailure(Response.Code.SERVER_ERROR, "execute heartbeat request failed,:" + ex.getMessage());
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
