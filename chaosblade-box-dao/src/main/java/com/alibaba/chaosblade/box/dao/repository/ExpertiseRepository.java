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

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.model.ExpertiseConstant;
import com.alibaba.chaosblade.box.dao.mapper.ExpertiseMapper;
import com.alibaba.chaosblade.box.dao.model.ExpertiseDO;
import com.alibaba.chaosblade.box.dao.query.ExpertisePageQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class ExpertiseRepository implements IRepository<String, ExpertiseDO> {
  @Autowired private ExpertiseMapper expertiseMapper;

  @Override
  public Optional<ExpertiseDO> findById(String s) {
    return Optional.ofNullable(expertiseMapper.selectById(s));
  }

  public boolean updateState(String expertiseId, Integer state) {
    ExpertiseDO expertiseDO = new ExpertiseDO();
    expertiseDO.setExpertiseId(expertiseId);
    expertiseDO.setState(state);
    return expertiseMapper.updateById(expertiseDO) > 0;
  }

  public int batchDeleteExpertisesByUserId(String userId, boolean logicalDelete) {
    ExpertiseDO expertiseDO = new ExpertiseDO();
    if (logicalDelete) {
      expertiseDO.setState(ExpertiseConstant.STATE_DELETED);
      return expertiseMapper.update(
          expertiseDO,
          new QueryWrapper<ExpertiseDO>()
              .ne("state", ExpertiseConstant.STATE_DELETED)
              .eq("user_id", userId));
    } else {
      expertiseDO.setUserId(userId);
      return expertiseMapper.delete(new QueryWrapper<ExpertiseDO>().eq("user_id", userId));
    }
  }

  /**
   * 查询经验库管理页数据
   *
   * @param key
   * @param user
   * @param tagNames
   * @param page
   * @param size
   * @return
   */
  public IPage<ExpertiseDO> findPageable(
      String key, ChaosUser user, List<String> tagNames, int page, int size) {
    ExpertisePageQuery expertisePageQuery = new ExpertisePageQuery();
    if (!Strings.isNullOrEmpty(key)) {
      expertisePageQuery.setPartName("%" + key + "%");
    }
    if (!CollectionUtil.isNullOrEmpty(tagNames)) {
      expertisePageQuery.setTagNames(tagNames);
    }
    expertisePageQuery.setUserId(user.getUserId());

    return expertiseMapper.selectUserExpertisesByPage(
        MyBatisUtil.getPage(page, size), expertisePageQuery);
  }

  /**
   * 查询经验库引用页数据
   *
   * @param chaosUser
   * @param key
   * @param tagNames
   * @param scopeType
   * @param page
   * @param size
   * @return
   */
  public IPage<ExpertiseDO> userFindPageable(
      ChaosUser chaosUser,
      String key,
      List<String> tagNames,
      String scopeType,
      int page,
      int size) {
    ExpertisePageQuery expertisePageQuery = new ExpertisePageQuery();
    expertisePageQuery.setState(ExpertiseConstant.STATE_ENABLED);
    if (!Strings.isNullOrEmpty(key)) {
      expertisePageQuery.setPartName("%" + key + "%");
    }
    // 模糊查询，因为一个经验库可能同时有多种类型
    if (!Strings.isNullOrEmpty(scopeType)) {
      expertisePageQuery.setScopeType("%" + scopeType + "%");
    }
    if (!CollectionUtil.isNullOrEmpty(tagNames)) {
      expertisePageQuery.setTagNames(tagNames);
    }
    // 这里查询不需要子账号ID，因为直接查询账号下的所有经验库和系统经验库
    expertisePageQuery.setUserId(chaosUser.getUserId());
    return expertiseMapper.selectUserAndAdminExpertisesByPage(
        MyBatisUtil.getPage(page, size), expertisePageQuery);
  }

  public List<ExpertiseDO> findExpertisesByType(Integer type) {
    QueryWrapper<ExpertiseDO> expertiseDOQueryWrapper = new QueryWrapper<>();
    expertiseDOQueryWrapper.eq("state", ExpertiseConstant.STATE_ENABLED);
    if (null != type) {
      expertiseDOQueryWrapper.eq("type", type);
    }
    return expertiseMapper.selectList(expertiseDOQueryWrapper);
  }

  @Override
  public boolean update(ExpertiseDO expertiseDO) {
    return expertiseMapper.updateById(expertiseDO) > 0;
  }

  @Override
  public boolean add(ExpertiseDO expertiseDO) {
    return expertiseMapper.insert(expertiseDO) > 0;
  }
}
