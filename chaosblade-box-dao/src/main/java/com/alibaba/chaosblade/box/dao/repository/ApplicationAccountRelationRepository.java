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

import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ApplicationAccountRelationMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationAccountRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yefei
 * @create 2020-12-14 15:12
 */
@Service
public class ApplicationAccountRelationRepository
    implements IRepository<String, ApplicationAccountRelationDO> {

  @Autowired private ApplicationAccountRelationMapper applicationAccountRelationMapper;

  @Override
  public Optional<ApplicationAccountRelationDO> findById(String s) {
    return Optional.ofNullable(applicationAccountRelationMapper.selectById(s));
  }

  @Override
  public boolean update(ApplicationAccountRelationDO applicationAccountRelationDO) {
    return applicationAccountRelationMapper.updateById(applicationAccountRelationDO) > 1;
  }

  public boolean updateByRelationId(ApplicationAccountRelationDO applicationAccountRelationDO) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("relation_id", applicationAccountRelationDO.getRelationId());
    return applicationAccountRelationMapper.update(applicationAccountRelationDO, queryWrapper) == 1;
  }

  public ApplicationAccountRelationDO selectRelation(String accountId, Long applicationId) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_id", accountId);
    queryWrapper.in("application_id", applicationId);
    queryWrapper.eq("is_delete", 0);
    return applicationAccountRelationMapper.selectOne(queryWrapper);
  }

  @Override
  public boolean add(ApplicationAccountRelationDO applicationAccountRelationDO) {
    return applicationAccountRelationMapper.insert(applicationAccountRelationDO) == 1;
  }

  public IPage<ApplicationAccountRelationDO> findApplicationsAccountPage(
      String userId, Collection<Long> applicationIds, int page, int size) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    if (!Strings.isNullOrEmpty(userId)) {
      queryWrapper.eq("account_id", userId);
    }
    queryWrapper.in("application_id", applicationIds);
    queryWrapper.eq("is_delete", 0);
    return applicationAccountRelationMapper.selectPage(
        MyBatisUtil.getPage(page, size), queryWrapper);
  }

  public IPage<ApplicationAccountRelationDO> findApplicationsAccountEnablePage(
      String userId, Collection<Long> applicationIds, int page, int size) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_id", userId);
    queryWrapper.in("application_id", applicationIds);
    queryWrapper.eq("status", 0);
    queryWrapper.eq("is_delete", 0);
    return applicationAccountRelationMapper.selectPage(
        MyBatisUtil.getPage(page, size), queryWrapper);
  }

  public <E> List<ApplicationAccountRelationDO> findApplicationsAccountEnable(
      String userId, Collection<E> applicationIds) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_id", userId);
    queryWrapper.in("application_id", applicationIds);
    queryWrapper.eq("status", 0);
    queryWrapper.eq("is_delete", 0);
    return applicationAccountRelationMapper.selectList(queryWrapper);
  }

  public List<ApplicationAccountRelationDO> findApplicationsAccount(String userId) {
    QueryWrapper<ApplicationAccountRelationDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_id", userId);
    queryWrapper.eq("status", 0);
    queryWrapper.eq("is_delete", 0);
    return applicationAccountRelationMapper.selectList(queryWrapper);
  }
}
