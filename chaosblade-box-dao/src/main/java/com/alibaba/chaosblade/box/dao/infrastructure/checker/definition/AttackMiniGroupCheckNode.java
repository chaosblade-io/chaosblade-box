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

package com.alibaba.chaosblade.box.dao.infrastructure.checker.definition;

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlowGroup;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class AttackMiniGroupCheckNode extends BaseMiniGroupCheckNode {
  @Override
  public Void invoke(MiniGroupCheckContext context) throws Exception {
    MiniFlowGroup miniFlowGroup = context.getMiniFlowGroup();
    List<MiniFlow> newMiniFlows = new ArrayList<>();
    for (MiniFlow miniFlow : miniFlowGroup.getFlows()) {
      if (!CollectionUtil.isNullOrEmpty(miniFlow.getAttack())) {
        newMiniFlows.add(miniFlow);
      } else {
        log.warn("mini flow not contains attack");
      }
    }
    miniFlowGroup.setFlows(newMiniFlows);
    return getNext().invoke(context);
  }
}
