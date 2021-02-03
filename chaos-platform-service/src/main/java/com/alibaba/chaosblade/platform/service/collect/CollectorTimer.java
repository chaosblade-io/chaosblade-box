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

package com.alibaba.chaosblade.platform.service.collect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.collector.*;
import com.alibaba.chaosblade.platform.collector.model.Container;
import com.alibaba.chaosblade.platform.collector.model.Node;
import com.alibaba.chaosblade.platform.collector.model.Pod;
import com.alibaba.chaosblade.platform.collector.model.Query;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.DeviceNodeDO;
import com.alibaba.chaosblade.platform.dao.model.DevicePodDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceNodeRepository;
import com.alibaba.chaosblade.platform.dao.repository.DevicePodRepository;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.service.model.device.ContainerBO;
import com.alibaba.chaosblade.platform.service.task.TimerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Component
public class CollectorTimer implements BeanPostProcessor {

    @Autowired
    private TimerFactory timerFactory;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceNodeRepository deviceNodeRepository;

    @Autowired
    private DevicePodRepository devicePodRepository;

    @Value("${chaos.collector.type}")
    private String collectorType;

    @Value("${chaos.collector.enable}")
    private boolean enableCollect;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof Collector && enableCollect) {
            CollectorStrategy strategy = o.getClass().getAnnotation(CollectorStrategy.class);
            CollectorType collectorType = EnumUtil.fromString(CollectorType.class, this.collectorType.toUpperCase());
            if (strategy.value() == collectorType) {
                if (o instanceof NodeCollector) {
                    nodeCollect((NodeCollector) o);
                }
                if (o instanceof PodCollector) {
                    podCollect((PodCollector) o);
                }
                if (o instanceof ContainerCollector) {
                    containerCollect((ContainerCollector) o);
                }
            }
        }
        return o;
    }

    private void nodeCollect(NodeCollector collector) {
        timerFactory.getTimer().newTimeout(timeout -> {
            try {
                CompletableFuture<List<Node>> future = collector.collect(Query.builder().build());
                future.handle((r, e) -> {
                    if (e != null) {
                        log.error("collect node fail!", e);
                        return null;
                    }
                    r.forEach(node -> {
                        Long aLong = deviceRepository.selectOneByUnique(DeviceType.NODE.getCode(), node.getName())
                                .map(DeviceDO::getId)
                                .orElseGet(() -> {
                                    Long deviceId = deviceRepository.insert(DeviceDO.builder()
                                            .hostname(node.getName())
                                            .type(DeviceType.NODE.getCode())
                                            .build());
                                    deviceNodeRepository.insert(DeviceNodeDO.builder()
                                            .deviceId(deviceId)
                                            .nodeName(node.getName())
                                            .build());
                                    return deviceId;
                                });
                        deviceRepository.updateByPrimaryKey(aLong, DeviceDO.builder()
                                .status(DeviceStatus.ONLINE.getStatus())
                                .lastOnlineTime(DateUtil.date())
                                .build());

                    });
                    return null;
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            nodeCollect(collector);
        }, 10, TimeUnit.SECONDS);
    }

    private void podCollect(PodCollector collector) {
        timerFactory.getTimer().newTimeout(timeout -> {
            try {
                List<DeviceNodeDO> nodes = deviceNodeRepository.selectList(DeviceNodeDO.builder().build());
                for (DeviceNodeDO node : nodes) {
                    CompletableFuture<List<Pod>> future = collector.collect(Query.builder()
                            .nodeName(node.getNodeName()).build());
                    future.handle((r, e) -> {
                        if (e != null) {
                            log.error("collect pod fail!", e);
                            return null;
                        }
                        r.forEach(pod -> {
                            Long deviceId = devicePodRepository.selectByNameAndNamespace(pod.getNamespace(), pod.getName())
                                    .map(DevicePodDO::getDeviceId)
                                    .orElseGet(() -> {
                                        // insert device pod
                                        Long id = deviceRepository.insert(DeviceDO.builder()
                                                .hostname(pod.getName())
                                                .ip(pod.getIp())
                                                .type(DeviceType.POD.getCode())
                                                .build());

                                        devicePodRepository.insert(DevicePodDO.builder()
                                                .nodeId(node.getId())
                                                .namespace(pod.getNamespace())
                                                .podName(pod.getName())
                                                .podIp(pod.getIp())
                                                .deviceId(id)
                                                .build());
                                        return id;
                                    });

                            deviceRepository.updateByPrimaryKey(deviceId, DeviceDO.builder()
                                    .status(DeviceStatus.ONLINE.getStatus())
                                    .lastOnlineTime(DateUtil.date())
                                    .build());

                        });
                        return null;
                    });
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            podCollect(collector);
        }, 10, TimeUnit.SECONDS);
    }

    private void containerCollect(ContainerCollector collector) {
        timerFactory.getTimer().newTimeout(timeout -> {
            try {
                List<DevicePodDO> devicePods = devicePodRepository.selectList(DevicePodDO.builder().build());
                for (DevicePodDO devicePod : devicePods) {
                    CompletableFuture<List<Container>> future = collector.collect(Query.builder()
                            .podName(devicePod.getPodName()).build());
                    future.handle((containers, e) -> {
                        if (e != null) {
                            log.error("collect container fail!", e);
                            return null;
                        }
                        List<ContainerBO> list = containers.stream().map(container ->
                                ContainerBO.builder()
                                        .containerId(container.getContainerId())
                                        .containerName(container.getName())
                                        .build()
                        ).collect(Collectors.toList());

                        devicePodRepository.updateByPrimaryKey(devicePod.getId(),
                                DevicePodDO.builder().containers(JsonUtils.writeValueAsString(list)).build());
                        return null;
                    });
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            containerCollect(collector);
        }, 10, TimeUnit.SECONDS);
    }
}
