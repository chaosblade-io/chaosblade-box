/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.platform.service.probes.heartbeats;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.ProbesDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.dao.repository.ProbesRepository;
import com.alibaba.chaosblade.platform.http.ChaosBladePingHttpInvoker;
import com.alibaba.chaosblade.platform.http.model.reuest.HttpChannelRequest;
import com.alibaba.chaosblade.platform.invoker.ResponseCommand;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author yefei
 */
@Component
public class Heartbeats implements InitializingBean {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ChaosBladePingHttpInvoker chaosBladeHttpInvoker;

    @Autowired
    private ProbesRepository probesRepository;

    @Value("${chaos.agent.port}")
    private int chaosAgentPort;

    private Timer timer;

    private ExecutorService executorService;

    public void addHeartbeats(ProbesDO probesDO) {
        timer.newTimeout(timeout -> {
            executorService.execute(() -> {

                HttpChannelRequest request = new HttpChannelRequest();
                request.setRequestURL("/ping");
                request.setHost(probesDO.getIp());
                request.setPort(chaosAgentPort);

                probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                        .lastPingTime(DateUtil.date())
                        .build());

                deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                        .lastPingTime(DateUtil.date())
                        .build());

                CompletableFuture<ResponseCommand> future = chaosBladeHttpInvoker.invoke(request);
                future.thenAccept(r -> {

                    probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                            .lastOnlineTime(DateUtil.date())
                            .build());

                    // OFFLINE -> ONLINE
                    probesRepository.selectById(probesDO.getId()).filter(
                            probe -> DeviceStatus.OFFLINE.getStatus() == probe.getStatus())
                            .ifPresent(probe ->
                                    probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                                            .status(DeviceStatus.ONLINE.getStatus())
                                            .build()));

                    deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                            .lastOnlineTime(DateUtil.date())
                            .build());

                    deviceRepository.selectById(probesDO.getDeviceId()).filter(
                            deviceDO -> DeviceStatus.OFFLINE.getStatus() == deviceDO.getStatus())
                            .ifPresent(deviceDO ->
                                    deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                                            .status(DeviceStatus.ONLINE.getStatus())
                                            .build()));

                }).exceptionally((e) -> {
                    // ONLINE -> OFFLINE
                    probesRepository.selectById(probesDO.getId()).filter(
                            probe -> DeviceStatus.ONLINE.getStatus() == probe.getStatus()
                                    && DateUtil.date().offset(DateField.MINUTE, -1).after(probe.getLastOnlineTime()))
                            .ifPresent(probe -> probesRepository.updateByPrimaryKey(probesDO.getId(), ProbesDO.builder()
                                    .status(DeviceStatus.OFFLINE.getStatus())
                                    .build()));

                    deviceRepository.selectById(probesDO.getDeviceId()).filter(
                            deviceDO -> DeviceStatus.ONLINE.getStatus() == deviceDO.getStatus()
                                    && DateUtil.date().offset(DateField.MINUTE, -1).after(deviceDO.getLastOnlineTime()))
                            .ifPresent(deviceDO ->
                                    deviceRepository.updateByPrimaryKey(probesDO.getDeviceId(), DeviceDO.builder()
                                            .status(DeviceStatus.OFFLINE.getStatus())
                                            .build()));
                    return null;
                });
            });

            // if status in (ONLINE, OFFLINE, FORBIDDEN) , send Heartbeats
            probesRepository.selectById(probesDO.getId()).filter(probe ->
                    DeviceStatus.ONLINE.getStatus() == probe.getStatus()
                            || DeviceStatus.OFFLINE.getStatus() == probe.getStatus()
                            || DeviceStatus.FORBIDDEN.getStatus() == probe.getStatus())
                    .ifPresent(this::addHeartbeats);

        }, 30, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() {

        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("Heartbeats");
            return thread;
        });

        executorService = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r);
            thread.setName("Heartbeats");
            return thread;
        });

        List<ProbesDO> probes = probesRepository.selectList(ProbesDO.builder().build());
        for (ProbesDO probe : probes) {
            if (DeviceStatus.ONLINE.getStatus() == probe.getStatus()
                    || DeviceStatus.OFFLINE.getStatus() == probe.getStatus()
                    || DeviceStatus.FORBIDDEN.getStatus() == probe.getStatus()) {
                addHeartbeats(probe);
            }
        }
    }
}
