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

import com.alibaba.chaosblade.box.dao.mapper.UserMapper;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/** @author sunju */
@Repository
@Component
public class LicenseRepository {

  private static final Integer NOT_DELETE_FLAG = 0;

  @Resource private UserMapper userMapper;

  public Optional<UserDo> getUserLicense(String userId) {
    if (!Strings.isNullOrEmpty(userId)) {
      QueryWrapper<UserDo> queryWrapper =
          new QueryWrapper<UserDo>().eq("user_id", userId).eq("is_deleted", NOT_DELETE_FLAG);
      return Optional.ofNullable(userMapper.selectOne(queryWrapper));
    }
    return Optional.empty();
  }

  //    public Optional<LicenseDO> getUserLicense(String userId, String regionId) {
  //        if (!Strings.isNullOrEmpty(userId) && !Strings.isNullOrEmpty(regionId)) {
  //            QueryWrapper<LicenseDO> queryWrapper = new QueryWrapper<LicenseDO>()
  //                    .eq("user_id", userId)
  //                    .eq("region_id", regionId)
  //                    .eq("is_delete", NOT_DELETE_FLAG);
  //            return Optional.ofNullable(licenseMapper.selectOne(queryWrapper));
  //        }
  //        return Optional.empty();
  //    }

}
