package com.alibaba.chaosblade.box.service.model.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String userName;

    private String password;
}
