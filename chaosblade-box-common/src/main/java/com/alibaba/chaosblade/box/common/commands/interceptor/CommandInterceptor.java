package com.alibaba.chaosblade.box.common.commands.interceptor;


import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.InvocationCommand;

/**
 * @author haibin.lhb
 *
 *
 */
public interface CommandInterceptor {

    /**
     * 执行前
     *
     * @param command
     */
    public void onStarted(Command<?> command);

    /**
     * 执行之后，处理返回值
     *
     * @param command
     * @param result
     */
    public void onReturn(InvocationCommand command, Object result);

    /**
     * 执行出错
     *
     * @param command
     * @param throwable 执行异常
     * @return transfer error
     */
    public void onError(InvocationCommand command, Throwable throwable);

}
