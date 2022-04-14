package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.commands.interceptor.CommandInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class CommandInterceptorSetterExecutorPreProcessor implements CommandExecutorPreProcessor {

    @Autowired
    private List<CommandInterceptor> commandInterceptorList;

    @Override
    public void preProcess(CommandExecutor commandExecutor) {
        commandExecutor.addCommandInterceptors(commandInterceptorList);
    }
}
