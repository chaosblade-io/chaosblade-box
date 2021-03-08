/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
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

package com.alibaba.chaosbox.web.interceptor;

import cn.hutool.core.util.IdUtil;
import com.alibaba.chaosbox.web.model.WebConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yefei
 */
@Slf4j
public class AttachRequestIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LocaleContextHolder.resetLocaleContext();

        String requestId = IdUtil.simpleUUID();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(WebConstants.REQUEST_ID, requestId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }

}
