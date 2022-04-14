package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.commands.interceptor.CommandInterceptor;
import com.google.common.collect.ImmutableList;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author haibin
 *
 *
 */
public abstract class CommandExecutor implements CommandExecutable, SpringCommandExecutable {

    protected List<CommandInterceptor> commandInterceptors = new ArrayList<>();

    public void addCommandInterceptors(List<CommandInterceptor> newCommandInterceptors) {
        if (newCommandInterceptors == null || newCommandInterceptors.isEmpty()) {return;}
        for (CommandInterceptor commandInterceptor : newCommandInterceptors) {
            if (!commandInterceptors.contains(commandInterceptor)) {
                commandInterceptors.add(commandInterceptor);
            }
        }
        AnnotationAwareOrderComparator.sort(commandInterceptors);
    }

    protected void onStarted(Command<?> command) {
        for (CommandInterceptor commandInterceptor : commandInterceptors) {
            commandInterceptor.onStarted(command);
        }
    }

    protected void onReturn(InvocationCommand invocationCommand, Object result) {
        for (CommandInterceptor commandInterceptor : commandInterceptors) {
            commandInterceptor.onReturn(invocationCommand, result);
        }
    }

    protected void onError(InvocationCommand invocationCommand, Throwable throwable) {
        for (CommandInterceptor commandInterceptor : commandInterceptors) {
            commandInterceptor.onError(invocationCommand, throwable);
        }
    }

    public List<CommandInterceptor> getCommandInterceptors() {
        return ImmutableList.copyOf(commandInterceptors);
    }

    protected <Response> Response internalInvoke(Command<?> command, Callable<Response> responseCallable) {
        InvocationCommand invocationCommand = new InvocationCommand(command);
        onStarted(command);
        invocationCommand.start();
        try {
            Response response = responseCallable.call();
            onReturn(invocationCommand, response);
            return response;
        } catch (Throwable throwable) {
            onError(invocationCommand, throwable);
            throw new CommandRunTimeException(throwable);
        } finally {
            invocationCommand.finish();
        }
    }

    /**
     * @return
     */
    public abstract String getCommandExecutorName();

    /**
     * 获取执行器信息
     *
     * @return
     */
    public abstract CommandExecutorInfo getCommandExecutorInfo();
}
