package com.alibaba.chaosblade.box.common.common.domain.definition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Author: sunju
 *
 * Date:   2020/5/19
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SceneFunctionParameterConstraint {

    public static final String CHECKER_TEMPLATE_OPPOSITE = "opposite";
    public static final String CHECKER_TEMPLATE_ONE_ONLY = "one_only";
    public static final String CHECKER_TEMPLATE_NOT_NULL = "not_null";

    /**
     * constraint range of parameters. INCLUDE current parameter.
     */
    List<String> range;

    /**
     * constraint checker.
     *
     * JS express which return a object like as below:
     *
     * <code>
     *     // item type: {id: "parameter id", name: "parameter name", alias: "parameter alias", "value": "<item value>"}
     *     var express = `
     *          (function() {
     *              const notNullItems = items.filter(item => item.value !== null && item.value !== undefined);
     *              if (notNullItems.length > 1) {
     *                  return {
     *                      result: false,
     *                      items: items.map(item => item.id),
     *                      message: '仅需要填写其中一个参数'
     *                  };
     *              }
     *              if (notNllItems.length <= 0) {
     *                  return {
     *                      result: false,
     *                      items: items.map(item => item.id),
     *                      message: '需要填写其中一个参数'
     *                  };
     *              }
     *              return {result: true};
     *          })();
     *     `;
     *
     *      // {result: false, items: ["", ""], message: ""}
     *      var context = {items: [{}, {}]};
     *      var result = eval(express, context);
     *      // check result and show message...
     * </code>
     */
    String checker;

    /**
     * Fixed templates of checker.
     */
    String checkerTemplate;

}
