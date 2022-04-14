package com.alibaba.chaosblade.box.service;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

public interface UserService {
    ChaosUser getUserByUserId(String userId);

    public boolean userNameExist(String userName) ;

    public ChaosUser saveUser(String userName, String password);

    public ChaosUser login(String userName, String password);

    public void updateLastLoginTime(Long id);

}
