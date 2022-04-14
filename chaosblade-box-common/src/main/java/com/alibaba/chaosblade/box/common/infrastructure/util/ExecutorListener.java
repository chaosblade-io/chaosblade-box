package com.alibaba.chaosblade.box.common.infrastructure.util;


import com.alibaba.chaosblade.box.common.common.domain.Response;

/**
 * @author haibin
 *
 *
 */
public interface ExecutorListener<ResultBody> {

    public void onSuccess(Response<ResultBody> result);

    public void onThrowable(Throwable throwable);

    public void onFailure(Response<ResultBody> result);

}
