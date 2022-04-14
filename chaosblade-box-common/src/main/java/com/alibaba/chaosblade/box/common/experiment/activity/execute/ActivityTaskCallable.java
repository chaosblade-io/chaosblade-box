package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import com.alibaba.chaosblade.box.common.commands.Command;
import com.alibaba.chaosblade.box.common.common.IdentityCommand;

import java.util.concurrent.Callable;

/**
 * @author haibin.lhb
 *
 *
 */
public class ActivityTaskCallable<T> implements Callable<T> {

    private IdentityCommand identityCommand;

    private Command<T> command;

    public ActivityTaskCallable(Command<T> command) {
        this.command = command;
        identityCommand = (IdentityCommand)command;
    }

    public String getId() {
        return identityCommand.getIdentity();
    }

    @Override
    public T call() throws Exception {
        return this.command.execute();
    }
}
