package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesDataQuery;
import com.alibaba.chaosblade.box.dao.infrastructure.model.cluster.KubernetesPod;

import java.util.Optional;

/**
 * @author haibin.lhb
 *
 *
 */
public interface KubernetesDataRepository {

    /**
     * 查询pod信息,条件满足下会优先通过apiServer去查询,然后在用chaos来兜底，为了保证精准度
     *
     * @param kubernetesDataQuery
     * @return
     */
    public Optional<KubernetesPod> findPodInfo(KubernetesDataQuery kubernetesDataQuery);



}
