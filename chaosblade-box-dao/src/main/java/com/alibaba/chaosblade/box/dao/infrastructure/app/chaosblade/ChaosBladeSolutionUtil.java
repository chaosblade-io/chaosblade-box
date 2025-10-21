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

package com.alibaba.chaosblade.box.dao.infrastructure.app.chaosblade;

import com.google.common.base.Strings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** @author sunpeng */
public class ChaosBladeSolutionUtil {

  private static final Pattern errorMsgMatch = Pattern.compile("(?<=`)(.*?)(?=`)");

  private static final String matchString = "“%s”";

  private static final String replaceIfNull = "“%s”：";

  private static final String remindStr = "（\"|\"表示\"或\",\"&\"表示\"与\"）";

  private static final String andStr = "|";

  private static final String orStr = "&";

  public static String parseSolution(String solution, String errorMessage) {
    if (!Strings.isNullOrEmpty(solution) && solution.contains(matchString)) {
      String errorParam = getIfMatchExit(errorMessage);
      if (!Strings.isNullOrEmpty(errorParam)) {
        return addIfNeedRemind(solution.replaceFirst(matchString, errorParam));
      }
      return solution.replaceFirst(replaceIfNull, "");
    }
    return solution;
  }

  private static String getIfMatchExit(String error) {
    Matcher m = errorMsgMatch.matcher(error);
    if (m.find()) {
      return m.group();
    }
    return null;
  }

  private static String addIfNeedRemind(String solution) {
    if (solution.contains(andStr) || solution.contains(orStr)) {
      return solution + remindStr;
    }
    return solution;
  }
}
