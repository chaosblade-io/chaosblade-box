package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.service.model.device.SyncContainerRequest;
import com.alibaba.chaosblade.box.service.model.device.SyncContainerResponse;
import com.alibaba.chaosblade.box.service.model.device.SyncPodRequest;
import com.alibaba.chaosblade.box.service.model.device.SyncPodResponse;

/**
 * @author hengyu
 * @version 1.0.0
 * @className CollectorService
 * @createTime 2021/11/23 10:39:00
 * @description
 */
public interface CollectorService {

    /**
     * sync pod by node name
     * @param request
     * @return
     */
    SyncPodResponse syncPodResource(SyncPodRequest request);

    /**
     * sync container by pod name
     * @param request
     * @return
     */
    SyncContainerResponse syncContainerResource(SyncContainerRequest request);
}
