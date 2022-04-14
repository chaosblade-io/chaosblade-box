package com.alibaba.chaosblade.box.service.model.cluster;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author haibin.lhb
 *
 *
 */
@Data
@EqualsAndHashCode
@ToString
public class ExperimentPodVO {

    @JSONField(name = "pod_ip")
    private String podIp;
    @JSONField(name = "pod_name")
    private String podName;
    @JSONField(name = "kub_namespace")
    private String kubNamespace;
    @JSONField(name = "app_name")
    private String appName;
    @JSONField(name = "app_id")
    private Long appId;
    /**
     * 是否被演练
     */
    @JSONField(name = "is_experiment")
    private Boolean isExperimented;

    @JSONField(name = "last_heart_time")
    private Long heartTime;
}
