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

package com.alibaba.chaosblade.box.service.collect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.chaosblade.box.collector.*;
import com.alibaba.chaosblade.box.collector.model.Container;
import com.alibaba.chaosblade.box.collector.model.Node;
import com.alibaba.chaosblade.box.collector.model.Pod;
import com.alibaba.chaosblade.box.collector.model.Query;
import com.alibaba.chaosblade.box.common.enums.DeviceStatus;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
import com.alibaba.chaosblade.box.common.utils.Preconditions;
import com.alibaba.chaosblade.box.common.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.box.common.utils.timer.Timer;
import com.alibaba.chaosblade.box.dao.QueryWrapperBuilder;
import com.alibaba.chaosblade.box.dao.mapper.DeviceMapper;
import com.alibaba.chaosblade.box.dao.model.ClusterDO;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceNodeDO;
import com.alibaba.chaosblade.box.dao.model.DevicePodDO;
import com.alibaba.chaosblade.box.dao.repository.ClusterRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceNodeRepository;
import com.alibaba.chaosblade.box.dao.repository.DevicePodRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;
import com.alibaba.chaosblade.box.service.model.device.ContainerBO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author yefei
 */
@Slf4j
@Component
public class CollectorTimer implements InitializingBean {

    public static final int OFFSET = -10;

    private Timer timer;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceNodeRepository deviceNodeRepository;

    @Autowired
    private DevicePodRepository devicePodRepository;

    @Autowired
    private ClusterRepository clusterRepository;

    @Value("${chaos.collector.type}")
    private String collectorType;

    @Value("${chaos.collector.enable}")
    private boolean enableCollect;

    @Value("${chaos.collector.period}")
    private Integer period;

    @Value("${chaos.collector.search.fieldSelector}")
    private String fieldSelector;

    @Value("${chaos.collector.search.labelSelector}")
    private String labelSelector;

    @Autowired
    private ApplicationContext applicationContext;

    private NodeCollector nodeCollector;

    private PodCollector podCollector;

    private ContainerCollector containerCollector;

    private ConcurrentHashMap<Long, Query> map = new ConcurrentHashMap<>(64);

    public void dryRun() throws Exception {
        Preconditions.checkNotNull(nodeCollector, "collector is null");
        CompletableFuture<List<Node>> future = nodeCollector.collect(Query.builder().build());
        log.info("collector dry run, node size: {}", future.get().size());
    }

    public void dryRun(String config) throws Exception {
        Preconditions.checkNotNull(nodeCollector, "collector is null");
        CompletableFuture<List<Node>> future = nodeCollector.collect(Query.builder().config(config).build());
        log.info("collector dry run, node size: {}", future.get().size());
    }

    public void collect(Query query) {
        map.put(query.getClusterId(), query);
        nodeCollect(nodeCollector, query);
        podCollect(podCollector, query);
        containerCollect(containerCollector, query);
    }

    public void stop(Query query) {

        Query q = map.get(query.getClusterId());
        if (q != null) {
            q.setStop(true);
        }
    }

    private void nodeCollect(NodeCollector collector, Query query) {
        timer.newTimeout(timeout -> {
            try {

                Thread.sleep(50);
                log.info("nodeCollect: collect node info ! ");

                // update ping time
                CompletableFuture<List<Node>> future = collector.collect(query);
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
                                            .clusterId(query.getClusterId())
                                            .deviceId(deviceId)
                                            .nodeIp(node.getIp())
                                            .nodeName(node.getName())
                                            .build());
                                    return aDo;
                                });

                        deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                                .ip(node.getIp())
                                .status(DeviceStatus.ONLINE.getStatus())
                                .lastOnlineTime(DateUtil.date())
                                .build());

                    });
                    return null;
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (query.getClusterId() != null) {
                    clusterRepository.updateByPrimaryKey(query.getClusterId(),
                            ClusterDO.builder().lastCollectTime(DateUtil.date()).build());
                }
            }
            if (!query.isStop()) {
                nodeCollect(collector, query);
            }
        }, period, TimeUnit.SECONDS);
    }

    private void podCollect(PodCollector collector, Query query) {
        timer.newTimeout(timeout -> {
            try {
                List<DeviceNodeDO> nodes = deviceNodeRepository.selectList(DeviceNodeDO.builder().build());
                for (DeviceNodeDO node : nodes) {
                    Query q = Query.builder().build();
                    q.setClusterId(query.getClusterId());
                    q.setConfig(query.getConfig());
                    q.setNodeName(node.getNodeName());
                    q.setFieldSelector(fieldSelector) ;
                    q.setLabelSelector(labelSelector);

                    Thread.sleep(50);
                    log.info("podCollect: collect node pods  container ; clusterId:{}, node name :{}",node.getClusterId(),node.getNodeName());

                    CompletableFuture<List<Pod>> future = collector.collect(q);
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
            } finally {
                if (query.getClusterId() != null) {
                    clusterRepository.updateByPrimaryKey(query.getClusterId(),
                            ClusterDO.builder().lastCollectTime(DateUtil.date()).build());
                }
            }
            if (!query.isStop()) {
                podCollect(collector, query);
            }
        }, period, TimeUnit.SECONDS);
    }

    private void containerCollect(ContainerCollector collector, Query query) {
        timer.newTimeout(timeout -> {

            try {
                List<DevicePodDO> devicePods = devicePodRepository.selectList(DevicePodDO.builder().build());
                for (DevicePodDO devicePod : devicePods) {
                    Query q = Query.builder().build();
                    q.setClusterId(query.getClusterId());
                    q.setConfig(query.getConfig());
                    q.setPodName(devicePod.getPodName());
                    q.setFieldSelector(fieldSelector) ;
                    q.setLabelSelector(labelSelector);

                    Thread.sleep(50);
                    log.info("containerCollect: collect pods container ; clusterId:{}, pods name :{}",query.getClusterId(),devicePod.getPodName());

                    CompletableFuture<List<Container>> future = collector.collect(q);
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
            } finally {
                if (query.getClusterId() != null) {
                    clusterRepository.updateByPrimaryKey(query.getClusterId(),
                            ClusterDO.builder().lastCollectTime(DateUtil.date()).build());
                }
            }
            if (!query.isStop()) {
                containerCollect(collector, query);
            }
        }, period, TimeUnit.SECONDS);
    }

    @Override
    public void afterPropertiesSet() {
        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("collector timer");
            return thread;
        });

        log.info("init collector timer");

        Map<String, Collector> beansOfType = applicationContext.getBeansOfType(Collector.class);
        for (Map.Entry<String, Collector> entry : beansOfType.entrySet()) {
            Collector value = entry.getValue();
            CollectorStrategy strategy = value.getClass().getAnnotation(CollectorStrategy.class);
            CollectorType collectorType = EnumUtil.fromString(CollectorType.class, this.collectorType.toUpperCase());
            if (strategy.value() == collectorType) {
                if (value instanceof NodeCollector) {
                    nodeCollector = (NodeCollector) value;
                }
                if (value instanceof PodCollector) {
                    podCollector = (PodCollector) value;
                }
                if (value instanceof ContainerCollector) {
                    containerCollector = (ContainerCollector) value;
                }
            }
        }

        // todo
        if (enableCollect) {
            ClusterDO clusterDO;
            List<ClusterDO> aDefault = clusterRepository.selectList(ClusterDO.builder().clusterName("default").build());
            if (CollUtil.isEmpty(aDefault)) {
                // init default cluster
                clusterDO = ClusterDO.builder().clusterName("default").build();
                clusterRepository.insert(clusterDO);
            } else {
                clusterDO = aDefault.get(0);
            }
            collect(Query.builder().clusterId(clusterDO.getId()).build());
        }


        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            deviceRepository.selectMachines(DeviceDO.builder()
                    .type(DeviceType.NODE.getCode())
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build())
                    .stream()
                    .filter(deviceDO -> DateUtil.date().offset(DateField.MINUTE, OFFSET).after(deviceDO.getLastOnlineTime()))
                    .forEach(deviceDO -> deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                            .status(DeviceStatus.OFFLINE.getStatus())
                            .build()));

            deviceRepository.selectMachines(DeviceDO.builder()
                    .type(DeviceType.POD.getCode())
                    .status(DeviceStatus.ONLINE.getStatus())
                    .build())
                    .stream()
                    .filter(deviceDO -> DateUtil.date().offset(DateField.MINUTE, OFFSET).after(deviceDO.getLastOnlineTime()))
                    .forEach(deviceDO -> deviceRepository.updateByPrimaryKey(deviceDO.getId(), DeviceDO.builder()
                            .status(DeviceStatus.OFFLINE.getStatus())
                            .build()));

        }, this.period, this.period, TimeUnit.SECONDS);
    }
}