package com.alibaba.chaosblade.box.common.commands;

import lombok.Data;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Data
public class CommandExecutorPoolConfig {

    private Map<String, CommandExecutorConfig> executor;

}
