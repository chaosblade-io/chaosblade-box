package com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.impl;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.definition.ExperimentFlowDefinitionMeta;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.interceptor.ExperimentActivityDefinitionInterceptor;
import com.alibaba.chaosblade.box.dao.infrastructure.service.SceneDaoFunctionService;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author sunju
 *
 */
@Component
public class JavaAgentDefaultArgumentInterceptor implements ExperimentActivityDefinitionInterceptor {

    private static final String JAVA_AGENT_INSTALL_APP_CODE = "chaos.jvm.install";

    @Resource
    private SceneDaoFunctionService sceneDaoFunctionService;


    private Map<String, String> setDefaultValueIfEmpty(Integer type, Map<String, String> values,
        Map<String, List<SceneFunctionParameterDO>> grouping) {
        if (values == null) {
            values = new HashMap<>();
        }
        for (Entry<String, List<SceneFunctionParameterDO>> entry : grouping.entrySet()) {
            String alias = entry.getKey();
            if (!values.containsKey(alias)) {
                List<SceneFunctionParameterDO> parameters = entry.getValue();
                if (null != parameters && !parameters.isEmpty()) {
                    SceneFunctionParameterDO parameter = parameters.get(0);
                    if (null != parameter && type.equals(parameter.getType())) {
                        SceneFunctionParameterComponent component = parameter.getComponent();
                        if (null != component) {
                            String defaultValue = component.getDefaultValue();
                            if (!Strings.isNullOrEmpty(defaultValue)) {
                                values.put(alias, defaultValue);
                            }
                        }
                    }
                }
            }
        }
        return values;
    }

    @Override
    public void preInterceptor(ExperimentActivityDefinition originDefinition, PhaseType phaseType,
                               ExperimentFlowDefinitionMeta experimentFlowDefinitionMeta) {
        if (null != originDefinition && phaseType == PhaseType.PREPARE) {
            if (JAVA_AGENT_INSTALL_APP_CODE.equals(originDefinition.getAppCode())) {
                SceneFunctionDO sceneFunction = sceneDaoFunctionService.querySceneFunctionByCode(
                    JAVA_AGENT_INSTALL_APP_CODE);
                Map<String, List<SceneFunctionParameterDO>> grouping = sceneFunction.getParameters()
                    .stream()
                    .collect(Collectors.groupingBy(SceneFunctionParameterDO::getAlias, Collectors.toList()));
                ExperimentNodeArgumentsDefinition args = originDefinition.getArguments();
                args.setAction(
                    setDefaultValueIfEmpty(SceneFunctionParameterDO.TYPE_ACTION, args.getAction(), grouping));
                args.setMatcher(
                    setDefaultValueIfEmpty(SceneFunctionParameterDO.TYPE_MATCHER, args.getMatcher(), grouping));
                args.setUserArgs(
                    setDefaultValueIfEmpty(SceneFunctionParameterDO.TYPE_USER, args.getUserArgs(), grouping));
            }
        }
    }
}
