package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.infrastructure.error.ThrowableChaosErrorWrappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin
 *
 *
 */
public abstract class SpringBeanCommand<Request, Response> implements Command<Response> {

    @Autowired
    protected ThrowableChaosErrorWrappers throwableChaosErrorWrappers;

    @Autowired
    protected CommandBus commandBus;

    /**
     * 单例的话执行要带参数
     *
     * @param request
     * @return
     */
    public abstract Response execute(Request request);

    @Override
    public Response execute() {
        throw new UnsupportedOperationException("Default not support execute()");
    }

    @Override
    public String getCommandExecutorName() {
        return CommandBus.SPRING_BEAN_POOL_NAME;
    }
}
