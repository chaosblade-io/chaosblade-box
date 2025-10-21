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

import com.alibaba.chaosblade.box.annotation.LoginUser;
import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosApplicationContext;
import com.alibaba.chaosblade.box.common.infrastructure.ChaosRequestContextHolder;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/** @author haibin */
public class UserWebArgumentResolver implements HandlerMethodArgumentResolver {

  private LoginUserResolver loginUserResolver;

  public UserWebArgumentResolver(LoginUserResolver loginUserResolver) {
    this.loginUserResolver = loginUserResolver;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return existAnnoation(parameter) != null;
  }

  private LoginUser existAnnoation(MethodParameter methodParameter) {
    Annotation[] annotations = methodParameter.getParameterAnnotations();
    for (Annotation annotation : annotations) {
      if (LoginUser.class.isInstance(annotation)) {
        return (LoginUser) annotation;
      }
    }
    return null;
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws Exception {
    ChaosUser chaosUser =
        loginUserResolver.resolve((HttpServletRequest) webRequest.getNativeRequest());
    ChaosRequestContextHolder.getApplicationContext()
        .ifPresent(
            new Consumer<ChaosApplicationContext>() {
              @Override
              public void accept(ChaosApplicationContext chaosApplicationContext) {
                chaosApplicationContext.setLoginUser(chaosUser);
              }
            });
    return chaosUser;
  }
}
