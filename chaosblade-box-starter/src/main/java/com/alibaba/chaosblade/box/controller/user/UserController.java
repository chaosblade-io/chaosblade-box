package com.alibaba.chaosblade.box.controller.user;

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.response.Response;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.util.EncryptUtil;
import com.alibaba.chaosblade.box.service.NamespaceService;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.model.user.UserRegisterRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author sunju
 *
 */
@RestController
public class UserController extends SessionBaseController {
    @Autowired
    UserService userService;

    @Autowired
    NamespaceService namespaceService;

    @ApiOperation(value = "用户注册")
    @PostMapping("UserRegister")
    public Response<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        if (userService.userNameExist(userRegisterRequest.getUserName())) {
            throw new IllegalArgumentException("user name already exist");
//            return Response.ofFailure()
        }

        ChaosUser user = userService.saveUser(userRegisterRequest.getUserName(),
                EncryptUtil.reEncryptPassword(userRegisterRequest.getPassword()));
        namespaceService.initDefaultNamespace(user.getUserId());
        return Response.ofSuccess(true);
    }

    @ApiOperation(value = "用户登陆")
    @PostMapping("UserLogin")
    public Response<ChaosUser> userLogin(@RequestBody UserRegisterRequest userRegisterRequest) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {
        ChaosUser user = userService.login(userRegisterRequest.getUserName(), EncryptUtil.reEncryptPassword(userRegisterRequest.getPassword()));
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
