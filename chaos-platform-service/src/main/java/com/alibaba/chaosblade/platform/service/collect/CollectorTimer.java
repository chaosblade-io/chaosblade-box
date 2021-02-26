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

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceStatus;
import com.alibaba.chaosblade.platform.cmmon.enums.DeviceType;
import com.alibaba.chaosblade.platform.cmmon.utils.JsonUtils;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import com.alibaba.chaosblade.platform.collector.*;
import com.alibaba.chaosblade.platform.collector.model.Container;
import com.alibaba.chaosblade.platform.collector.model.Node;
import com.alibaba.chaosblade.platform.collector.model.Pod;
import com.alibaba.chaosblade.platform.collector.model.Query;
import com.alibaba.chaosblade.platform.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.platform.dao.mapper.DeviceMapper;
import com.alibaba.chaosblade.platform.dao.model.DeviceDO;
import com.alibaba.chaosblade.platform.dao.model.DeviceNodeDO;
import com.alibaba.chaosblade.platform.dao.model.DevicePodDO;
import com.alibaba.chaosblade.platform.dao.repository.DeviceNodeRepository;
import com.alibaba.chaosblade.platform.dao.repository.DevicePodRepository;
import com.alibaba.chaosblade.platform.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.platform.service.model.device.ContainerBO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Component
public class CollectorTimer implements BeanPostProcessor, InitializingBean {

    private Timer timer;

    @Autowired
    private DeviceMapper deviceMapper;

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

    @Value("${chaos.collector.period}")
    private Integer period;

    private NodeCollector nodeCollector;

    public void dryRun() throws Exception {
        CompletableFuture<List<Node>> future = nodeCollector.collect(Query.builder().build());
        log.info("collector dry run, node size: {}", future.get().size());
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof Collector && enableCollect) {
            CollectorStrategy strategy = o.getClass().getAnnotation(CollectorStrategy.class);
            CollectorType collectorType = EnumUtil.fromString(CollectorType.class, this.collectorType.toUpperCase());
            if (strategy.value() == collectorType) {
                if (o instanceof NodeCollector) {
                    nodeCollector = (NodeCollector) o;
                    nodeCollect(nodeCollector);
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
        timer.newTimeout(timeout -> {
            try {
                CompletableFuture<List<Node>> future = collector.collect(Query.builder().build());
                QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
                queryWrapper.lambda().eq(DeviceDO::getType, DeviceType.NODE.getCode());
                deviceMapper.update(DeviceDO.builder().lastPingTime(DateUtil.date()).build(), queryWrapper);

                future.handle((nodes, e) -> {
                    if (e != null) {
                        log.error("collect node fail!", e);
                        return null;
                    }
                    nodes.forEach(node -> {
                        DeviceDO deviceDO = deviceRepository.selectOneByUnique(DeviceType.NODE.getCode(), node.getName())
                                .orElseGet(() -> {
                                    DeviceDO aDo = DeviceDO.builder()
                                            .hostname(node.getName())
                                            .ip(node.getIp())
                                            .status(DeviceStatus.ONLINE.getStatus())
                                            .lastOnlineTime(DateUtil.date())
                                            .type(DeviceType.NODE.getCode())
                                            .build();
                                    Long deviceId = deviceRepository.insert(aDo);
                                    deviceNodeRepository.insert(DeviceNodeDO.builder()
                                            .deviceId(deviceId)
                                            .nodeIp(node.getIp())
                                            .nodeName(node.getName())
                                            .build());
                                    return aDo;
                                });

                        if (deviceDO.getStatus() == DeviceStatus.OFFLINE.getStatus()) {
                            deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                                    .ip(node.getIp())
                                    .status(DeviceStatus.ONLINE.getStatus())
                                    .lastOnlineTime(DateUtil.date())
                                    .build());
                        }

                    });
                    return null;
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            nodeCollect(collector);
        }, period, TimeUnit.SECONDS);
    }

    private void podCollect(PodCollector collector) {
        timer.newTimeout(timeout -> {
            try {
                List<DeviceNodeDO> nodes = deviceNodeRepository.selectList(DeviceNodeDO.builder().build());
                for (DeviceNodeDO node : nodes) {
                    CompletableFuture<List<Pod>> future = collector.collect(Query.builder()
                            .nodeName(node.getNodeName()).build());

                    QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
                    queryWrapper.lambda().eq(DeviceDO::getType, DeviceType.POD.getCode());
                    deviceMapper.update(DeviceDO.builder().lastPingTime(DateUtil.date()).build(), queryWrapper);

                    future.handle((pods, e) -> {
                        if (e != null) {
                            log.error("collect pod fail!", e);
                            return null;
                        }
                        pods.forEach(pod -> {
                            Long deviceId = devicePodRepository.selectByNameAndNamespace(pod.getNamespace(), pod.getName())
                                    .map(DevicePodDO::getDeviceId)
                                    .orElseGet(() -> {
                                        // insert device pod
                                        Long id = deviceRepository.insert(DeviceDO.builder()
                                                .hostname(pod.getName())
                                                .ip(pod.getIp())
                                                .status(DeviceStatus.ONLINE.getStatus())
                                                .lastOnlineTime(DateUtil.date())
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

                            if (deviceMapper.selectById(deviceId).getStatus() == DeviceStatus.OFFLINE.getStatus()) {
                                deviceRepository.updateByPrimaryKey(deviceId, DeviceDO.builder()
                                        .status(DeviceStatus.ONLINE.getStatus())
                                        .lastOnlineTime(DateUtil.date())
                                        .build());
                            }
                        });
                        return null;
                    });
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            podCollect(collector);
        }, period, TimeUnit.SECONDS);
    }

    private void containerCollect(ContainerCollector collector) {
        timer.newTimeout(timeout -> {
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
        }, period, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            deviceRepository.selectMachines(DeviceDO.builder()
                    .type(DeviceType.NODE.getCode())
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build())
                    .stream()
                    .filter(deviceDO -> DateUtil.date().offset(DateField.MINUTE, -1).after(deviceDO.getLastOnlineTime()))
                    .forEach(deviceDO -> deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                            .status(DeviceStatus.OFFLINE.getStatus())
                            .build()));

            deviceRepository.selectMachines(DeviceDO.builder()
                    .type(DeviceType.POD.getCode())
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build())
                    .stream()
                    .filter(deviceDO -> DateUtil.date().offset(DateField.MINUTE, -1).after(deviceDO.getLastOnlineTime()))
                    .forEach(deviceDO -> deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                            .status(DeviceStatus.OFFLINE.getStatus())
                            .build()));

        }, 30, 30, TimeUnit.SECONDS);

        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("collector timer");
            return thread;
        });
    }
}
