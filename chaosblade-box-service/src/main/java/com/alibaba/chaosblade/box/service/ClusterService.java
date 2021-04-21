package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.service.model.cluster.ClusterBO;

import java.util.List;

/**
 * @author yefei
 */
public interface ClusterService {

    /**
     *
     * @param clusterBO
     */
    void addCluster(ClusterBO clusterBO);

    /**
     *
     * @param clusterBO
     * @return
     */
    List<ClusterBO> getClusterPageable(ClusterBO clusterBO);

    /**
     *
     * @param clusterBO
     */
    void activeCollect(ClusterBO clusterBO) throws Exception;

    /**
     *
     * @param clusterBO
     */
    void closeCollect(ClusterBO clusterBO) throws Exception;

}
