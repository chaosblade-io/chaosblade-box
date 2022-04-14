package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.chaosblade.box.service.model.experiment.ExperimentScope;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@ToString
@Data
@EqualsAndHashCode
public class ExperimentClusterVO {

    private String clusterId;

    private String clusterName;

    private Long nodeCount;

    private Long onlineCount;

    /**
     * agent状态
     */
    private Boolean agentConsistency;
    /**
     * 部分节点
     */
    private List<ExperimentScope> partNodes;

    private List<String> chaosTools;

    private Boolean upgrade;

    private String version;

    private String pluginType;

    private String installMode;


}
