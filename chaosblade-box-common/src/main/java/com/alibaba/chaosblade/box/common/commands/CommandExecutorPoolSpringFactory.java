package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.commands.decorator.CommandExecutorPoolSetterCommandDecorator;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.DefaultCommandExecutor;
import com.alibaba.chaosblade.box.common.infrastructure.util.SpringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author haibin
 *
 *
 */
public class CommandExecutorPoolSpringFactory implements FactoryBean<CommandBus>,
    ApplicationContextAware {

    private CommandBus commandBus;

    private ApplicationContext applicationContext;

    @Autowired
    private List<CommandExecutorPreProcessor> commandExecutorPreProcessors;

    @Override
    public CommandBus getObject() throws Exception {
        if (commandBus == null) {
            afterPropertiesSet();
        }
        return commandBus;
    }

    @Override
    public Class<?> getObjectType() {
        return CommandBus.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        CommandExecutorPoolConfig commandExecutorPoolConfig = loadConfig();
        List<CommandExecutor> commandExecutors = initCommandExecutors(commandExecutorPoolConfig.getExecutor());
        this.commandBus = new CommandBus(commandExecutors);
        initCommandDecorators(commandBus);
        preProcessCommandExecutor(commandExecutors);
    }

    private void preProcessCommandExecutor(List<CommandExecutor> commandExecutors) {
        for (CommandExecutor commandExecutor : commandExecutors) {
            for (CommandExecutorPreProcessor commandExecutorPreProcessor : commandExecutorPreProcessors) {
                commandExecutorPreProcessor.preProcess(commandExecutor);
            }
        }
    }

    private CommandExecutorPoolConfig loadConfig() throws IOException {
        return JSON.parseObject(
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("command_executor_config.json")),
            CommandExecutorPoolConfig.class);
    }

    private List<CommandExecutor> initCommandExecutors(
        Map<String, CommandExecutorConfig> executor) {
        List<CommandExecutor> commandExecutors = new ArrayList<>();
        executor.entrySet().forEach(new Consumer<Entry<String, CommandExecutorConfig>>() {
            @Override
            public void accept(Entry<String, CommandExecutorConfig> stringCommandExecutorConfigEntry) {
                CommandExecutorConfig commandExecutorConfig = stringCommandExecutorConfigEntry.getValue();
                CommandExecutor commandExecutor = null;
                if (commandExecutorConfig.getExecutorClass() != null) {
                    commandExecutor = initCommandExecutorByClass(commandExecutorConfig.getExecutorClass(),
                        stringCommandExecutorConfigEntry.getKey(), stringCommandExecutorConfigEntry.getValue());
                } else {
                    commandExecutor = new DefaultCommandExecutor(stringCommandExecutorConfigEntry.getKey(),
                        applicationContext,
                        stringCommandExecutorConfigEntry.getValue());
                }
                commandExecutors.add(commandExecutor);
            }
        });
        SpringUtils.getBeans(applicationContext, CommandExecutor.class).forEach(new Consumer<CommandExecutor>() {
            @Override
            public void accept(CommandExecutor commandExecutor) {
                commandExecutors.add(commandExecutor);
            }
        });
        return commandExecutors;
    }

    private void initCommandDecorators(CommandBus commandBus) {
        commandBus.addCommandDecorator(new CommandExecutorPoolSetterCommandDecorator(commandBus));
        SpringUtils.getBeans(applicationContext, CommandDecorator.class).forEach(new Consumer<CommandDecorator>() {
            @Override
            public void accept(CommandDecorator commandDecorator) {
                commandBus.addCommandDecorator(commandDecorator);
            }
        });
    }

    private CommandExecutor initCommandExecutorByClass(String className, String name,
        CommandExecutorConfig commandExecutorConfig) {
        try {
            Class<?> claszz = CommandExecutor.class.getClassLoader().loadClass(className);
            try {
                Constructor<?> constructor = claszz.getConstructor(String.class, ApplicationContext.class,
                    CommandExecutorConfig.class);
                return (CommandExecutor)constructor.newInstance(name, applicationContext, commandExecutorConfig);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("not found constructor", e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found error", e);
        } catch (Exception e) {
            throw new RuntimeException("init object error", e);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
