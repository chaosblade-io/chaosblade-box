package com.alibaba.chaosblade.box.dao.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentNodeArgumentsDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.application.ApplicationSceneFunctionParamFilter;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionDO;
import com.alibaba.chaosblade.box.dao.model.SceneFunctionParameterDO;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionParameterRepository;
import com.alibaba.chaosblade.box.dao.repository.SceneFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Component
public class ArgumentDefinitionConvertor {

    @Autowired
    private SceneFunctionParameterRepository sceneFunctionParameterRepository;

    @Autowired
    private SceneFunctionRepository sceneFunctionRepository;

    @Autowired
    private ApplicationSceneFunctionParamFilter applicationSceneFunctionParamFilter;

    public ExperimentNodeArgumentsDefinition mergeArgumentDefinitions(String appCode,
                                                                      List<SceneArgumentDefinition> arguments) {
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition = new ExperimentNodeArgumentsDefinition();
        if (CollectionUtil.isNullOrEmpty(arguments)) {
            return experimentNodeArgumentsDefinition;
        }
        Map<String, String> actions = new HashMap<>();
        Map<String, String> userArgs = new HashMap<>();
        for (SceneArgumentDefinition sceneArgumentDefinition : arguments) {
            actions.put(sceneArgumentDefinition.getAlias(), sceneArgumentDefinition.getValue());
            userArgs.put(sceneArgumentDefinition.getAlias(), sceneArgumentDefinition.getValue());
            experimentNodeArgumentsDefinition.getArgsComponents().put(sceneArgumentDefinition.getAlias(),
                sceneArgumentDefinition.getComponent());
        }
        experimentNodeArgumentsDefinition.setAction(actions);
        experimentNodeArgumentsDefinition.setUserArgs(userArgs);
        return experimentNodeArgumentsDefinition;
    }

    public List<SceneArgumentDefinition> convertToArgumentDefinitions(String appCode,
                                                                      List<SceneFunctionParameterDO> sceneFunctionParameterDOS, boolean fromApp) {
        return sceneFunctionParameterDOS.stream().filter(new Predicate<SceneFunctionParameterDO>() {
            @Override
            public boolean test(SceneFunctionParameterDO sceneFunctionParameterDO) {
                if (fromApp) {
                    return !applicationSceneFunctionParamFilter.skip(appCode, sceneFunctionParameterDO.getAlias());
                }
                if (sceneFunctionParameterDO.isInvisibleWithNoDep()) { return false; }
                return true;
            }
        }).sorted(getSceneFunctionParameterDOComparator()).map(
            sceneFunctionParameterDO -> {
                SceneArgumentDefinition sceneArgumentDefinition = new SceneArgumentDefinition();
                sceneArgumentDefinition.setDescription(sceneFunctionParameterDO.getDescription());
                sceneArgumentDefinition.setEnabled(true);
                sceneArgumentDefinition.setComponent(sceneFunctionParameterDO.getComponent());
                sceneArgumentDefinition.setAlias(sceneFunctionParameterDO.getAlias());
                sceneArgumentDefinition.setName(sceneFunctionParameterDO.getName());
                sceneArgumentDefinition.setParameterId(sceneFunctionParameterDO.getParameterId());
                sceneArgumentDefinition.setFunctionId(sceneFunctionParameterDO.getFunctionId());
                return sceneArgumentDefinition;
            }).collect(Collectors.toList());

    }

    private Comparator<SceneFunctionParameterDO> getSceneFunctionParameterDOComparator() {
        return (o1, o2) -> {
            if (o1.getComponent() != null && o2.getComponent() != null) {
                if (o1.getComponent().isRequired() && !o2.getComponent().isRequired()) {
                    return -1;
                }
                if (!o1.getComponent().isRequired() && o2.getComponent().isRequired()) {
                    return 1;
                }
            }
            if (o1.getSequence() == null || o2.getSequence() == null) { return 0; }
            return o1.getSequence().compareTo(o2.getSequence());
        };
    }

    public List<SceneArgumentDefinition> splitExperimentNodeArgumentsDefinition(String appCode,
        ExperimentNodeArgumentsDefinition experimentNodeArgumentsDefinition) {
        SceneFunctionDO sceneFunctionDO = sceneFunctionRepository.findByCode(appCode).orElse(null);
        if (sceneFunctionDO == null) { return new ArrayList<>(); }
        if (experimentNodeArgumentsDefinition == null) { return new ArrayList<>();}
        List<SceneFunctionParameterDO> sceneFunctionParameterDOS = sceneFunctionParameterRepository
            .findFilterParamsByFunctionId(
                sceneFunctionDO);
        Map<String, String> userArguments = experimentNodeArgumentsDefinition.getAllArguments();
        Set<String> argAlias = new HashSet<>();
        return sceneFunctionParameterDOS.stream().sorted(
            getSceneFunctionParameterDOComparator()).map(
            sceneFunctionParameterDO -> {
                if (argAlias.contains(sceneFunctionParameterDO.getAlias())) { return null; }
                argAlias.add(sceneFunctionParameterDO.getAlias());
                SceneArgumentDefinition sceneArgumentDefinition = new SceneArgumentDefinition();
                sceneArgumentDefinition.setAlias(sceneFunctionParameterDO.getAlias());
                sceneArgumentDefinition.setValue(userArguments.get(sceneFunctionParameterDO.getAlias()));
                sceneArgumentDefinition.setEnabled(true);
                sceneArgumentDefinition.setOrder(sceneFunctionParameterDO.getSequence());
                sceneArgumentDefinition.setName(sceneFunctionParameterDO.getName());
                sceneArgumentDefinition.setDescription(sceneFunctionParameterDO.getDescription());
                sceneArgumentDefinition.setParameterId(sceneFunctionParameterDO.getParameterId());
                sceneArgumentDefinition.setFunctionId(sceneFunctionParameterDO.getFunctionId());
                mergeComponent(sceneFunctionParameterDO.getComponent(),
                    experimentNodeArgumentsDefinition.getArgsComponents().get(sceneFunctionParameterDO.getAlias()));
                sceneArgumentDefinition.setComponent(sceneFunctionParameterDO.getComponent());
                return sceneArgumentDefinition;
            }).filter(sceneArgumentDefinition -> sceneArgumentDefinition != null).collect(
            Collectors.toList());
    }

    private void mergeComponent(SceneFunctionParameterComponent latestComponent,
                                SceneFunctionParameterComponent paramComponent) {
        if (paramComponent == null) { return; }
        latestComponent.setPlainText(null);
        latestComponent.setOpLevel(paramComponent.getOpLevel());
        latestComponent.setCipherText(paramComponent.getCipherText());
    }

}
