/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosbox.service.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosbox.common.executor.ExecutorFactory;
import com.alibaba.chaosbox.common.executor.ThreadPoolExecutorFactory;
import com.alibaba.chaosbox.common.TaskLogRecord;
import com.alibaba.chaosbox.common.utils.timer.HashedWheelTimer;
import com.alibaba.chaosbox.common.utils.timer.Timer;
import com.alibaba.chaosbox.service.task.listener.ExperimentTaskCompleteListener;
import com.alibaba.chaosbox.service.task.listener.ExperimentTaskStartListener;
import com.alibaba.chaosbox.service.task.log.i18n.TaskLogType;
import com.alibaba.chaosbox.service.task.log.i18n.TaskLogUtil;
import com.alibaba.chaosbox.service.task.stateless.ActivityTaskHandlerStrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yefei
 */
@Slf4j
@Component
@TaskLogRecord
public class DefaultActivityTaskExecuteContext implements ActivityTaskExecuteContext, InitializingBean {

    private Executor executor;

    protected Timer timer;

    @Autowired
    private ActivityTaskHandlerStrategyContext activityTaskHandlerStrategyContext;

    private final Map<ActivityTaskExecutePipeline, ExperimentTaskStartListener> taskStartListenerMap = new ConcurrentHashMap<>();

    private final Map<ActivityTaskExecutePipeline, ExperimentTaskCompleteListener> taskCompleteListenerMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        ExecutorFactory executorFactory = new ThreadPoolExecutorFactory();
        executor = executorFactory.createExecutorService(new ThreadFactory() {

            final AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setName("EXPERIMENT-TASK-THREAD-" + atomicInteger.getAndIncrement());
                return thread;
            }
        });

        timer = new HashedWheelTimer(r -> {
            Thread thread = new Thread(r);
            thread.setName("EXPERIMENT-TASK-THREAD-TIMER");
            return thread;
        });
    }

    @Override
    public void fireExecute(ActivityTaskExecutePipeline activityTaskExecutePipeline) {
        TaskNode<ActivityTask> internalTask = activityTaskExecutePipeline.getCurrentTask();
        if (internalTask == null) {
            return;
        }
        try {
            if (internalTask == activityTaskExecutePipeline.head() &&
                    taskStartListenerMap.get(activityTaskExecutePipeline) != null) {
                ExperimentTaskStartListener experimentTaskStartListener = taskStartListenerMap.get(activityTaskExecutePipeline);
                experimentTaskStartListener.notify(this, internalTask.getTask());
            }
            List<CompletableFuture<Void>> futures = CollUtil.newArrayList();
            for (TaskNode<ActivityTask> node = internalTask; node != null; node = node.prev()) {
                CompletableFuture<Void> future = node.getTask().future();
                futures.add(future);
            }

            TaskNode<ActivityTask> next = internalTask.next();
            if (next != null) {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).handle((r, e) -> {
                    executeActivityTask0(next.getTask());
                    return null;
                });
            }

            ActivityTask activityTask = internalTask.getTask();

            if (internalTask == activityTaskExecutePipeline.tail()
                    && taskCompleteListenerMap.get(activityTaskExecutePipeline) != null) {
                ExperimentTaskCompleteListener experimentTaskCompleteListener = taskCompleteListenerMap.get(activityTaskExecutePipeline);
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).handleAsync((r, e) -> {
                    experimentTaskCompleteListener.notify(this, activityTask, e);
                    return null;
                }, executor).handleAsync((r, e) -> {
                    if (e != null) {
                        log.error("complete listener notify error!", e);
                    }
                    return null;
                });
            }

            if (internalTask.prev() != null) {
                String prePhase = internalTask.prev().getTask().getPhase();
                String nextPhase = internalTask.getTask().getPhase();
                if (!prePhase.equals(nextPhase)) {
                    TaskLogUtil.info(log, TaskLogType.EXPERIMENT_PHASE_TRANSFER, activityTask.getExperimentTaskId(),
                            prePhase, nextPhase, String.valueOf(activityTask.getExperimentTaskId()));
                    return;
                }
            }
            executeActivityTask(activityTaskExecutePipeline, internalTask);
        } catch (Throwable throwable) {
            log.error("fireExecute error!", throwable);
            activityTaskHandlerStrategyContext.postHandle(
                    internalTask.getTask(),
                    throwable);

        }
    }

    private void executeActivityTask(ActivityTaskExecutePipeline activityTaskExecutePipeline, TaskNode<ActivityTask> internalTask) {

        ActivityTask activityTask = internalTask.getTask();
        Long waitOfBefore = activityTask.getWaitOfBefore();
        if (waitOfBefore != null) {

            TaskLogUtil.info(log, TaskLogType.EXPERIMENT_WAIT_OF_BEFORE, activityTask.getExperimentTaskId(),
                    String.valueOf(activityTask.getActivityTaskId()),
                    String.valueOf(waitOfBefore)
            );

            timer.newTimeout(timeout ->
                            executor.execute(() -> {
                                try {
                                    executeActivityTask0(activityTask);
                                } catch (Throwable throwable) {
                                    activityTaskHandlerStrategyContext.postHandle(
                                            internalTask.getTask(),
                                            throwable);
                                }
                            }),
                    waitOfBefore,
                    TimeUnit.MILLISECONDS);
        } else {
            // todo Ensuring transaction commit
            executor.execute(() -> {
                try {
                    executeActivityTask0(activityTask);
                } catch (Throwable throwable) {
                    activityTaskHandlerStrategyContext.postHandle(
                            internalTask.getTask(),
                            throwable);
                }
            });
        }
    }

    public void executeActivityTask0(ActivityTask activityTask) {
        try {
            boolean b = activityTaskHandlerStrategyContext.preHandle(activityTask);
            if (b) {
                activityTaskHandlerStrategyContext.handle(activityTask);
            }
        } catch (Exception e) {
            activityTaskHandlerStrategyContext.postHandle(activityTask, e);
        }
    }

    @Override
    public void addExperimentTaskStartListener(ActivityTaskExecutePipeline activityTaskExecutePipeline, ExperimentTaskStartListener experimentTaskStartListener) {
        taskStartListenerMap.put(activityTaskExecutePipeline, experimentTaskStartListener);
    }

    @Override
    public void addExperimentTaskCompleteListener(ActivityTaskExecutePipeline activityTaskExecutePipeline, ExperimentTaskCompleteListener experimentTaskCompleteListener) {
        taskCompleteListenerMap.put(activityTaskExecutePipeline, experimentTaskCompleteListener);
    }

    @Override
    public Executor executor() {
        return executor;
    }

    @Override
    public Timer timer() {
        return timer;
    }

    @Override
    public Logger getContextLogger() {
        return log;
    }

}
