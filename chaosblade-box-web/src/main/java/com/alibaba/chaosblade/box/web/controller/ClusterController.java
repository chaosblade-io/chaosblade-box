package com.alibaba.chaosblade.box.web.controller;

import com.alibaba.chaosblade.box.service.ClusterService;
import com.alibaba.chaosblade.box.service.model.cluster.ClusterBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yefei
 */
@RestController
@RequestMapping("/api")
public class ClusterController {

    @Autowired
    private ClusterService clusterService;

    @RequestMapping("/AddCluster")
    public void addCluster(@RequestBody ClusterBO clusterBO) {
        clusterService.addCluster(clusterBO);
    }

    @RequestMapping("/UpdateCluster")
    public void updateCluster(@RequestBody ClusterBO clusterBO) {
        clusterService.updateCluster(clusterBO);
    }

    @RequestMapping("/GetClusterPageable")
    public List<ClusterBO> getClusterPageable(@RequestBody ClusterBO clusterBO) {
        return clusterService.getClusterPageable(clusterBO);
    }

    @RequestMapping("/ActiveCollect")
    public void activeCollect(@RequestBody ClusterBO clusterBO) throws Exception {
        clusterService.activeCollect(clusterBO);
    }

    @RequestMapping("/CloseCollect")
    public void closeCollect(@RequestBody ClusterBO clusterBO) throws Exception {
        clusterService.closeCollect(clusterBO);
    }
}
