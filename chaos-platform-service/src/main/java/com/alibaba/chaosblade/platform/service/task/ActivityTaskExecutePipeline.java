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

import com.alibaba.chaosblade.platform.cmmon.exception.BizException;
import org.checkerframework.checker.units.qual.A;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author yefei
 */
public class ActivityTaskExecutePipeline {

    private Long experimentTaskId;

    /**
     * first task
     */
    private InternalActivityTaskNode head;

    /**
     * last task
     */
    private InternalActivityTaskNode tail;

    /**
     * current task
     */
    private volatile TaskNode<ActivityTask> currentTask;

    private volatile boolean isEnd;

    private final static AtomicReferenceFieldUpdater<ActivityTaskExecutePipeline, TaskNode> atomicReferenceFieldUpdater
            = AtomicReferenceFieldUpdater.newUpdater(ActivityTaskExecutePipeline.class, TaskNode.class, "currentTask");

    public TaskNode<ActivityTask> getCurrentTask() {
        if (isEnd) {
            return null;
        }
        if (currentTask == null) {
            if (head == null) {
                isEnd = true;
                return null;
            }
            if (!atomicReferenceFieldUpdater.compareAndSet(this, null, head)) {
                getCurrentTask();
            }
        } else {
            if (!atomicReferenceFieldUpdater.compareAndSet(this, currentTask, currentTask.next())) {
                getCurrentTask();
            }
            if (currentTask == null) {
                isEnd = true;
                return null;
            }
        }
        return currentTask;
    }

    public TaskNode<ActivityTask> head() {
        return head;
    }

    public TaskNode<ActivityTask> tail() {
        return tail;
    }

    public void addLast(ActivityTask activityTask) {
        if (experimentTaskId == null) {
            experimentTaskId = activityTask.activityTaskDTO().getExperimentTaskId();
        }
        Long activityTaskId = activityTask.activityTaskDTO().getActivityTaskId();
        if (activityTaskId == null) {
            throw new BizException("add activity task, task id is null");
        }
        activityTask.setActivityTaskExecutePipeline(this);
        InternalActivityTaskNode TaskNode = new InternalActivityTaskNode(activityTask, activityTaskId);
        if (head == null) {
            head = TaskNode;
        } else {
            tail.next = TaskNode;
            TaskNode.prev = tail;
        }
        tail = TaskNode;
    }

    private static class InternalActivityTaskNode implements TaskNode<ActivityTask> {

        private final Long activityTaskId;

        private final ActivityTask activityTask;

        private InternalActivityTaskNode prev;

        private InternalActivityTaskNode next;

        public InternalActivityTaskNode(ActivityTask activityTask, Long activityTaskId) {
            this.activityTask = activityTask;
            this.activityTaskId = activityTaskId;
        }

        @Override
        public TaskNode<ActivityTask> next() {
            return next;
        }

        @Override
        public TaskNode<ActivityTask> prev() {
            return prev;
        }

        @Override
        public ActivityTask getTask() {
            return activityTask;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            InternalActivityTaskNode that = (InternalActivityTaskNode) o;
            return activityTaskId.equals(that.activityTaskId);
        }

        @Override
        public int hashCode() {
            return activityTaskId.hashCode();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityTaskExecutePipeline that = (ActivityTaskExecutePipeline) o;
        return Objects.equals(experimentTaskId, that.experimentTaskId);
    }

    @Override
    public int hashCode() {
        return experimentTaskId != null ? experimentTaskId.hashCode() : 0;
    }
}
