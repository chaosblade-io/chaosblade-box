package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesDataQuery;
import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesPod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Slf4j
public class PublicKubernetesDataRepository implements KubernetesDataRepository {


    @Override
    public Optional<KubernetesPod> findPodInfo(KubernetesDataQuery kubernetesDataQuery) {
        KubernetesPod kubernetesPod = getKubernetesPod(kubernetesDataQuery);
        if (kubernetesPod != null) {
            kubernetesPod.setUserId(kubernetesDataQuery.getUserId());
            kubernetesPod.setNamespace(kubernetesDataQuery.getNamespace());
            kubernetesPod.setTargetConfigurationId(kubernetesDataQuery.getTargetConfigurationId());
            kubernetesPod.setClusterId(kubernetesDataQuery.getClusterId());
        }
        return Optional.ofNullable(kubernetesPod);
    }



    private KubernetesPod getKubernetesPod(KubernetesDataQuery kubernetesDataQuery) {
        return null;
    }


}
