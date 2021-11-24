package com.alibaba.chaosblade.box.service.collect;

import java.util.Map;

import com.alibaba.chaosblade.box.collector.Collector;
import com.alibaba.chaosblade.box.collector.CollectorStrategy;
import com.alibaba.chaosblade.box.collector.CollectorType;
import com.alibaba.chaosblade.box.collector.ContainerCollector;
import com.alibaba.chaosblade.box.collector.NodeCollector;
import com.alibaba.chaosblade.box.collector.PodCollector;
import com.alibaba.chaosblade.box.collector.model.Pod;
import com.alibaba.chaosblade.box.common.enums.DeviceStatus;
import com.alibaba.chaosblade.box.common.enums.DeviceType;
import com.alibaba.chaosblade.box.dao.model.DeviceDO;
import com.alibaba.chaosblade.box.dao.model.DeviceNodeDO;
import com.alibaba.chaosblade.box.dao.model.DevicePodDO;
import com.alibaba.chaosblade.box.dao.repository.DevicePodRepository;
import com.alibaba.chaosblade.box.dao.repository.DeviceRepository;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

/**
 * @author hengyu
 * @version 1.0.0
 * @className AbstractCollect
 * @createTime 2021/11/23 12:56:00
 * @description
 */
public abstract class AbstractCollect {

    @Autowired
    protected ApplicationContext applicationContext;

    protected NodeCollector nodeCollector;

    protected PodCollector podCollector;

    protected ContainerCollector containerCollector;

    @Value("${chaos.collector.type}")
    protected String collectorType;


    public static void syncDeviceAndPodInfo(DeviceNodeDO deviceNode, Pod pod, DevicePodRepository devicePodRepository,
        DeviceRepository deviceRepository) {
        Long deviceId = devicePodRepository.selectByNameAndNamespace(pod.getNamespace(),
                pod.getName())
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
                    .nodeId(deviceNode.getId())
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
    }


    protected void initCollector() {
        Map<String, Collector> beansOfType = applicationContext.getBeansOfType(Collector.class);
        for (Map.Entry<String, Collector> entry : beansOfType.entrySet()) {
            Collector value = entry.getValue();
            CollectorStrategy strategy = value.getClass().getAnnotation(CollectorStrategy.class);
            CollectorType collectorType = EnumUtil.fromString(CollectorType.class, this.collectorType.toUpperCase());
            if (strategy.value() == collectorType) {
                if (value instanceof NodeCollector) {
                    nodeCollector = (NodeCollector)value;
                }
                if (value instanceof PodCollector) {
                    podCollector = (PodCollector)value;
                }
                if (value instanceof ContainerCollector) {
                    containerCollector = (ContainerCollector)value;
                }
            }
        }
    }




}
