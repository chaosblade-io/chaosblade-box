package com.alibaba.chaosblade.box.common.commands.decorator;


import com.alibaba.chaosblade.box.common.commands.BasePoolCommand;
import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.commands.CommandDecorator;

/**
 * @author haibin
 *
 *
 */
public class CommandExecutorPoolSetterCommandDecorator implements CommandDecorator {

    private CommandBus commandBus;

    public CommandExecutorPoolSetterCommandDecorator(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @Override
    public <Response> Command<Response> decorate(Command<Response> sourceCommand) {
        if (sourceCommand instanceof BasePoolCommand) {
            ((BasePoolCommand)sourceCommand).setCommandExecutorPool(commandBus);
        }
        return sourceCommand;
    }
}
