package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author haibin.lhb
 *
 * 
 */
@Component
@Slf4j
public class LogPrintQueueListener extends QueueListener {

    @Override
    public void onEnterWaiting(TaskQueue.WaitingItem waitingItem) {
        log.debug("EnterWaitingQueue:" + waitingItem.getId());
    }

    @Override
    public void onLeaveWaiting(TaskQueue.WaitingItem waitingItem) {
        log.debug("LeaveWaitingQueue:" + waitingItem.getId());
    }

    @Override
    public void onEnterRunning(TaskQueue.RunningItem runningItem) {
        log.debug("EnterRunningQueue:" + runningItem.getId());
    }

    @Override
    public void onLeaveRunning(TaskQueue.RunningItem runningItem) {
        log.debug("LeaveRunningQueue:" + runningItem.getId() + ",duration:" + runningItem.getRunningDuration());
    }

    @Override
    public void onLeaveBlocking(TaskQueue.BlockingItem blockingItem) {
        super.onLeaveBlocking(blockingItem);
    }
}
