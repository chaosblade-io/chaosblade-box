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

import com.alibaba.chaosblade.box.dao.mapper.ApplicationConfigurationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationConfigurationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ApplicationConfigurationRepository
    implements IRepository<Long, ApplicationConfigurationDO> {

  @Autowired private ApplicationConfigurationMapper applicationConfigurationMapper;

  public ApplicationConfigurationDO findByAliasAndAppId(String alias, String appId) {
    QueryWrapper<ApplicationConfigurationDO> applicationConfigurationDOQueryWrapper =
        new QueryWrapper<>();
    applicationConfigurationDOQueryWrapper.eq("alias", alias);
    applicationConfigurationDOQueryWrapper.eq("app_id", Long.valueOf(appId));
    return applicationConfigurationMapper.selectOne(applicationConfigurationDOQueryWrapper);
  }

  @Override
  public Optional<ApplicationConfigurationDO> findById(Long aLong) {
    return Optional.ofNullable(applicationConfigurationMapper.selectById(aLong));
  }

  @Override
  public boolean update(ApplicationConfigurationDO applicationConfigurationDO) {
    applicationConfigurationDO.setGmtModified(new Date());
    return applicationConfigurationMapper.updateById(applicationConfigurationDO) > 0;
  }

  @Override
  public boolean add(ApplicationConfigurationDO applicationConfigurationDO) {
    applicationConfigurationDO.setGmtModified(new Date());
    return applicationConfigurationMapper.insert(applicationConfigurationDO) > 0;
  }

  public List<ApplicationConfigurationDO> findByApplicationId(Long appId) {
    QueryWrapper<ApplicationConfigurationDO> applicationConfigurationDOQueryWrapper =
        new QueryWrapper<>();
    applicationConfigurationDOQueryWrapper.eq("app_id", appId);
    return applicationConfigurationMapper.selectList(applicationConfigurationDOQueryWrapper);
  }
}
