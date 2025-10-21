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

package com.alibaba.chaosblade.box.toolsmgr.ansible.command;

import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommand;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleCommandExtraVarsUtil;
import com.alibaba.chaosblade.box.toolsmgr.ansible.AnsibleConstants;
import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import java.util.List;

/**
 * @author: xinyuan.ymm
 * @create: 2022-01-07 9:41 PM
 */
public class AnsiblePingCommand implements AnsibleCommand, AnsibleConstants {

  @Override
  public String getCommand(MgrRequest mgrRequest) {

    String sb = String.format("ansible %s", mgrRequest.getInstanceIp());

    if (!mgrRequest.getInstanceUser().isEmpty()) {
      sb = String.format("%s -u %s", sb, mgrRequest.getInstanceUser());
    }
    sb = String.format("%s -m ping", sb);

    return String.format("%s %s", sb, AnsibleCommandExtraVarsUtil.getExtraVars(mgrRequest));
  }

  @Override
  public boolean resultPredicate(List<String> result, boolean ignoreBackgroundTimeout) {
    if (result.stream().findFirst().get().contains("SUCCESS")) {
      return true;
    }
    return false;
  }
}
