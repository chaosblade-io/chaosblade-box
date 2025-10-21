/*
 * Copyright 2025 The ChaosBlade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.common.experiment.activity.checker.params;

import com.alibaba.chaosblade.box.common.infrastructure.domain.activity.ActivityGroupDefinitionCheckItem;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.SceneArgumentDefinition;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.ExperimentActivityInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/** @ClassName WhereRulePreCheck @Author liminghao */
@Component
public class WhereRulePreCheck extends ActivityParamPreChecker {

  private String WHERE_RULE = "where_rule";

  public static List<String> allOperators = new ArrayList<String>();

  public static String GreaterThanOrEqual = ">=";

  public static String GreaterThan = ">";

  public static String LessThanOrEqual = "<=";

  public static String LessThan = "<";

  public static String Equal = "eq";

  public static String NotEqual = "ne";

  public static String Contains = "cs";

  public static String StartsWith = "sw";

  public static String EndsWith = "ew";

  static {
    allOperators.add(GreaterThan);
    allOperators.add(GreaterThanOrEqual);
    allOperators.add(LessThanOrEqual);
    allOperators.add(LessThan);
    allOperators.add(Equal);
    allOperators.add(NotEqual);
    allOperators.add(Contains);
    allOperators.add(StartsWith);
    allOperators.add(EndsWith);
  }

  @Override
  public List<ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult> preCheck(
      ActivityParamPreCheckContext activityParamPreCheckContext,
      ExperimentActivityInfo experimentActivityInfo) {
    return checkArguments(activityParamPreCheckContext, experimentActivityInfo);
  }

  @Override
  protected ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult
      internalCheckArgument(
          ActivityParamPreCheckContext activityParamPreCheckContext,
          ExperimentActivityInfo experimentActivityInfo,
          SceneArgumentDefinition sceneArgumentDefinition) {
    String value = sceneArgumentDefinition.getValue();
    String alias = sceneArgumentDefinition.getAlias();
    if (WHERE_RULE.equals(alias)) {
      List<WhereRuleItem> whereRuleItems = null;
      try {
        whereRuleItems =
            new ObjectMapper().readValue(value, new TypeReference<List<WhereRuleItem>>() {});
        for (WhereRuleItem whereRuleItem : whereRuleItems) {
          isValid(whereRuleItem.getOperator());
        }
      } catch (Exception exception) {
        ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult result =
            new ActivityGroupDefinitionCheckItem.ActivityGroupDefinitionParamCheckResult();
        result.setAlias(alias);
        result.setError("sql筛选语句(where_rule)语法错误：" + exception.getMessage());
        return result;
      }
    }
    return null;
  }

  private void isValid(String operator) {
    if (!allOperators.contains(operator)) {
      throw new IllegalArgumentException("invalid operator:" + operator + ",scope:" + allOperators);
    }
  }
}
