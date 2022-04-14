package com.alibaba.chaosblade.box.common.experiment.activity.execute;

import com.alibaba.chaosblade.box.common.common.CloseableCommand;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author haibin.lhb
 *
 * 
 */
public class TaskQueue implements TaskQueueLifecycle {

    private WatchDog watchDog;

    private static Integer MAX_WAITING_TIME_MILLS = 3 * 60 * 1000;

    private static Integer MAX_RUNNING_TIME_MILLS = 5 * 60 * 1000;

    public TaskQueue() {
        watchDog = new WatchDog(this);
    }

    @Override
    public void init() {
        watchDog.start();
    }

    @Override
    public void close() {
        watchDog.stop();
    }

    private AtomicInteger finishedCount = new AtomicInteger(0);

    /**
     * 队列状态
     */
    @Data
    public static class QueueStats {
        private Integer blocking;
        private Integer waiting;
        private Integer finished;
        private Integer hang;
    }

    public QueueStats getQueueStats() {
        QueueStats queueStats = new QueueStats();
        queueStats.setBlocking(blockingItems.size());
        queueStats.setHang(hangItems.size());
        queueStats.setWaiting(waitingItems.size());
        queueStats.setFinished(finishedCount.get());
        return queueStats;
    }

    private transient final ReentrantLock lock = new ReentrantLock();

    /**
     * 移除等待队列中的任务
     *
     * @param activityTaskFutureTask
     */
    public void removeWaitingItem(ActivityTaskFutureTask activityTaskFutureTask) {
        for (int i = 0; i < waitingItems.size(); i++) {
            WaitingItem waitingItem = waitingItems.get(0);
            if (waitingItem.getId().equals(activityTaskFutureTask.getId())) {
                waitingItem.leave(this);
            }
        }
    }

    /**
     * 移除正在运行中的任务
     *
     * @param activityTaskFutureTask
     */
    public void removeRunningItem(ActivityTaskFutureTask activityTaskFutureTask) {
        //正在运行中的任务
        for (int i = 0; i < runningItems.size(); i++) {
            RunningItem runningItem = runningItems.get(0);
            if (runningItem.getId().equals(activityTaskFutureTask.getId())) {
                runningItem.leave(this);
            }
        }
        for (int i = 0; i < hangItems.size(); i++) {
            HangItem hangItem = hangItems.get(0);
            if (hangItem.getId().equals(activityTaskFutureTask.getId())) {
                hangItem.leave(this);
            }
        }
        finishedCount.incrementAndGet();
    }

    /**
     * 根据任务Id来获取队列对象
     *
     * @param experimentTaskId
     * @return
     */
    public QueueItem findRunningItemById(String experimentTaskId) {
        QueueItem queueItem = hangItems.get(experimentTaskId);
        if (queueItem == null) {
            queueItem = runningItems.get(experimentTaskId);
        }
        return queueItem;
    }

    /**
     * watchDog，用来定时维护队列状态
     */
    private static class WatchDog extends TimerTask {

        private WeakReference<TaskQueue> queueWeakReference;

        private ScheduledExecutorService scheduledExecutorService;

        public WatchDog(TaskQueue taskQueue) {
            this.queueWeakReference = new WeakReference<>(taskQueue);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    return new Thread(r, "Queue-watch-dog");
                }
            });
        }

        public void start() {
            scheduledExecutorService.scheduleAtFixedRate(this, 0, 3, TimeUnit.SECONDS);
        }

        @Override
        public void run() {
            TaskQueue taskQueue = queueWeakReference.get();
            if (taskQueue != null) {
                taskQueue.maintain();
            } else {
                cancel();
            }
        }

        public void stop() {
            if (!scheduledExecutorService.isShutdown()) {
                scheduledExecutorService.shutdown();
            }
        }
    }

    private void maintain() {
        lock.lock();
        try {
            //正在运行中的任务
            for (int i = 0; i < runningItems.size(); i++) {
                RunningItem runningItem = runningItems.get(0);
                if (satisfyHangCondition(runningItem)) {
                    HangItem hangItem = new HangItem(runningItem);
                    hangItem.enter(this);
                    runningItem.leave(this);
                }
            }
            //首先判断下waiting的任务
            for (int i = 0; i < waitingItems.size(); i++) {
                WaitingItem waitingItem = waitingItems.get(0);
                if (satisfyBlockingCondition(waitingItem)) {
                    BlockingItem blockingItem = new BlockingItem(waitingItem);
                    blockingItem.enter(this);
                    waitingItem.leave(this);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 是否满足block的条件,当一个处于等待状态的演练长时间没有被运行，就会标记为block
     *
     * @param waitingItem
     * @return
     */
    private boolean satisfyBlockingCondition(WaitingItem waitingItem) {
        return waitingItem.getWaitingTime() >= MAX_WAITING_TIME_MILLS;
    }

    /**
     * 是否满足hang的条件，当一个任务的持续时间超出一定限制时候，就会被判断为hang
     *
     * @param runningItem
     * @return
     */
    private boolean satisfyHangCondition(RunningItem runningItem) {
        return runningItem.getRunningDuration() >= MAX_RUNNING_TIME_MILLS;
    }

    /**
     * 等待去运行的任务列表,当一个任务被加入到队列里面的，但是还没有被调度运行时候
     */
    private final ItemList<WaitingItem> waitingItems = new ItemList<>();

    /**
     * 被阻塞的任务列表,当一个任务长时间没有被调度到
     */
    private final ItemList<BlockingItem> blockingItems = new ItemList<>();

    /**
     * 正在运行的任务列表,当一个任务开始运行
     */
    private final ItemList<RunningItem> runningItems = new ItemList<>();

    /**
     * 当一个任务运行时间过长，就会处于hang
     */
    private final ItemList<HangItem> hangItems = new ItemList<>();

    private static Logger Logger = LoggerFactory.getLogger("task-execute");

    private final List<QueueListener> queueListenerList = new ArrayList<>();

    public void addQueueListener(QueueListener queueListener) {
        if (queueListener == null) { return; }
        queueListenerList.add(queueListener);
    }

    public static abstract class QueueItem implements CloseableCommand {
        @Getter
        public final ActivityTaskFutureTask task;

        protected QueueItem(ActivityTaskFutureTask task) {
            this.task = task;
        }

        public String getId() {
            return task.getId();
        }

        /**
         * add item to quque
         *
         * @param taskQueue
         */
        public abstract void enter(TaskQueue taskQueue);

        /**
         * remove from queue
         *
         * @param taskQueue
         */
        public abstract void leave(TaskQueue taskQueue);

        @Override
        public void close() {
            task.cancel(true);
        }
    }

    public static class BlockingItem extends QueueItem {

        private WaitingItem waitingItem;

        protected BlockingItem(WaitingItem waitingItem) {
            super(waitingItem.getTask());
            this.waitingItem = waitingItem;
        }

        @Override
        public void enter(TaskQueue taskQueue) {
            if (taskQueue.blockingItems.add(this)) {
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onEnterBlocking(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onEnterBlocking failed while processing " + this, throwable);
                    }
                }
            }
        }

        @Override
        public void leave(TaskQueue taskQueue) {
            if (taskQueue.blockingItems.remove(this)) {
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onLeaveBlocking(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onLeaveBlocking failed while processing " + this, throwable);
                    }
                }
            }
        }
    }

    /**
     * waiting item object
     */
    public static final class WaitingItem extends QueueItem implements Comparable<WaitingItem> {

        private Long addedTime;

        protected WaitingItem(ActivityTaskFutureTask task) {
            super(task);
        }

        public Long getWaitingTime() {
            return System.currentTimeMillis() - addedTime;
        }

        @Override
        public void enter(TaskQueue taskQueue) {
            if (taskQueue.waitingItems.add(this)) {
                this.addedTime = System.currentTimeMillis();
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onEnterWaiting(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onEnterWaiting failed while processing " + this, throwable);
                    }
                }
            }
        }

        @Override
        public void leave(TaskQueue taskQueue) {
            if (taskQueue.waitingItems.remove(this)) {
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onLeaveWaiting(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onEnterWaiting failed while processing " + this, throwable);
                    }
                }
            }
        }

        @Override
        public int compareTo(@NotNull WaitingItem o) {
            return this.getId().compareTo(o.getId());
        }
    }

    public static class RunningItem extends QueueItem {

        private Long startRunning;

        private Long endTime;

        protected RunningItem(ActivityTaskFutureTask task) {
            super(task);
        }

        public Long getRunningDuration() {
            return endTime == null ? System.currentTimeMillis() - startRunning : endTime - startRunning;
        }

        @Override
        public void enter(TaskQueue taskQueue) {
            if (taskQueue.runningItems.add(this)) {
                this.startRunning = System.currentTimeMillis();
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onEnterRunning(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onEnterRunning failed while processing " + this, throwable);
                    }
                }
            }
        }

        @Override
        public void leave(TaskQueue taskQueue) {
            if (taskQueue.runningItems.remove(this)) {
                this.endTime = System.currentTimeMillis();
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onLeaveRunning(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onLeaveRunning failed while processing " + this, throwable);
                    }
                }
            }
        }
    }

    /**
     * {@link ArrayList} of {@link QueueItem} with more convenience methods.
     */
    public static class ItemList<T extends QueueItem> extends ArrayList<T> {
        public T get(String taskId) {
            for (T item : this) {
                if (item.getId().equals(taskId)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public synchronized boolean add(T t) {
            return super.add(t);
        }

        @Override
        public synchronized boolean remove(Object o) {
            return super.remove(o);
        }

        public List<T> getAll(String taskId) {
            List<T> result = new ArrayList<>();
            for (T item : this) {
                if (item.getId().equals(taskId)) {
                    result.add(item);
                }
            }
            return result;
        }

        public boolean containsKey(String task) {
            return get(task) != null;
        }

        public void put(Task task, T item) {
            assert item.task.equals(task);
            add(item);
        }

        public ItemList<T> values() {
            return this;
        }

    }

    public static final class HangItem extends QueueItem {

        public HangItem(RunningItem runningItem) {
            super(runningItem.getTask());
        }

        @Override
        public void enter(TaskQueue taskQueue) {
            if (taskQueue.hangItems.add(this)) {
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onEnterHang(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onEnterHang failed while processing " + this, throwable);
                    }
                }
            }
        }

        @Override
        public void leave(TaskQueue taskQueue) {
            if (taskQueue.hangItems.remove(this)) {
                for (QueueListener queueListener : taskQueue.queueListenerList) {
                    try {
                        queueListener.onLeaveHang(this);
                    } catch (Throwable throwable) {
                        Logger.warn("QueueListener onLeaveHang failed while processing " + this, throwable);
                    }
                }
            }
        }
    }

}
