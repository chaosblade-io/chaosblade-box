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

package com.alibaba.chaosblade.platform.service.task;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.chaosblade.platform.cmmon.executor.ExecutorFactory;
import com.alibaba.chaosblade.platform.cmmon.executor.ThreadPoolExecutorFactory;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskCompleteListener;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskStartListener;
import com.alibaba.chaosblade.platform.service.task.stateless.ActivityTaskHandlerStrategyContext;
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

    @Autowired
    private ActivityTaskHandlerStrategyContext activityTaskHandlerStrategyContext;

    @Autowired
    private TimerFactory timerFactory;

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
                experimentTaskStartListener.notify(this, internalTask.getTask().activityTaskDTO());
            }

            if (internalTask.prev() != null && !internalTask.prev().getTask().activityTaskDTO().getPhase()
                    .equals(internalTask.getTask().activityTaskDTO().getPhase())) {
                return;
            }

            final ActivityTask activityTask = internalTask.getTask();

            List<CompletableFuture<Void>> futures = CollUtil.newArrayList();
            for (TaskNode<ActivityTask> node = internalTask; node != null; node = node.prev()) {
                CompletableFuture<Void> future = node.getTask().future();
                futures.add(future);
            }

            final TaskNode<ActivityTask> next = internalTask.next();
            if (next != null) {
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).handle((r, e) -> {
                    executeActivityTask(next.getTask());
                    return null;
                });
            }

            if (internalTask == activityTaskExecutePipeline.tail()
                    && taskCompleteListenerMap.get(activityTaskExecutePipeline) != null) {
                ExperimentTaskCompleteListener experimentTaskCompleteListener = taskCompleteListenerMap.get(activityTaskExecutePipeline);
                ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).handleAsync((r, e) -> {
                    experimentTaskCompleteListener.notify(this, activityTaskDTO, e);
                    return null;
                }, executor);
            }

            Long waitOfBefore = activityTask.activityTaskDTO().getWaitOfBefore();
            if (waitOfBefore != null) {
                log.info("演练阶段执行前等待, 任务ID：{}, 子任务ID: {} 等待时间：{} 毫秒",
                        activityTask.activityTaskDTO().getExperimentTaskId(),
                        activityTask.activityTaskDTO().getActivityTaskId(),
                        waitOfBefore);
                timerFactory.getTimer().newTimeout(timeout ->
                                executor.execute(() -> {
                                    try {
                                        executeActivityTask(activityTask);
                                    } catch (Throwable throwable) {
                                        activityTaskHandlerStrategyContext.postHandle(
                                                internalTask.getTask(),
                                                throwable);
                                    }
                                }),
                        waitOfBefore,
                        TimeUnit.MILLISECONDS);
            } else {
                executor.execute(() -> {
                    try {
                        executeActivityTask(activityTask);
                    } catch (Throwable throwable) {
                        activityTaskHandlerStrategyContext.postHandle(
                                internalTask.getTask(),
                                throwable);
                    }
                });
            }
        } catch (Throwable throwable) {
            activityTaskHandlerStrategyContext.postHandle(
                    internalTask.getTask(),
                    throwable);
        }
    }

    @Override
    public void executeActivityTask(ActivityTask activityTask) {
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
    public Logger getContextLogger() {
        return log;
    }

}
