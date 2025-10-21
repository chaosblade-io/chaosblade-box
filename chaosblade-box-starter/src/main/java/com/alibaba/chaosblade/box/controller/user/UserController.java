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

package com.alibaba.chaosblade.box.controller.user;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.util.EncryptUtil;
import com.alibaba.chaosblade.box.service.NamespaceService;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.model.user.UserRegisterRequest;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** @author sunju */
@RestController
public class UserController extends SessionBaseController {
  @Autowired UserService userService;

  @Autowired NamespaceService namespaceService;

  @ApiOperation(value = "用户注册")
  @PostMapping("UserRegister")
  public Response<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException,
          IOException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
    if (userService.userNameExist(userRegisterRequest.getUserName())) {
      throw new IllegalArgumentException("user name already exist");
      //            return Response.ofFailure()
    }

    ChaosUser user =
        userService.saveUser(
            userRegisterRequest.getUserName(),
            EncryptUtil.reEncryptPassword(userRegisterRequest.getPassword()));
    namespaceService.initDefaultNamespace(user.getUserId());
    return Response.ofSuccess(true);
  }

  @ApiOperation(value = "用户登陆")
  @PostMapping("UserLogin")
  public Response<ChaosUser> userLogin(@RequestBody UserRegisterRequest userRegisterRequest)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException,
          IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
    ChaosUser user =
        userService.login(
            userRegisterRequest.getUserName(),
            EncryptUtil.reEncryptPassword(userRegisterRequest.getPassword()));
    refreshSession(user);
    userService.updateLastLoginTime(user.getId());
    return Response.ofSuccess(user);
  }

  @ApiOperation(value = "用户退出")
  @PostMapping("UserLoginout")
  public Response<Boolean> userLoginout() {
    invalidateSession();

    return Response.ofSuccess(true);
  }

  @ApiOperation(value = "用户详情")
  @PostMapping("LoginUserInfo")
  public Response<ChaosUser> getLoginUserInfo(@LoginUser ChaosUser chaosUser) {

    return Response.ofSuccess(chaosUser);
  }
}
