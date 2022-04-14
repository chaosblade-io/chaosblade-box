package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class ClusterInfo {

    @JSONField(name = "cluster_name")
    private String clusterName;

    @JSONField(name = "pod_count")
    private Integer podCount;

}
