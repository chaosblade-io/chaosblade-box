package com.alibaba.chaosblade.box.dao.query;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class ApiAuthorizationQuery {

    private String accessToken;

    private String permission;

    private List<String> permissionTargets;

}
