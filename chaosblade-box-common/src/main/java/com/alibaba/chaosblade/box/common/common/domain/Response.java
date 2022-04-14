package com.alibaba.chaosblade.box.common.common.domain;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class Response<T> {

    private T result;

    private boolean success;

    /**
     * error errorCode
     */
    private ChaosError error;

    /**
     * 获取traceId
     */
    private String traceId;

    public static <T> Response<T> okWithData(T t) {
        return withData(t, true);
    }

    public static Response<Void> ok() {
        Response<Void> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    public static <T> Response<T> copy(Response<T> source) {
        Response<T> r = new Response<>();
        r.setResult(source.getResult());
        r.setError(source.getError());
        r.setSuccess(source.success);
        r.setTraceId(source.traceId);
        return r;
    }

    public static <T> Response<T> failedWith(ChaosError error) {
        Response<T> r = new Response<>();
        r.setSuccess(false);
        r.setError(error);
        return r;
    }

    public static <T> Response<T> failedWith(IErrorCode iErrorCode) {
        Response<T> r = new Response<>();
        r.setSuccess(false);
        r.setError(new ChaosError(iErrorCode));
        return r;
    }

    public static <T> Response<T> failedWith(IErrorCode iErrorCode, String message) {
        Response<T> r = new Response<>();
        r.setSuccess(false);
        r.setError(new ChaosError(iErrorCode, message));
        return r;
    }

    public static <T> Response<T> withData(T t, boolean success) {
        Response<T> r = new Response<>();
        r.setResult(t);
        r.setSuccess(success);
        return r;
    }

    public void error(ChaosError error) {
        this.setSuccess(false);
        this.setError(error);
    }

}
