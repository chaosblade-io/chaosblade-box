package com.alibaba.chaosblade.box.common.commands;

import java.util.concurrent.Future;

/**
 * @author haibin
 *
 * 
 */
public interface SpringCommandExecutable {

    /**
     * 异步运行
     *
     * @param beanCommandClass
     * @param request
     * @return
     */
    public <Request, Response> Future<Response> asyncRun(
        Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass,
        Request request);

    /**
     * 同步运行
     *
     * @param beanCommandClass
     * @param request
     * @return
     */
    public <Request, Response> Response syncRun(Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass,
        Request request);

}
