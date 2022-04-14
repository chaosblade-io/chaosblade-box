package com.alibaba.chaosblade.box.model;

import com.alibaba.chaosblade.box.common.common.domain.ChaosError;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.service.model.RestResponse;

/**
 * @author haibin
 * 
 * 
 */
public class RestResponseUtil {

    public static RestResponse<Void> ok() {
        RestResponse<Void> r = new RestResponse<>();
        r.setSuccess(true);
        return r;
    }

    public static <T> RestResponse<T> okWithData(T t) {
        RestResponse<T> r = new RestResponse<>();
        r.setResult(t);
        r.setSuccess(true);
        r.setCode("200");
        return r;
    }

    public static <T> RestResponse<T> failed(ChaosError chaosError) {
        RestResponse<T> r = new RestResponse<>();
        r.setSuccess(false);
        if (chaosError != null) {
            r.setCode(chaosError.getCode());
            r.setMessage(chaosError.getErrorMessage());
            r.setStatusCode(chaosError.getCodeStatus());
        }
        return r;
    }

    public static <T> RestResponse<T> initWithServiceResponse(Response<T> response) {
        RestResponse<T> r = new RestResponse<>();
        if (null == response) {
            return r;
        }
        r.setResult(response.getResult());
        r.setSuccess(response.isSuccess());
        if (response.getError() != null) {
            return failed(response.getError());
        }
        return r;
    }
}
