package com.alibaba.chaosblade.box.common.commands;

import lombok.Data;

/**
 * @author haibin
 *
 *
 */
@Data
public class CommandExecutorConfig {

    private int maxPoolSize;

    private int coreSize = 3;

    private String executorClass;
}
