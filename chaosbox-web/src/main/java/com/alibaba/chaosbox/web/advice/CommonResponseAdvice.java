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

package com.alibaba.chaosbox.web.advice;

import com.alibaba.chaosbox.dao.page.PageUtils;
import com.alibaba.chaosbox.web.context.OriginContext;
import com.alibaba.chaosbox.web.model.PageResponse;
import com.alibaba.chaosbox.web.model.Response;
import com.alibaba.chaosbox.web.model.WebConstants;
import com.github.pagehelper.Page;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author yefei
 */
@ControllerAdvice
@Order(0)
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !(returnType.getParameterType() == Response.class || HttpEntity.class.isAssignableFrom(returnType.getParameterType()));
    }

    @Override
    public Response<Object> beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                    ServerHttpRequest httpRequest, ServerHttpResponse httpResponse) {

        Page<Object> page = PageUtils.getPage();
        Response<Object> response;
        if (page != null) {
            PageResponse<Object> pageResponse = new PageResponse();
            pageResponse.setData(body);
            pageResponse.setPage(page.getPageNum());
            pageResponse.setPageSize(page.getPageSize());
            pageResponse.setPages(page.getPages());
            pageResponse.setTotal(page.getTotal());
            pageResponse.setOriginal(OriginContext.getOriginal());

            PageUtils.clearPage();

            response = Response.ofSuccess(pageResponse);
        } else {
            response = Response.ofSuccess(body);
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = String.valueOf(requestAttributes.getAttribute(WebConstants.REQUEST_ID, RequestAttributes.SCOPE_REQUEST));
        response.setRequestId(requestId);
        return response;
    }

}
