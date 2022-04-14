package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterLinkage;

/**
 * @author haibin
 *
 * 
 */
public class RecoveryStrategyToleranceArgumentDefinitionRepo {
    public static String PERCENT = "percent";

    public static String DURATION = "duration";

    public static final RecoveryStrategyToleranceArgumentDefinition TOLERANCE_HOST_PERCENT = createTolerance(
        "机器占比",
        "percent",
        "指标达到阈值的机器数超过总机器数指定比例时执行恢复操作",
        "%"
    );

    public static final RecoveryStrategyToleranceArgumentDefinition TOLERANCE_DURATION = createTolerance(
        "持续时长",
        "duration",
        "指标达到阈值的持续时长超过指定持续时长时执行恢复操作",
        "秒"
    );

    private static RecoveryStrategyToleranceArgumentDefinition createTolerance(String name, String alias,
        String description, String unit) {
        RecoveryStrategyToleranceArgumentDefinition tolerance = new RecoveryStrategyToleranceArgumentDefinition();
        tolerance.setName(name);
        tolerance.setAlias(alias);
        tolerance.setUnit(unit);
        tolerance.setDescription(description);
        tolerance.setEnabled(true);

        SceneFunctionParameterComponent component = new SceneFunctionParameterComponent();
        component.setType("number");
        component.setRequired(true);
        component.setUnit(unit);


        SceneFunctionParameterLinkage linkage = new SceneFunctionParameterLinkage();
        linkage.setDefaultState(true);

        component.setLinkage(linkage);
        tolerance.setComponent(component);

        return tolerance;
    }
}
