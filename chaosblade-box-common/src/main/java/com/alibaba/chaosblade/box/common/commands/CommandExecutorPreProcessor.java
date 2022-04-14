package com.alibaba.chaosblade.box.common.commands;

/**
 * @author haibin.lhb
 *
 *
 */
public interface CommandExecutorPreProcessor {

    /**
     * after new commandExecutor
     *
     * @param commandExecutor
     */
    public void preProcess(CommandExecutor commandExecutor);
}
