package com.alibaba.chaosblade.box.common.commands;

/**
 * @author haibin
 *
 *
 */
public interface Command<Response> {

    /**
     * 获取命令的执行引擎
     *
     * @return
     */
    public String getCommandExecutorName();

    /**
     * 执行命令
     *
     * @return
     */
    public Response execute();

}
