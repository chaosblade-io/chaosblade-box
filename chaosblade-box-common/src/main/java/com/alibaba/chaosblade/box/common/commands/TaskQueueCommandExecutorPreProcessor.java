package com.alibaba.chaosblade.box.common.commands;

import com.alibaba.chaosblade.box.common.experiment.activity.execute.ActivityTaskFlowExecutionCommandExecutor;
import com.alibaba.chaosblade.box.common.experiment.activity.execute.QueueListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
public class TaskQueueCommandExecutorPreProcessor implements CommandExecutorPreProcessor {

    @Autowired
    private List<QueueListener> queueListenerList;

    @Override
    public void preProcess(CommandExecutor commandExecutor) {
        if (commandExecutor instanceof ActivityTaskFlowExecutionCommandExecutor) {
            for (QueueListener queueListener : queueListenerList) {
                ((ActivityTaskFlowExecutionCommandExecutor)commandExecutor).getTaskQueue().addQueueListener(
                    queueListener);
            }
        }
    }
}
