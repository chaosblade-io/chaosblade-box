package com.alibaba.chaosblade.box.common.commands;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author haibin
 *
 *
 */
@Slf4j
//@Component
//@Primary
public class CommandBus implements CommandExecutable, SpringCommandExecutable {

    private Map<String, CommandExecutor> commandTypeCommandExecutorPoolMap = new ConcurrentHashMap<>();

    public static String SPRING_BEAN_POOL_NAME = "spring-pool";

    private List<CommandDecorator> commandDecorators = new ArrayList<>();

    /**
     * 初始化commandBus
     *
     * @param commandExecutorMap
     */
    public CommandBus(List<CommandExecutor> commandExecutorMap) {
        Objects.requireNonNull(commandExecutorMap, "executor not be empty or null");
        for (CommandExecutor commandExecutor : commandExecutorMap) {
            CommandExecutor oldCommandExecutor = commandTypeCommandExecutorPoolMap.get(
                commandExecutor.getCommandExecutorName());
            if (oldCommandExecutor != null) {
                throw new IllegalStateException(String
                    .format("command executor for name:%s already exist,the exist one is:%s,the new one is:%s",
                        oldCommandExecutor.getCommandExecutorName(),
                        oldCommandExecutor.getClass().getCanonicalName(),
                        commandExecutor.getClass().getCanonicalName()));
            }
            commandTypeCommandExecutorPoolMap.put(commandExecutor.getCommandExecutorName(), commandExecutor);
        }
    }

    /**
     * 挑选执行引擎
     *
     * @param commandExecutorName
     * @return
     */
    public CommandExecutor select(String commandExecutorName) {
        CommandExecutor commandExecutor = commandTypeCommandExecutorPoolMap.get(commandExecutorName);
        if (commandExecutor == null) {
            log.info("not found command executor for command by name:" + commandExecutorName + ",use default instead");
            return commandTypeCommandExecutorPoolMap.get(CommandExecutorConstant.EXECUTOR_DEFAULT);
        }
        return commandExecutor;
    }

    /**
     * 获取引擎的执行信息
     *
     * @return
     */
    public List<CommandExecutorInfo> getCommandExecutorInfo() {
        return commandTypeCommandExecutorPoolMap.values().stream().map(
            new Function<CommandExecutor, CommandExecutorInfo>() {
                @Override
                public CommandExecutorInfo apply(CommandExecutor commandExecutor) {
                    return commandExecutor.getCommandExecutorInfo();
                }
            }).collect(Collectors.toList());
    }

    @Override
    public <Response> Future<Response> asyncRun(Command<Response> command) {
        return select(command.getCommandExecutorName()).asyncRun(decorateCommand(command));
    }

    @Override
    public <Response> Response syncRun(Command<Response> command) {
        return select(command.getCommandExecutorName()).syncRun(decorateCommand(command));
    }

    private <Response> Command<Response> decorateCommand(Command<Response> command) {
        Command<Response> resultCommand = command;
        if (commandDecorators != null) {
            for (CommandDecorator commandDecorator : commandDecorators) {
                resultCommand = commandDecorator.decorate(resultCommand);
            }
        }
        return resultCommand;
    }

    public void addCommandDecorator(CommandDecorator commandDecorator) {
        this.commandDecorators.add(commandDecorator);
    }

    @Override
    public <Request, Response> Future<Response> asyncRun(
        Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass, Request request) {
        return select(SPRING_BEAN_POOL_NAME).asyncRun(beanCommandClass, request);
    }

    @Override
    public <Request, Response> Response syncRun(Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass,
        Request request) {
        return select(SPRING_BEAN_POOL_NAME).syncRun(beanCommandClass, request);
    }
}
