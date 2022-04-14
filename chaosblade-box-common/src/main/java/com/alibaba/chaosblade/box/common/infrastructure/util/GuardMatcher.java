package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.common.domain.definition.SceneFunctionParameterComponent;
import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ConditionArgumentDefinitionOperation;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.RecoveryStrategyFieldArgumentDefinition;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.apache.commons.jexl3.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Author: sunju
 *
 * Date:   2019/11/26
 */
@UtilityClass
public class GuardMatcher {

    private static final String VALUE_KEY_NAME = "value";

    public static boolean match(Object actualValue, List<RecoveryStrategyFieldArgumentDefinition> strategies) {
        return match(actualValue, strategies, true);
    }

    public static boolean match(Object actualValue, List<RecoveryStrategyFieldArgumentDefinition> strategies, boolean andOperationFirst) {
        if (null == strategies || strategies.isEmpty()) {
            return false;
        }

        String combineExpression = strategies.stream()
                                             .filter(Objects::nonNull)
                                             .map(GuardMatcher::generateExpression)
                                             .filter(Objects::nonNull)
                                             .reduce((p1, p2) -> {
                                                 boolean op1 = p1.getRight();
                                                 boolean op2 = p2.getRight();

                                                 boolean isAnd;
                                                 if (andOperationFirst) {
                                                     isAnd = op1 || op2;
                                                 } else {
                                                     isAnd = op1 && op2;
                                                 }

                                                 String symbol = isAnd ? "&&" : "||";

                                                 return Pair.of("(" + p1.getLeft() + symbol + p2.getLeft() + ")", isAnd);
                                             })
                                             .get()
                                             .getLeft();


        if (!Strings.isNullOrEmpty(combineExpression)) {
            JexlContext context = new MapContext();
            context.set(VALUE_KEY_NAME, actualValue);

            JexlEngine engine = new JexlBuilder().create();
            JexlExpression jexlExpression = engine.createExpression(combineExpression);

            return (Boolean) jexlExpression.evaluate(context);
        }

        return false;
    }

    @Nullable
    private static Pair<String, Boolean> generateExpression(RecoveryStrategyFieldArgumentDefinition strategy) {
        Object value = getValue(strategy);
        ConditionArgumentDefinitionOperation condition = strategy.getOperation();

        if (null == value || null == condition) {
            return null;
        }

        String expression;
        if (value instanceof Number) {
            expression = VALUE_KEY_NAME + condition.getLabel() + value;
        } else {
            if (Objects.equals(condition.getValue(), ConditionArgumentDefinitionOperation.LENGTH.getValue())) {
                expression = VALUE_KEY_NAME + "." + condition.getValue() + "()==" + value;
            } else {
                expression = VALUE_KEY_NAME + "." + condition.getValue() + "(\"" + value + "\")";
            }
        }

        return Pair.of(expression, strategy.isAnd());
    }

    private static Object getValue(RecoveryStrategyFieldArgumentDefinition strategy) {
        String value = strategy.getValue();

        SceneFunctionParameterComponent component = strategy.getComponent();
        if (null != component && Objects.equals("number", component.getType())) {
            if (Strings.isNullOrEmpty(value)) {
                return 0;
            }
            return Integer.parseInt(value);
        }

        return value;
    }

//    private double convertPercent(Number value) {
//        BigDecimal decimal = new BigDecimal(value.longValue());
//        BigDecimal result = decimal.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
//        return result.doubleValue();
//    }

}
