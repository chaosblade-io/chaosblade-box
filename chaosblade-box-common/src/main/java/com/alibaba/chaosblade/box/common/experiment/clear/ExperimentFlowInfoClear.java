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

package com.alibaba.chaosblade.box.common.experiment.clear;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.ExperimentFlowInfo;
import java.util.List;
import java.util.function.Consumer;

/** @author haibin.lhb */
public class ExperimentFlowInfoClear {

  public static void clearAllIds(ExperimentFlowInfo experimentFlowInfo) {
    if (experimentFlowInfo == null) {
      return;
    }
    List<MiniFlowGroup> flowGroups = experimentFlowInfo.getFlowGroups();
    if (flowGroups == null) {
      return;
    }
    flowGroups.forEach(
        new Consumer<MiniFlowGroup>() {
          @Override
          public void accept(MiniFlowGroup miniFlowGroup) {
            miniFlowGroup.setGroupId(null);
            List<MiniFlow> miniFlows = miniFlowGroup.getFlows();
            if (miniFlows == null) {
              return;
            }
            miniFlows.forEach(
                new Consumer<MiniFlow>() {
                  @Override
                  public void accept(MiniFlow miniFlow) {
                    miniFlow.setFlowId(null);
                    miniFlow
                        .getPrepare()
                        .forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                    miniFlow
                        .getAttack()
                        .forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                    miniFlow
                        .getCheck()
                        .forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                    miniFlow
                        .getRecover()
                        .forEach(
                            experimentActivityInfo -> experimentActivityInfo.setActivityId(null));
                  }
                });
          }
        });
  }

  /**
   * 演练转经验，还需要清除掉应用和机器相关信息
   *
   * @param experimentFlowInfo
   */
  public static void clearApplicationAndHosts(ExperimentFlowInfo experimentFlowInfo) {
    if (experimentFlowInfo == null) {
      return;
    }
    List<MiniFlowGroup> flowGroups = experimentFlowInfo.getFlowGroups();
    if (flowGroups == null) {
      return;
    }
    flowGroups.forEach(
        new Consumer<MiniFlowGroup>() {
          @Override
          public void accept(MiniFlowGroup miniFlowGroup) {
            miniFlowGroup.setAppId(null);
            miniFlowGroup.setAppName(null);
            miniFlowGroup.setAppGroups(null);
            miniFlowGroup.setHosts(null);
            miniFlowGroup.setSelectType(null);
            miniFlowGroup.setHostPercent(null);
            List<MiniFlow> miniFlows = miniFlowGroup.getFlows();
            if (miniFlows == null) {
              return;
            }
            miniFlows.forEach(
                new Consumer<MiniFlow>() {
                  @Override
                  public void accept(MiniFlow miniFlow) {
                    miniFlow.setFlowId(null);
                    miniFlow
                        .getPrepare()
                        .forEach(experimentActivityInfo -> experimentActivityInfo.setScope(null));
                    miniFlow
                        .getAttack()
                        .forEach(experimentActivityInfo -> experimentActivityInfo.setScope(null));
                    miniFlow
                        .getCheck()
                        .forEach(experimentActivityInfo -> experimentActivityInfo.setScope(null));
                    miniFlow
                        .getRecover()
                        .forEach(experimentActivityInfo -> experimentActivityInfo.setScope(null));
                  }
                });
          }
        });
  }
}
