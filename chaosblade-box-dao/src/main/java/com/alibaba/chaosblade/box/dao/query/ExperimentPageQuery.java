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

package com.alibaba.chaosblade.box.dao.query;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.common.enums.IEnum;
import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/** @author sunpeng */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentPageQuery {

  List<String> experimentIds;

  ChaosUser user;
  String userId;

  String namespace;
  List<Integer> runResults;
  List<Integer> stateValues;
  Integer stateValue;
  /** 部分名字 */
  String partName;

  Boolean containsDeleted;

  List<String> tagNames;

  Boolean scheduler;

  public void setStateValues(List<ExperimentStateEnum> state) {
    if (!CollectionUtil.isNullOrEmpty(state)) {
      this.stateValues = state.stream().map(IEnum::getValue).collect(Collectors.toList());
    }
  }

  public void addStateValues(ExperimentStateEnum state) {
    if (null == state) {
      return;
    }
    if (CollectionUtil.isNullOrEmpty(this.stateValues)) {
      this.stateValues = new ArrayList<>();
    }
    this.stateValues.add(state.getValue());
  }

  public void setStateValue(ExperimentStateEnum state) {
    if (null != state) {
      stateValue = state.getValue();
    }
  }

  public void setPartName(String partName) {
    if (!Strings.isNullOrEmpty(partName)) {
      this.partName = "%" + partName + "%";
    }
  }

  public void setRunResults(List<ResultEnum> results) {
    if (!CollectionUtil.isNullOrEmpty(results)) {
      this.runResults = results.stream().map(ResultEnum::getValue).collect(Collectors.toList());
    }
  }

  public void setUser(ChaosUser user) {
    this.user = user;
    if (null != user) {
      if (!Strings.isNullOrEmpty(user.getUserId())) {
        this.userId = user.getUserId();
      }
    }
  }
}
