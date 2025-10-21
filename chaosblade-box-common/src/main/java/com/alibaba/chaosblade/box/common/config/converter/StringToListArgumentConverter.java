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
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/** @author haibin.lhb */
public class StringToListArgumentConverter implements ArgumentTypeConverter<String, List<String>> {

  @Override
  public List<String> convert(String from) {
    return Lists.newArrayList(StringUtils.splitByWholeSeparator(from, ","));
  }
}
