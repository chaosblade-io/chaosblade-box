package com.alibaba.chaosblade.box.common.common.domain.definition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * <p>Date: 2019/1/21
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SceneFunctionParameterLinkage {

  /** TRUE: display; FALSE: hidden */
  boolean defaultState;

  /** parameter id which depends */
  String depends;

  /**
   * condition of display, condition expression should be evaluated.
   *
   * <p>Component will change to display if condition return TRUE, otherwise will change to hide.
   *
   * <p>e.g
   *
   * <p>For input type component <code>
   *     var parameterAlias = 'myInputParam';
   *
   *     var context = {myInputParam: 'name'};
   *     var condition = 'myInputParam !== null';
   *
   *     var newState = eval(condition, context);
   *     if (newState) {
   *         // display this component
   *     } else {
   *         // hide this component
   *     }
   * </code> </br>
   *
   * <p>For radio or select type component <code>
   *     var parameterAlias = 'mySelectParam';
   *     var selectedOption = {key: 'myKey', value: 'enable'};
   *
   *     var context = {mySelectParam: 'myKey'};
   *     var condition = 'mySelectParam === "myKey"';
   *
   *     var newState = eval(condition, context);
   *     if (newState) {
   *         // display this component
   *     } else {
   *         // hide this component
   *     }
   * </code>
   */
  String condition;
}
