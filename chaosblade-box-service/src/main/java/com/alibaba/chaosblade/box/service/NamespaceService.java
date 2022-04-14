package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.infrastructure.domain.namespace.Namespace;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;

import java.util.List;

/**
 * @author sunju
 *
 */
public interface NamespaceService {

    List<Namespace> getUserNamespaces(String userId);

    Namespace addNamespace(String name, String userId, String description) throws ChaosException;

    boolean deleteNamespace(String name, String userId);

    void initDefaultNamespace(String userId);

    Namespace queryNamespace(String userId, String namespace);

    public String getDefaultNamespace();
}
