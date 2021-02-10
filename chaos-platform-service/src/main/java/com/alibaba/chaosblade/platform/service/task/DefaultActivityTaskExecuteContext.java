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
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.chaosblade.platform.service.logback.TaskLogRecord;
import com.alibaba.chaosblade.platform.service.model.experiment.activity.ActivityTaskDTO;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskCompleteListener;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskStartListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author yefei
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@TaskLogRecord
public class DefaultActivityTaskExecuteContext implements ActivityTaskExecuteContext {

    private final ActivityTaskExecutePipeline activityTaskExecutePipeline;

    private final Executor executor;

    private volatile TaskNode<ActivityTask> currentTask;

    private ExperimentTaskStartListener experimentTaskStartListener;

    private ExperimentTaskCompleteListener experimentTaskCompleteListener;

    @Autowired
    private TimerFactory timerFactory;

    private final static AtomicReferenceFieldUpdater<DefaultActivityTaskExecuteContext, TaskNode> atomicReferenceFieldUpdater
            = AtomicReferenceFieldUpdater.newUpdater(DefaultActivityTaskExecuteContext.class, TaskNode.class, "currentTask");

    public DefaultActivityTaskExecuteContext(ActivityTaskExecutePipeline activityTaskExecutePipeline) {
        this(activityTaskExecutePipeline, Executors.newCachedThreadPool());
    }

    public DefaultActivityTaskExecuteContext(ActivityTaskExecutePipeline activityTaskExecutePipeline,
                                             Executor executor) {
        this.activityTaskExecutePipeline = activityTaskExecutePipeline;
        this.executor = executor;
    }

    @Override
    public void fireExecute() {
        TaskNode<ActivityTask> internalTask = null;
        try {
            if (currentTask == null) {
                internalTask = activityTaskExecutePipeline.head();
                if (atomicReferenceFieldUpdater.compareAndSet(this, null, internalTask)) {
                    if (currentTask == null) {
                        return;
                    }
                    if (experimentTaskStartListener != null) {
                        experimentTaskStartListener.notify(this, currentTask.getTask().activityTaskDTO());
                    }
                } else {
                    fireExecute();
                }
            } else {
                internalTask = currentTask.next();
                if (!atomicReferenceFieldUpdater.compareAndSet(this, currentTask, internalTask)) {
                    fireExecute();
                }
                if (currentTask == null) {
                    return;
                }
            }

            if (internalTask.prev() != null && !internalTask.prev().getTask().activityTaskDTO().getPhase()
                    .equals(internalTask.getTask().activityTaskDTO().getPhase())) {
                return;
            }

            final ActivityTask activityTask = internalTask.getTask();

            List<CompletableFuture> futures = CollUtil.newArrayList();
            for (TaskNode<ActivityTask> node = internalTask; node != null; node = node.prev()) {
                CompletableFuture<Void> future = node.getTask().future();
                futures.add(future);
            }

            final TaskNode<ActivityTask> next = internalTask.next();
            if (next != null && next.getTask() instanceof PreviousPhaseActivityTaskListener) {
                final ActivityTask nextTask = next.getTask();

                CompletableFuture.allOf(ArrayUtil.toArray(futures, CompletableFuture.class)).handle((r, e) -> {
                    if (nextTask.preHandler(DefaultActivityTaskExecuteContext.this)) {
                        ((PreviousPhaseActivityTaskListener) nextTask).complete(DefaultActivityTaskExecuteContext.this, e);
                    }
                    return null;
                });
            }

            if (internalTask == activityTaskExecutePipeline.tail()) {
                if (experimentTaskCompleteListener != null) {
                    ActivityTaskDTO activityTaskDTO = activityTask.activityTaskDTO();
                    CompletableFuture.allOf(ArrayUtil.toArray(futures, CompletableFuture.class)).handleAsync((r, e) -> {
                        experimentTaskCompleteListener.notify(this, activityTaskDTO, e);
                        return null;
                    }, executor);
                }
            }

            Long waitOfBefore = activityTask.activityTaskDTO().getWaitOfBefore();
            if (waitOfBefore != null) {
                log.info("演练阶段执行前等待, 任务ID：{}, 子任务ID: {} 等待时间：{} 毫秒",
                        activityTask.activityTaskDTO().getExperimentTaskId(),
                        activityTask.activityTaskDTO().getActivityTaskId(),
                        waitOfBefore);
                timerFactory.getTimer().newTimeout(timeout ->
                                executor.execute(() -> executeActivityTask(activityTask)),
                        waitOfBefore,
                        TimeUnit.MILLISECONDS);
            } else {
                executor.execute(() -> executeActivityTask(activityTask));
            }
        } catch (Throwable throwable) {
            if (internalTask != null) {
                internalTask.getTask().postHandler(this, throwable);
            }
        }
    }

    private void executeActivityTask(ActivityTask activityTask) {
        try {
            if (activityTask.preHandler(DefaultActivityTaskExecuteContext.this)) {
                activityTask.handler(DefaultActivityTaskExecuteContext.this);
            }
        } catch (Exception e) {
            activityTask.postHandler(this, e);
        }
    }

    @Override
    public TaskNode<ActivityTask> currentTask() {
        return currentTask;
    }

    @Override
    public ActivityTaskExecutePipeline pipeline() {
        return activityTaskExecutePipeline;
    }

    @Override
    public Executor executor() {
        return executor;
    }

    @Override
    public Logger getContextLogger() {
        return log;
    }

    @Override
    public void addExperimentTaskStartListener(ExperimentTaskStartListener experimentTaskStartListener) {
        this.experimentTaskStartListener = experimentTaskStartListener;

    }

    @Override
    public void addExperimentTaskCompleteListener(ExperimentTaskCompleteListener experimentTaskCompleteListener) {
        this.experimentTaskCompleteListener = experimentTaskCompleteListener;
    }
}
