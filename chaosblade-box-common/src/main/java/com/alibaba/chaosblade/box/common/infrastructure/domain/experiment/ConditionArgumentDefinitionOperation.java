package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ConditionArgumentDefinitionOperation {

    public static final ConditionArgumentDefinitionOperation EQ = ConditionArgumentDefinitionOperation.of("=", "eq");
    public static final ConditionArgumentDefinitionOperation GT = ConditionArgumentDefinitionOperation.of(">", "gt");
    public static final ConditionArgumentDefinitionOperation GTE = ConditionArgumentDefinitionOperation.of(">=", "gte");
    public static final ConditionArgumentDefinitionOperation LT = ConditionArgumentDefinitionOperation.of("<", "lt");
    public static final ConditionArgumentDefinitionOperation LTE = ConditionArgumentDefinitionOperation.of("<=", "lte");

    public static final ConditionArgumentDefinitionOperation CONTAINS = ConditionArgumentDefinitionOperation.of("包含", "contains");
    public static final ConditionArgumentDefinitionOperation START_WITH = ConditionArgumentDefinitionOperation.of("开头为", "startsWith");
    public static final ConditionArgumentDefinitionOperation END_WITH = ConditionArgumentDefinitionOperation.of("结尾为", "endsWith");
    public static final ConditionArgumentDefinitionOperation LENGTH = ConditionArgumentDefinitionOperation.of("长度", "length");

    public static final List<ConditionArgumentDefinitionOperation> NUMERIC_OPERATIONS = ImmutableList.of(
            ConditionArgumentDefinitionOperation.EQ,
            ConditionArgumentDefinitionOperation.GT,
            ConditionArgumentDefinitionOperation.GTE,
            ConditionArgumentDefinitionOperation.LT,
            ConditionArgumentDefinitionOperation.LTE
    );

    public static final List<ConditionArgumentDefinitionOperation> TEXT_OPERATIONS = ImmutableList.of(
            ConditionArgumentDefinitionOperation.CONTAINS,
            ConditionArgumentDefinitionOperation.START_WITH,
            ConditionArgumentDefinitionOperation.END_WITH,
            ConditionArgumentDefinitionOperation.LENGTH
    );




    String label;
    String value;

}
