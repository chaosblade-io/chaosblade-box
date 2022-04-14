package com.alibaba.chaosblade.box.service.model.agent;

import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
public class ExperimentClusterQueryRequest extends BaseRequest {

    @JSONField(name = "node_configuration_id")
    private String nodeConfigurationId;

    private Integer page;

    private Integer size;

    private String key;

    @JSONField(name = "cluster_id")
    private String clusterId;

    /**
     * k8snamespace
     */
    @JSONField(name = "kub_namespace")
    private List<String> kubNamespaces;

}
