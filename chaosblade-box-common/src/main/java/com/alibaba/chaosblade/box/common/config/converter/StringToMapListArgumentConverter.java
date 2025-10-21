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

package com.alibaba.chaosblade.box.common.config.converter;

import com.alibaba.chaosblade.box.common.app.sdk.argument.ArgumentTypeConverter;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author haibin.lhb */
public class StringToMapListArgumentConverter
    implements ArgumentTypeConverter<String, Map<String, List<String>>> {

  /**
   * 标准数据格式: params:userId,userId2;param2:userId1,userId2
   *
   * @param from instance of source type
   * @return
   */
  @Override
  public Map<String, List<String>> convert(String from) {
    Map<String, List<String>> result = new HashMap<>();
    if (Strings.isNullOrEmpty(from)) {
      return result;
    }
    List<String> paramsPairs = Splitter.on(";").omitEmptyStrings().trimResults().splitToList(from);
    if (paramsPairs.isEmpty()) {
      return result;
    }
    paramsPairs.forEach(
        paramPair -> {
          String[] params = paramPair.split(":");
          if (params.length == 2) {
            result.put(params[0], Splitter.on(",").splitToList(params[1]));
          }
        });
    return result;
  }
}
