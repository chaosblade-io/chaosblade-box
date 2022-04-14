package com.alibaba.chaosblade.box.auth;


import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author haibin
 *
 *
 */
public interface LoginUserResolver {

    public ChaosUser resolve(HttpServletRequest httpServletRequest) throws
        Exception;
}
