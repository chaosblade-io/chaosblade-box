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

package com.alibaba.chaosblade.box.dao.model;

import com.alibaba.chaosblade.box.dao.infrastructure.model.Script;
import com.alibaba.chaosblade.box.dao.model.base.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** @author haibin */
@TableName("t_chaos_script")
@Data
public class ScriptDO extends BaseDO {

  @TableId(type = IdType.ID_WORKER)
  private String scriptId;

  private String appCode;

  private String functionId;

  private String userId;

  private String subUserId;

  private String scriptContent;

  private Integer version;

  private Boolean isDelete;

  private String name;

  private String signature;

  private String language;

  public static Script toScript(ScriptDO scriptDO) {
    if (scriptDO == null) {
      return null;
    }
    return new Script(
        scriptDO.getScriptId(),
        scriptDO.getSignature(),
        scriptDO.getName(),
        scriptDO.getScriptContent(),
        scriptDO.getLanguage());
  }
}
