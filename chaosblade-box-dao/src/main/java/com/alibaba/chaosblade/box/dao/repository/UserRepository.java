package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.dao.mapper.UserMapper;
import com.alibaba.chaosblade.box.dao.model.UserDo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    public Optional<UserDo> findByUserName(@NotNull String userName) {
        if (userName.equals("")) {
            return Optional.empty();
        }
        QueryWrapper<UserDo> queryWrapper = new QueryWrapper<UserDo>()
                .eq("user_name", userName);
        if (queryWrapper == null) {
            throw new ChaosException(CommonErrorCode.P_STS_TOKEN_LOGIN_ILLEGAL);
        }

        UserDo userDo = userMapper.selectOne(queryWrapper);
        if (userDo == null) {
            return Optional.empty();
        }
        return Optional.of(userDo);
    }

    public boolean save(ChaosUser user) {
        UserDo userDo = new UserDo();
        userDo.setUserName(user.getUserName());
        userDo.setUserPassword(user.getPassword());
        userDo.setUserId(user.getUserId());
        userDo.setLicense(user.getLicense());

        return userMapper.insert(userDo) > 0;
    }

    public void updateLastLoginTime (UserDo userDo) {

        userMapper.updateById(userDo);
    }

    public String getUserIdByLicense(String license) {
        QueryWrapper<UserDo> queryWrapper = new QueryWrapper<UserDo>()
                .eq("license", license);
        if (queryWrapper == null) {
            throw new ChaosException(CommonErrorCode.P_STS_TOKEN_LOGIN_ILLEGAL);
        }
        UserDo userDo = userMapper.selectOne(queryWrapper);
        if (userDo == null) {
            return "";
        }
        return userDo.getUserId();
    }

    public UserDo getById (String userId) {
        QueryWrapper<UserDo> queryWrapper = new QueryWrapper<UserDo>()
                .eq("user_id", userId);
        if (queryWrapper == null) {
            throw new ChaosException(CommonErrorCode.P_STS_TOKEN_LOGIN_ILLEGAL);
        }
        return userMapper.selectOne(queryWrapper);
    }
}
