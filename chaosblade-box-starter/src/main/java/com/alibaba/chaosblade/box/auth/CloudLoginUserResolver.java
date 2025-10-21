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

import com.alibaba.chaosblade.box.common.common.domain.user.ChaosUser;
import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.alibaba.chaosblade.box.common.infrastructure.exception.PermissionDeniedException;
import com.google.common.collect.ImmutableList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** @author haibin */
@Slf4j
@Component
public class CloudLoginUserResolver implements LoginUserResolver {

  private static final List<String> WHITE_LIST = ImmutableList.of();

  @Override
  public ChaosUser resolve(HttpServletRequest httpServletRequest) throws ChaosException {
    String path = httpServletRequest.getRequestURI();
    if (WHITE_LIST.contains(path)) {
      return null;
    }

    HttpSession session = httpServletRequest.getSession();
    String uid = "";
    String name = "";
    String license = "";
    ChaosUser chaosUser = new ChaosUser();
    if (session != null) {
      Enumeration<String> enumerations = session.getAttributeNames();
      while (enumerations.hasMoreElements()) {
        String element = enumerations.nextElement();
        if (element.equals("uid")) {
          uid = session.getAttribute("uid").toString();
        }
        if (element.equals("name")) {
          name = session.getAttribute("name").toString();
        }
        if (element.equals("license")) {
          license = session.getAttribute("license").toString();
        }
      }
    }

    if (uid.equals("")) {
      throw new PermissionDeniedException(CommonErrorCode.P_LOGIN_MISSED, null);
    }

    chaosUser.setUserId(uid);
    chaosUser.setUserName(name);
    chaosUser.setLicense(license);
    return chaosUser;
  }
}
