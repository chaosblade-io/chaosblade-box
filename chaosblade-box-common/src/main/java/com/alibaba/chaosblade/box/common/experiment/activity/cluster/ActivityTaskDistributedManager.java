package com.alibaba.chaosblade.box.common.experiment.activity.cluster;

import com.alibaba.chaosblade.box.common.infrastructure.util.FastJsonDecoder;
import com.google.common.base.Strings;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author haibin
 *
 *
 */
@Component
@Slf4j
public class ActivityTaskDistributedManager {

    @Value("${server.port}")
    private int serverPort;

    public void sendImmediatelyShutdownSignal(String targetHost, String experimentTaskId) {
        try {
            if (Strings.isNullOrEmpty(targetHost)) { return; }
            buildTransport(targetHost).shutdownTask(experimentTaskId);
        } catch (Throwable throwable) {
            log.error("activity remote request failed", throwable);

        }
    }

    public ExecutionJobTransport buildTransport(String host) {
        String url = "http://" + host + ":" + serverPort;
        return Feign.builder().decoder(new FastJsonDecoder())
            .decoder(new FastJsonDecoder())
            .target(ExecutionJobTransport.class, url);
    }

    public boolean isRunning(String targetHost, String activityTaskId) {
        try {
            if (Strings.isNullOrEmpty(targetHost)) { return false; }
            return buildTransport(targetHost).isActivityTaskRunning(activityTaskId).isSuccess();
        } catch (Throwable throwable) {
            log.error("activity remote request failed", throwable);
            return false;
        }
    }

}
