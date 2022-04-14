package com.alibaba.chaosblade.box.dao.infrastructure.model.cluster;

import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1DeploymentStatus;
import lombok.Data;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data
public class BaseKubernetesObject {

    private V1DeploymentSpec spec;

    private V1DeploymentStatus status;

}
