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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author yefei
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultActivityTaskExecutePipeline implements ActivityTaskExecutePipeline {

    /**
     * first task
     */
    private InternalActivityTaskNode head;

    /**
     * last task
     */
    private InternalActivityTaskNode tail;

    @Override
    public TaskNode<ActivityTask> head() {
        return head;
    }

    @Override
    public TaskNode<ActivityTask> tail() {
        return tail;
    }

    @Override
    public void addList(ActivityTask activityTask) {
        Long activityTaskId = activityTask.activityTaskDTO().getActivityTaskId();
        if (activityTaskId == null) {
            throw new BizException("add activity task, task id is null");
        }
        InternalActivityTaskNode TaskNode = new InternalActivityTaskNode(activityTask, activityTaskId);
        if (head == null) {
            head = TaskNode;
        } else {
            tail.next = TaskNode;
            TaskNode.prev = tail;
        }
        tail = TaskNode;
    }

    private static class InternalActivityTaskNode implements TaskNode<ActivityTask>  {

        private final Long activityTaskId;

        private final ActivityTask activityTask;

        private InternalActivityTaskNode prev;

        private InternalActivityTaskNode next;

        public InternalActivityTaskNode(ActivityTask activityTask, Long activityTaskId) {
            this.activityTask = activityTask;
            this.activityTaskId = activityTaskId;
        }

        @Override
        public TaskNode<ActivityTask> next(){
            return next;
        }

        @Override
        public TaskNode<ActivityTask> prev(){
            return prev;
        }

        @Override
        public ActivityTask getTask() {
            return activityTask;
        }

        public Long getActivityTaskId() {
            return activityTaskId;
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
}
