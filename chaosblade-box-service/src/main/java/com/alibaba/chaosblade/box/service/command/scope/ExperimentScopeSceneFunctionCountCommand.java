package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.mapper.CloudManualMapper;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import com.alibaba.chaosblade.box.service.model.scope.FunctionInvocationCount;
import com.alibaba.chaosblade.box.service.model.scope.ScopeInfoQueryRequest;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 查询演练小程序使用
 *
 * @author haibin
 *
 *
 */
@Component
public class ExperimentScopeSceneFunctionCountCommand
    extends SpringBeanCommand<ScopeInfoQueryRequest, List<FunctionInvocationCount>> {

    @Autowired
    private CloudManualMapper cloudManualMapper;

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Override
    public List<FunctionInvocationCount> execute(ScopeInfoQueryRequest scopeInfoQueryRequest) {
        String configurationId = scopeInfoQueryRequest.getConfigurationId();
        if (Strings.isNullOrEmpty(configurationId)) { return new ArrayList<>(); }
        return cloudManualMapper.countAppCodeByHost(configurationId).stream().map(
            new Function<Map<String, Object>, FunctionInvocationCount>() {
                @Override
                public FunctionInvocationCount apply(Map<String, Object> stringObjectMap) {
                    FunctionInvocationCount functionInvocationCount = new FunctionInvocationCount();
                    String appCode = stringObjectMap.get("app_code").toString();
                    functionInvocationCount.setCount(Integer.parseInt(stringObjectMap.get("total").toString()));
                    functionInvocationCount.setCode(appCode);
                    functionInvocationCount.setName(sceneFunctionRepository.findByCode(appCode).map(
                        new Function<SceneFunctionDO, String>() {
                            @Override
                            public String apply(SceneFunctionDO sceneFunctionDO) {
                                return sceneFunctionDO.getName();
                            }
                        }).orElse(null));
                    return functionInvocationCount;
                }
            }).filter(new Predicate<FunctionInvocationCount>() {
            @Override
            public boolean test(FunctionInvocationCount functionInvocationCount) {
                return !Strings.isNullOrEmpty(functionInvocationCount.getName());
            }
        }).collect(Collectors.toList());
    }
}
