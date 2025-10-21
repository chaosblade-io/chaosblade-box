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

package com.alibaba.chaosblade.box.toolsmgr.ansible;

import com.alibaba.chaosblade.box.toolsmgr.model.MgrRequest;
import org.apache.commons.lang3.StringUtils;

/** 命令扩展参数 */
public class AnsibleCommandExtraVarsUtil {

  /**
   * 特殊变量采用 extra-var方式 添加到ad-hoc 命令行中，不存储hosts文件
   *
   * @param ansibleContext
   * @return
   */
  public static String getExtraVars(MgrRequest mgrRequest) {
    StringBuilder sb = new StringBuilder();

    sb.append(" --extra-vars '");
    if (mgrRequest.getInstancePort() != null) {
      sb.append(" ansible_ssh_port=");
      sb.append(mgrRequest.getInstancePort());
    } else {
      sb.append(" ansible_ssh_port=" + AnsibleConstants.ANSIBLE_LINUX_DEFAULT_PORT);
    }

    if (mgrRequest.getNeedPassword()) {
      if (StringUtils.isBlank(mgrRequest.getInstancePassword())) {
        throw new IllegalArgumentException("use privilege escalation, must input password");
      }
      sb.append(" ansible_ssh_pass=");
      sb.append(mgrRequest.getInstancePassword());
    }

    sb.append("'");

    return sb.toString();
  }
}
