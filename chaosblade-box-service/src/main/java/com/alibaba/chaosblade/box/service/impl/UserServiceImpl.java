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

package com.alibaba.chaosblade.box.service.impl;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.util.ClockUtil;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.alibaba.chaosblade.box.dao.repository.UserRepository;
import com.alibaba.chaosblade.box.service.UserService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Resource protected UserRepository userRepository;

  @Override
  public boolean userNameExist(String userName) {
    Optional<UserDo> options = userRepository.findByUserName(userName);
    return options.isPresent();
  }

  @Override
  public ChaosUser login(String userName, String password) {

    Optional<UserDo> options = userRepository.findByUserName(userName);
    if (!options.isPresent()) {
      throw new ChaosException(CommonErrorCode.P_STS_TOKEN_LOGIN_ILLEGAL);
    }

    UserDo userDo = options.get();
    if (!userDo.getUserPassword().equals(password)) {
      throw new ChaosException(CommonErrorCode.P_LOGIN_FORBINATION);
    }

    return convert(userDo);
  }

  @Override
  public ChaosUser saveUser(String userName, String password) {
    ChaosUser user = new ChaosUser(userName, password);
    userRepository.save(user);

    return user;
  }

  protected ChaosUser convert(UserDo userDo) {
    ChaosUser user = new ChaosUser();
    user.setUserId(userDo.getUserId());
    user.setUserName(userDo.getUserName());
    user.setId(userDo.getId());
    user.setLicense(userDo.getLicense());

    return user;
  }

  @Override
  public void updateLastLoginTime(Long id) {
    UserDo userDo = new UserDo();
    userDo.setId(id);
    userDo.setLastLoginTime(ClockUtil.now());

    userRepository.updateLastLoginTime(userDo);
  }

  @Override
  public ChaosUser getUserByUserId(String userId) {
    ChaosUser user = new ChaosUser();
    user.setUserId(userId);
    return user;
  }
}
