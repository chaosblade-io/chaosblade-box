package com.alibaba.chaosblade.box.common.commands;

import java.util.Map;
import lombok.Data;

/** @author haibin */
@Data
public class CommandExecutorPoolConfig {

  private Map<String, CommandExecutorConfig> executor;
}
