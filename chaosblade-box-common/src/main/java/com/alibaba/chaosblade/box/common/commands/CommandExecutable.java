package com.alibaba.chaosblade.box.common.commands;

import java.util.concurrent.Future;

/**
 * @author haibin
 * 
 *
 */
public interface CommandExecutable {
    /**
     * 异步运行
     *
     * @param command
     * @return
     */
    public <Response> Future<Response> asyncRun(Command<Response> command);

    /**
     * 同步运行
     *
     * @param command
     * @return
     */
    public <Response> Response syncRun(Command<Response> command);
}
