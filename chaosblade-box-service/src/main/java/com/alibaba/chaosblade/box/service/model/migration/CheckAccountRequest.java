package com.alibaba.chaosblade.box.service.model.migration;

import lombok.Data;

@Data
public class CheckAccountRequest {
    private String account;

    private String password;

    // only for mysql
    private String host;

    private String port;
}
