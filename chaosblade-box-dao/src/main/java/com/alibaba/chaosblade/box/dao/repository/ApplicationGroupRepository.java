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

import com.alibaba.chaosblade.box.dao.mapper.ApplicationGroupMapper;
import com.alibaba.chaosblade.box.dao.model.ApplicationGroupDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ApplicationGroupRepository implements IRepository<Long, ApplicationGroupDO> {

  @Autowired private ApplicationGroupMapper applicationGroupMapper;

  @Override
  public Optional<ApplicationGroupDO> findById(Long aLong) {
    QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", aLong);
    return Optional.ofNullable(applicationGroupMapper.selectOne(queryWrapper));
  }

  @Override
  public boolean update(ApplicationGroupDO applicationGroupDO) {
    QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("id", applicationGroupDO.getId());
    return applicationGroupMapper.update(applicationGroupDO, queryWrapper) > 0;
  }

  @Override
  public boolean add(ApplicationGroupDO applicationGroupDO) {
    return applicationGroupMapper.insert(applicationGroupDO) > 0;
  }

  public List<String> findApplicationGroupsByAppId(Long appId) {
    QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("app_id", appId);
    List<String> A =
        applicationGroupMapper.selectList(queryWrapper).stream()
            .map(
                new Function<ApplicationGroupDO, String>() {
                  @Override
                  public String apply(ApplicationGroupDO applicationGroupDO) {
                    return applicationGroupDO.getName();
                  }
                })
            .collect(Collectors.toList());
    return A;
  }

  public ApplicationGroupDO findApplicationGroup(Long appId, String groupName) {
    QueryWrapper<ApplicationGroupDO> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("name", groupName);
    queryWrapper.eq("app_id", appId);
    return applicationGroupMapper.selectOne(queryWrapper);
  }
}
