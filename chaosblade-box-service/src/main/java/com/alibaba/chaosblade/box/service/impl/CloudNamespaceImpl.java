package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.domain.namespace.Namespace;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.model.NamespaceDO;
import com.alibaba.chaosblade.box.dao.repository.LicenseRepository;
import com.alibaba.chaosblade.box.dao.repository.NamespaceRepository;
import com.alibaba.chaosblade.box.service.NamespaceService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
public class CloudNamespaceImpl implements NamespaceService {

    @Resource
    protected NamespaceRepository namespaceRepository;

    @Resource
    protected LicenseRepository licenseRepository;

    private static List<String> ProtectedNamespace = Lists.newArrayList("demo", "default");

    @Override
    public List<Namespace> getUserNamespaces(String userId) {
        Optional<List<NamespaceDO>> optional = namespaceRepository.getUserNamespace(userId);
        if (optional.isPresent()) {
            List<NamespaceDO> namespaces = optional.get();
            return namespaces.stream()
//                .filter(namespaceDO -> !ProtectedNamespace.contains(namespaceDO.getName()))
                .sorted(Comparator.comparing(NamespaceDO::getGmtCreate))
                .map(this::convert)
                .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    @Override
    public Namespace addNamespace(String name, String userId, String description) throws ChaosException {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }

        NamespaceDO namespace = new NamespaceDO();
        namespace.setName(name);
        namespace.setUserId(userId);
        namespace.setDescription(description);

        boolean result = namespaceRepository.addNamespace(namespace);
        if (result) {
            return convert(namespace);
        } else {
            throw new ChaosException(CommonErrorCode.P_NAMESPACE_EXIST, "实例已存在");
        }

    }

    @Override
    public boolean deleteNamespace(String name, String userId) {
        return namespaceRepository.deleteNamespace(name, userId);
    }

    protected Namespace convert(NamespaceDO ns) {
        Namespace namespace = new Namespace();
        namespace.setId(String.valueOf(ns.getId()));
        namespace.setName(ns.getName());
        namespace.setUserId(ns.getUserId());
        return namespace;
    }

    @Override
    public void initDefaultNamespace(String userId) {
        NamespaceDO namespacedo = new NamespaceDO();
        namespacedo.setUserId(userId);
        namespacedo.setName("default");
        namespacedo.setDescription("默认的命名空间");

        namespaceRepository.addNamespace(namespacedo);
    }

    @Override
    public Namespace queryNamespace(String userId, String namespace) {
        NamespaceDO namespaceDO = namespaceRepository.queryNamespace(userId, namespace);
        if (namespaceDO != null) {
            return convert(namespaceDO);
        }
        return null;
    }

    @Override
    public String getDefaultNamespace() {
        return "default";
    }
}
