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

package com.alibaba.chaosblade.box.auth;

import com.alibaba.chaosblade.box.annotation.AdminOperation;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/** @author haibin */
public class AdminOperationHandlerInterceptor implements HandlerInterceptor {
  //    private AdministratorManager administratorManager;

  private LoginUserResolver loginUserResolver;

  public AdminOperationHandlerInterceptor(LoginUserResolver loginUserResolver) {
    this.loginUserResolver = loginUserResolver;
  }
  //    public AdminOperationHandlerInterceptor(AdministratorManager administratorManager,
  //        LoginUserResolver loginUserResolver) {
  //        this.administratorManager = administratorManager;
  //        this.loginUserResolver = loginUserResolver;
  //    }

  @Override
  public boolean preHandle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object handler)
      throws Exception {
    if (handler instanceof HandlerMethod) {
      HandlerMethod handlerMethod = (HandlerMethod) handler;
      boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(AdminOperation.class);
      boolean hasClassAnnotation =
          AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AdminOperation.class) != null;
      if (hasMethodAnnotation || hasClassAnnotation) {
        ChaosUser chaosUser = loginUserResolver.resolve(httpServletRequest);
        if (chaosUser == null) {
          throw new ChaosException(CommonErrorCode.P_USER_PERMISSION_DENIED, "无权限");
        }
      }
    }
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {}

  @Override
  public void afterCompletion(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Object o,
      Exception e)
      throws Exception {}
}
