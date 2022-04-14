package com.alibaba.chaosblade.box.service.model.cluster;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: xinyuan.ymm
 * @create: 2021-10-12 7:15 下午
 */
public class IdentifierCollection {
    private Map<String, IdentifierModel> identifierModelMap;

    public static IdentifierCollection getInstance() {
        IdentifierCollection identifierCollection = new IdentifierCollection();
        identifierCollection.identifierModelMap = new HashMap<>();
        return identifierCollection;
    }

    public IdentifierCollection add(String configurationId, String hostConfigurationId) {
        IdentifierModel identifierModel = new IdentifierModel(configurationId, hostConfigurationId);
        this.identifierModelMap.put(configurationId, identifierModel);
        return this;
    }

    public IdentifierCollection add(IdentifierModel identifierModel) {
        this.identifierModelMap.put(identifierModel.getConfigurationId(), identifierModel);
        return this;
    }

    public List<IdentifierModel> getIdentifierList() {
        if (identifierModelMap != null) {
            return Lists.newArrayList(identifierModelMap.values());
        }

        return Lists.newArrayList();
    }
}
