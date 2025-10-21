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

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequestMapping(value = "/chaos/")
public class SessionBaseController {

  protected HttpServletRequest getServletRequest() {
    ServletRequestAttributes servletRequestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return servletRequestAttributes.getRequest();
  }

  protected HttpSession getSession() {
    return getServletRequest().getSession(false);
  }

  protected void invalidateSession() {
    getSession().invalidate();
  }

  protected void refreshSession(ChaosUser user) {
    HttpSession session = getServletRequest().getSession();
    session.setAttribute("uid", user.getUserId());
    session.setAttribute("name", user.getUserName());
    session.setAttribute("license", user.getLicense());
  }
}
