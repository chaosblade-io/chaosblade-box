package com.alibaba.chaosblade.box.dao.infrastructure.application.impl;

import com.alibaba.chaosblade.box.dao.infrastructure.application.ApplicationSceneFunctionParamFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Component
public class DefaultApplicationSceneFunctionParamFilter implements ApplicationSceneFunctionParamFilter {

    private Map<String, List<String>> filters = new HashMap<>();

    @Override
    public void load(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    @Override
    public boolean skip(String appCode, String paramAlias) {
        return filters.getOrDefault(appCode, new ArrayList<>()).contains(paramAlias);
    }
}
