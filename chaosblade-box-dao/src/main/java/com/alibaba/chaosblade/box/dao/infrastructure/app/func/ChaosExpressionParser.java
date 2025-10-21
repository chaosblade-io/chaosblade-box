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

package com.alibaba.chaosblade.box.dao.infrastructure.app.func;

import com.alibaba.chaosblade.box.dao.infrastructure.app.func.tools.StringTool;
import com.alibaba.chaosblade.box.dao.infrastructure.app.func.tools.TimeTool;
import com.google.common.base.Strings;
import org.apache.commons.jexl3.*;

/**
 * Parser for expression. Expression MUST be surround with "func()". Use like
 * "func(StringTool.concat('now:', TimeTool.timestamp()))".
 *
 * @author sunju
 */
public final class ChaosExpressionParser {

  private static final String FUNC_START = "func(";

  private static final String FUNC_END = ")";

  private JexlContext jexlContext;

  private static class ChaosExpressionParserHolder {
    public static ChaosExpressionParser chaosExpressionParser = new ChaosExpressionParser();
  }

  public static ChaosExpressionParser getInstance() {
    return ChaosExpressionParserHolder.chaosExpressionParser;
  }

  private ChaosExpressionParser() {
    initContext();
  }

  private void initContext() {
    jexlContext = new MapContext();
    jexlContext.set("StringTool", StringTool.class);
    jexlContext.set("TimeTool", TimeTool.class);
  }

  public boolean isExpression(Object value) {
    if (null == value) {
      return false;
    }

    if (String.class != value.getClass()) {
      return false;
    }

    String string = String.valueOf(value);

    return !Strings.isNullOrEmpty(string)
        && string.startsWith(FUNC_START)
        && string.endsWith(FUNC_END);
  }

  public Object eval(Object value) {
    if (!isExpression(value)) {
      return value;
    }

    String string = String.valueOf(value);

    int sIndex = string.indexOf('(');
    int eIndex = string.lastIndexOf(')');
    String expression = string.substring(sIndex + 1, eIndex);

    if (!Strings.isNullOrEmpty(expression)) {
      JexlEngine engine = new JexlBuilder().create();
      JexlExpression jexlExpression = engine.createExpression(expression);
      return jexlExpression.evaluate(this.jexlContext);
    }

    return string;
  }
}
