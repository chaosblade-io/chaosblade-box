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

package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.dao.mapper.ScriptMapper;
import com.alibaba.chaosblade.box.dao.model.ScriptDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** @author haibin */
@Repository
public class ScriptRepository implements IRepository<String, ScriptDO> {

  @Autowired private ScriptMapper scriptMapper;

  @Override
  public Optional<ScriptDO> findById(String s) {
    return Optional.ofNullable(scriptMapper.selectById(s));
  }

  @Override
  public boolean update(ScriptDO scriptDO) {
    Preconditions.checkArgument(scriptDO.getScriptId() != null, "script id not null");
    Preconditions.checkArgument(scriptDO.getVersion() != null, "script version not null");
    Integer oldVersion = scriptDO.getVersion();
    QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("version", oldVersion);
    queryWrapper.eq("script_id", scriptDO.getScriptId());
    scriptDO.setVersion(oldVersion + 1);
    return scriptMapper.update(scriptDO, queryWrapper) > 0;
  }

  @Override
  public boolean add(ScriptDO scriptDO) {
    scriptDO.setIsDelete(false);
    return scriptMapper.insert(scriptDO) > 0;
  }

  public ScriptDO findByAppCode(String appCode) {
    QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("app_code", appCode);
    return scriptMapper.selectOne(queryWrapper);
  }

  public ScriptDO findByFunctionId(String functionId) {
    QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("function_id", functionId);
    return scriptMapper.selectOne(queryWrapper);
  }

  public ScriptDO getScriptByScriptId(String scriptId) {
    QueryWrapper<ScriptDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("script_id", scriptId).eq("is_delete", false);
    return scriptMapper.selectOne(queryWrapper);
  }
}
