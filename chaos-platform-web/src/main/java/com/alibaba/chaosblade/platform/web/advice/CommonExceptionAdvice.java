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

package com.alibaba.chaosblade.platform.web.advice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import com.alibaba.chaosblade.platform.web.model.Response;
import com.alibaba.chaosblade.platform.web.model.WebConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author yefei
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionAdvice {

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response resolveException(Throwable ex) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = String.valueOf(requestAttributes.getAttribute(WebConstants.REQUEST_ID, RequestAttributes.SCOPE_REQUEST));
        log.error("request id: {}, message: {}", requestId, ex.getMessage(), ex);

        Response response = Response.ofFail("SYSTEM_ERROR: " + ex.getMessage());
        response.setRequestId(requestId);
        return response;
    }

    /**
     * @param ex
     */
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    @ResponseBody
    public Response resolveCommonException(Exception ex) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = String.valueOf(requestAttributes.getAttribute(WebConstants.REQUEST_ID, RequestAttributes.SCOPE_REQUEST));
        if (StrUtil.isBlank(ex.getMessage())) {
            log.error("request id: {}, message: {}", requestId, ex.getMessage(), ex);
        }
        Response response = Response.ofFail(ex.getMessage());
        response.setRequestId(requestId);
        return response;
    }

    /**
     * @param ex
     */
    @ExceptionHandler({BizException.class})
    @ResponseBody
    public Response<Object> resolveBizException(BizException ex) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = String.valueOf(requestAttributes.getAttribute(WebConstants.REQUEST_ID, RequestAttributes.SCOPE_REQUEST));
        String message = ex.getMessage();
        if (ex.getData() != null) {
            message = message + ":" + ex.getData();
        }
        Response<Object> response = Response.ofFail(message);
        if (ex.getCode() != null) {
            response.setCode(ex.getCode());
        }
        response.setData(ex.getData());
        response.setRequestId(requestId);
        return response;
    }

}
