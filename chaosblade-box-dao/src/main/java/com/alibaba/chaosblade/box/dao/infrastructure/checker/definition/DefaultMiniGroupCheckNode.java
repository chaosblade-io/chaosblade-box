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

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.chain.ChainRootNode;
import com.alibaba.chaosblade.box.common.infrastructure.constant.HostSelectTypes;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import java.util.List;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
@ChainRootNode
public class DefaultMiniGroupCheckNode extends BaseMiniGroupCheckNode {

  @Override
  public Void invoke(MiniGroupCheckContext context) throws Exception {
    List<Host> hosts = context.getMiniFlowGroup().getHosts();
    checkHost(hosts, context.getMiniFlowGroup().getSelectType());
    return null;
  }

  public void checkHost(List<Host> hosts, Integer selectType) {
    if (HostSelectTypes.SELECT_TYPE_IP.equals(selectType)) {
      if (CollectionUtil.isNullOrEmpty(hosts)) {
        throw new ChaosException(CommonErrorCode.P_HOST_REQUIRED);
      }
    }
  }
}
