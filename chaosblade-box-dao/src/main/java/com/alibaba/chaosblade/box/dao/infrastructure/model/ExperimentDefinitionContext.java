package com.alibaba.chaosblade.box.dao.infrastructure.model;

import com.alibaba.chaosblade.box.common.app.sdk.constants.PhaseType;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentActivityDefinition;
import com.alibaba.chaosblade.box.common.common.domain.definition.ExperimentFlowDefinition;
import com.alibaba.chaosblade.box.common.experiment.model.AppNameAndIdPair;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.request.ExperimentDefinitionRequest;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.google.common.base.Strings;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ExperimentDefinitionContext {

    private Set<String> appCodes;

    private Set<AppNameAndIdPair> applications = new HashSet<>();

    /**
     * 解析应用数据
     *
     * @param appName
     * @param appId
     */
    public void addApplication(String appName, String appId) {
        if (Strings.isNullOrEmpty(appId)) { return; }
        applications.add(new AppNameAndIdPair(appName, appId));
    }

    private ExperimentFlowDefinition experimentFlowDefinition;

    private ExperimentDO experimentDO;

    /**
     * 应用
     */

    private ExperimentDefinitionRequest experimentDefinitionRequest;

    public ExperimentFlowDefinition getExperimentFlowDefinition() {
        return experimentFlowDefinition;
    }

    public void addDefinitions(PhaseType phaseType,
                               List<ExperimentActivityDefinition> experimentActivityDefinitions) {
        if (experimentActivityDefinitions == null) {
            return;
        }
        if (PhaseType.RECOVER.equals(phaseType)) {
            experimentFlowDefinition.getRecover().addAll(experimentActivityDefinitions);
        }
        if (PhaseType.PREPARE.equals(phaseType)) {
            experimentFlowDefinition.getPrepare().addAll(experimentActivityDefinitions);
        }
        if (PhaseType.CHECK.equals(phaseType)) {
            experimentFlowDefinition.getCheck().addAll(experimentActivityDefinitions);
        }
        if (PhaseType.ATTACK.equals(phaseType)) {
            experimentFlowDefinition.getAttack().addAll(experimentActivityDefinitions);
        }
    }

    public ExperimentDefinitionContext() {
        appCodes = new HashSet<>();
        experimentFlowDefinition = new ExperimentFlowDefinition();
        experimentFlowDefinition.setAttack(new ArrayList<>());
        experimentFlowDefinition.setCheck(new ArrayList<>());
        experimentFlowDefinition.setRecover(new ArrayList<>());
        experimentFlowDefinition.setPrepare(new ArrayList<>());
    }

    public void addAppCode(String appCode) {
        String[] codeStr = appCode.split("\\.");
        if (codeStr.length > 1) {
            appCodes.add(codeStr[1]);
        }
    }

}