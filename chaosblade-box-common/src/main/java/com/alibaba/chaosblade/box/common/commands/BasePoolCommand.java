package com.alibaba.chaosblade.box.common.commands;


public abstract class BasePoolCommand<Response> implements Command<Response> {

    public void setCommandExecutorPool(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    private CommandBus commandBus;

    public CommandBus getPool() {
        return this.commandBus;
    }

}
