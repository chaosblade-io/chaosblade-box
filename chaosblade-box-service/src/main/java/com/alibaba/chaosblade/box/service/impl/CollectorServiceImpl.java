package com.alibaba.chaosblade.box.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.chaosblade.box.collector.model.Container;
import com.alibaba.chaosblade.box.collector.model.Pod;
import com.alibaba.chaosblade.box.collector.model.Query;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.common.utils.JsonUtils;
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
import com.alibaba.chaosblade.box.service.CollectorService;
import com.alibaba.chaosblade.box.service.collect.AbstractCollect;
import com.alibaba.chaosblade.box.service.model.device.ContainerBO;
import com.alibaba.chaosblade.box.service.model.device.SyncContainerRequest;
import com.alibaba.chaosblade.box.service.model.device.SyncContainerResponse;
import com.alibaba.chaosblade.box.service.model.device.SyncPodRequest;
import com.alibaba.chaosblade.box.service.model.device.SyncPodResponse;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author hengyu
 * @version 1.0.0
 * @className CollectorServiceImpl
 * @createTime 2021/11/23 10:39:00
 * @description
 */

@Service
@Slf4j
public class CollectorServiceImpl extends AbstractCollect implements CollectorService, InitializingBean {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceRepository deviceRepository;

    @Resource
    private DeviceNodeRepository deviceNodeRepository;

    @Resource
    private DevicePodRepository devicePodRepository;

    @Resource
    private ClusterRepository clusterRepository;

    @Value("${chaos.collector.search.fieldSelector}")
    private String fieldSelector;

    @Value("${chaos.collector.search.labelSelector}")
    private String labelSelector;

    @Override
    public SyncPodResponse syncPodResource(SyncPodRequest request) {
        try {
            List<ClusterDO> clusterDoList = clusterRepository.selectList(ClusterDO.builder().build());
            AtomicReference<ClusterDO> cluster = new AtomicReference<>();
            List<Optional<DeviceNodeDO>> deviceNodeList = clusterDoList.stream().map((clusterDO) -> {
                Long id = clusterDO.getId();
                Optional<DeviceNodeDO> deviceNodeDO = deviceNodeRepository.selectByNodeName(id, request.getNodeName());
                if (deviceNodeDO.isPresent()) {
                    cluster.set(clusterDO);
                    return deviceNodeDO;
                } else {
                    return Optional.<DeviceNodeDO>empty();
                }
            }).filter(Optional::isPresent).collect(Collectors.toList());

            if (cluster.get() == null || deviceNodeList.size() != 1) {
                log.debug("deviceNodeList size :{}", deviceNodeList.size());
                return SyncPodResponse.builder().success(false).podNum(0).build();
            }

            Optional<DeviceNodeDO> deviceNodeOptional = deviceNodeList.get(0);
            if (!deviceNodeOptional.isPresent()) {
                return SyncPodResponse.builder().success(false).podNum(0).build();
            }
            DeviceNodeDO deviceNode = deviceNodeOptional.get();

            CompletableFuture<List<Pod>> future
                = collectPodsList(request, cluster, deviceNode);

            updateNodePingTime();

            CompletableFuture<SyncPodResponse> handle = future.handle((pods, e) -> {
                if (e != null) {
                    log.error("collect pod fail!", e);
                    return null;
                }
                List<String> list = new ArrayList<>();
                SyncPodResponse result = SyncPodResponse.builder().podNum(pods.size()).podList(list).build();
                pods.forEach(pod -> {
                    list.add(pod.getName());
                    syncDeviceAndPodInfo(deviceNode, pod, devicePodRepository, deviceRepository);
                });
                result.setSuccess(true);
                result.setPodList(list);
                return result;
            });
            return handle.get();
        } catch (Exception e) {
            log.error("syncPodResource exception !", e);
            e.printStackTrace();
        }

        return null;
    }


    private void updateNodePingTime() {
        QueryWrapper<DeviceDO> queryWrapper = QueryWrapperBuilder.build();
        queryWrapper.lambda().eq(DeviceDO::getType, DeviceType.POD.getCode());
        deviceMapper.update(DeviceDO.builder().lastPingTime(DateUtil.date()).build(), queryWrapper);
    }

    private CompletableFuture<List<Pod>> collectPodsList(SyncPodRequest request,
        AtomicReference<ClusterDO> cluster, DeviceNodeDO deviceNode) {

        ClusterDO clusterDO = cluster.get();
        Query q = Query.builder().build();
        q.setClusterId(deviceNode.getClusterId());
        q.setConfig(clusterDO.getConfig());
        q.setNodeName(request.getNodeName());
        q.setFieldSelector(fieldSelector);
        q.setLabelSelector(labelSelector);

        return podCollector.collect(q);
    }

    @Override
    public SyncContainerResponse syncContainerResource(SyncContainerRequest request) {
        try {

            Optional<DevicePodDO> devicePodDO = devicePodRepository.selectByNameAndNamespace(request.getNamespace(),
                request.getPodName());

            if (!devicePodDO.isPresent()) {
                return SyncContainerResponse.builder().success(false).containerNum(0).build();
            }
            DevicePodDO devicePod = devicePodDO.get();
            Optional<DeviceNodeDO> deviceNodeOptional = deviceNodeRepository.selectById(devicePod.getNodeId());
            if (!deviceNodeOptional.isPresent()) {
                return SyncContainerResponse.builder().success(false).containerNum(0).build();
            }
            DeviceNodeDO deviceNode = deviceNodeOptional.get();

            Optional<ClusterDO> clusterDO = clusterRepository.selectById(deviceNode.getClusterId());
            if (!clusterDO.isPresent()) {
                return SyncContainerResponse.builder().success(false).containerNum(0).build();
            }
            ClusterDO cluster = clusterDO.get();

            Query q = Query.builder().build();
            q.setClusterId(deviceNode.getClusterId());
            q.setConfig(cluster.getConfig());
            q.setPodName(devicePod.getPodName());
            q.setFieldSelector(fieldSelector);
            q.setLabelSelector(labelSelector);

            CompletableFuture<List<Container>> future = containerCollector.collect(q);

            List<String> resultList = new ArrayList<>();
            CompletableFuture<SyncContainerResponse> handle = future.handle((containers, e) -> {
                if (e != null) {
                    log.error("collect container fail!", e);
                    return null;
                }

                List<ContainerBO> list = containers.stream().map(container ->
                    {
                        resultList.add(container.getName());
                        return ContainerBO.builder()
                            .containerId(container.getContainerId())
                            .containerName(container.getName())
                            .build();

                    }
                ).collect(Collectors.toList());

                devicePodRepository.updateByPrimaryKey(devicePod.getId(),
                    DevicePodDO.builder().containers(JsonUtils.writeValueAsString(list)).build());

                return SyncContainerResponse.builder().success(true).containerList(resultList).containerNum(
                    resultList.size()).build();
            });
            return handle.get();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return SyncContainerResponse.builder().success(false).containerNum(0).build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initCollector();
    }

}
