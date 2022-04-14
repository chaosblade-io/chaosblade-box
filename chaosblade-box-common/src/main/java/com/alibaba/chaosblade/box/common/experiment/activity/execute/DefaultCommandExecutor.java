package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import com.alibaba.chaosblade.box.common.commands.*;
import com.alibaba.chaosblade.box.common.infrastructure.util.SpringUtils;
import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.*;

/**
 * @author haibin
 *
 *
 */
public class DefaultCommandExecutor extends CommandExecutor {

    protected ExecutorService executor;

    protected String name;

    protected CommandExecutorInfoHolder commandExecutorInfoHolder;

    private ApplicationContext applicationContext;

    private BlockingQueue<Runnable> blockingQueue;

    public DefaultCommandExecutor(String name, ApplicationContext applicationContext,
        CommandExecutorConfig commandExecutorConfig) {
        this.name = name;
        this.blockingQueue = initQueue();
        this.executor = initExecutor(commandExecutorConfig, this.blockingQueue);
        this.commandExecutorInfoHolder = new CommandExecutorInfoHolder(new CommandExecutorInfo(name));
        this.applicationContext = applicationContext;
    }

    protected ExecutorService initExecutor(CommandExecutorConfig commandExecutorConfig,
        BlockingQueue<Runnable> blockingQueue) {
        return new ThreadPoolExecutor(commandExecutorConfig.getCoreSize(),
            commandExecutorConfig.getMaxPoolSize(), 5, TimeUnit.SECONDS, blockingQueue,
            r -> new Thread(r, "command-executor-" + name), (r, executor) -> {
        });
    }

    protected BlockingQueue<Runnable> initQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public <Response> Future<Response> asyncRun(Command<Response> command) {
        return this.executor.submit(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return internalInvoke(command, new Callable<Response>() {
                    @Override
                    public Response call() throws Exception {
                        return command.execute();
                    }
                });
            }
        });
    }

    @Override
    public <Response> Response syncRun(Command<Response> command) {
        return internalInvoke(command, new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return command.execute();
            }
        });
    }

    @Override
    public String getCommandExecutorName() {
        return name;
    }

    @Override
    public CommandExecutorInfo getCommandExecutorInfo() {
        CommandExecutorInfo commandExecutorInfo = this.commandExecutorInfoHolder.getCommandExecutorInfo();
        if (this.executor instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executor;
            commandExecutorInfo.setMaxPoolSize(threadPoolExecutor.getMaximumPoolSize());
            commandExecutorInfo.setCorePoolSize(threadPoolExecutor.getCorePoolSize());
            commandExecutorInfo.setCompletedTask(threadPoolExecutor.getCompletedTaskCount());
            commandExecutorInfo.setQueueSize(threadPoolExecutor.getQueue().size());
            commandExecutorInfo.setActiveCount(threadPoolExecutor.getActiveCount());
        }
        return commandExecutorInfo;
    }

    @Override
    public <Request, Response> Future<Response> asyncRun(
            Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass, Request request) {
        SpringBeanCommand<Request, Response> beanCommand = SpringUtils.getBean(applicationContext, beanCommandClass);
        Preconditions.checkArgument(beanCommand != null,
            "Not found spring command bean by class:" + beanCommandClass.getName());
        return this.executor.submit(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return internalInvoke(beanCommand, new Callable<Response>() {
                    @Override
                    public Response call() throws Exception {
                        return beanCommand.execute(request);
                    }
                });
            }
        });
    }

    ;

    @Override
    public <Request, Response> Response syncRun(Class<? extends SpringBeanCommand<Request, Response>> beanCommandClass,
        Request request) {
        SpringBeanCommand<Request, Response> beanCommand = SpringUtils.getBean(applicationContext, beanCommandClass);
        Preconditions.checkArgument(beanCommand != null,
            "Not found spring command bean by class:" + beanCommandClass.getName());
        return internalInvoke(beanCommand, new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                return beanCommand.execute(request);
            }
        });
    }

    private static class CommandExecutorInfoHolder {

        public CommandExecutorInfoHolder(CommandExecutorInfo commandExecutorInfo) {
            this.commandExecutorInfo = commandExecutorInfo;
        }

        private CommandExecutorInfo commandExecutorInfo;

        public CommandExecutorInfo getCommandExecutorInfo() {
            return commandExecutorInfo;
        }
    }

}
