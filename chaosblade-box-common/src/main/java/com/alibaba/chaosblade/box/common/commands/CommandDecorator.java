package com.alibaba.chaosblade.box.common.commands;

/**
 * @author haibin
 *
 *
 */
public interface CommandDecorator {

    public <Response> Command<Response> decorate(Command<Response> sourceCommand);
}
