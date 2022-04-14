package com.alibaba.chaosblade.box.common.commands;

/**
 * @author haibin.lhb
 *
 *
 */
public class ImmutableCommand<Response> implements Command<Response> {

    private Command<Response> command;

    public ImmutableCommand(Command<Response> command) {
        this.command = command;
    }

    @Override
    public String getCommandExecutorName() {
        return this.command.getCommandExecutorName();
    }

    @Override
    public Response execute() {
        throw new UnsupportedOperationException("Readonly command");
    }
}
