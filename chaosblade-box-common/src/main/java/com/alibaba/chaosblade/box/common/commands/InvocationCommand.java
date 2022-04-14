package com.alibaba.chaosblade.box.common.commands;

import lombok.Getter;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author haibin.lhb
 *
 *
 */
@Getter
public class InvocationCommand {

    private Command<?> command;

    private StopWatch stopWatch;

    public InvocationCommand(Command<?> command) {
        this.command = command;
        this.stopWatch = new StopWatch();
    }

    public void start() {
        this.stopWatch.start();
    }

    public void finish() {
        if (this.stopWatch.isStarted()) {
            this.stopWatch.stop();
        }
    }

    public long getCost(TimeUnit timeUnit) {
        return stopWatch.getTime(timeUnit);
    }

    public Command<?> getImmutableCommand() {
        return new ImmutableCommand<>(command);
    }

}
