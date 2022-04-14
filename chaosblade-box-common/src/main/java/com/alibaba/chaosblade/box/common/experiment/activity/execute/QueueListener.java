package com.alibaba.chaosblade.box.common.experiment.activity.execute;

/**
 * @author haibin.lhb
 *
 *
 */
public abstract class QueueListener {

    public void onEnterWaiting(TaskQueue.WaitingItem waitingItem) {}

    ;

    public void onLeaveWaiting(TaskQueue.WaitingItem waitingItem) {}

    public void onEnterRunning(TaskQueue.RunningItem runningItem) {}

    public void onLeaveRunning(TaskQueue.RunningItem runningItem) {

    }

    public void onEnterBlocking(TaskQueue.BlockingItem blockingItem) {

    }

    public void onLeaveBlocking(TaskQueue.BlockingItem blockingItem) {

    }

    public void onEnterHang(TaskQueue.HangItem hangItem) {
    }

    public void onLeaveHang(TaskQueue.HangItem hangItem) {

    }
}
