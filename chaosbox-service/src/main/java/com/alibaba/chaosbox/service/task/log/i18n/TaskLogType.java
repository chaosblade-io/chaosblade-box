/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")),
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

package com.alibaba.chaosbox.service.task.log.i18n;

/**
 * @author yefei
 */
public enum TaskLogType {

    START_EXPERIMENT("start.experiment"),

    START_METRIC("start.metric"),

    NO_METRIC("no.metric"),

    GET_METRIC_ERROR("get.metric.error"),

    CHECK_TASK_STATUS("check.task.status"),

    CHECK_SUB_TASK_STATUS("check.sub.task.status"),

    EXECUTE_SUB_TASK("execute.sub.task"),

    TASK_UNABLE_EXECUTE("task.unable.execute"),

    SUB_TASK_UNABLE_EXECUTE("sub.task.unable.execute"),

    SUB_EXECUTE_SUCCESS("sub.execute.success"),

    SUB_EXECUTE_ERROR("sub.execute.error"),

    SUB_EXECUTE_EXECUTING("sub.execute.executing"),

    EXPERIMENT_WAIT_OF_BEFORE("experiment.wait.of.before"),

    EXPERIMENT_WAIT_OF_AFTER("experiment.wait.of.after"),

    RECOVER_CHECK_SUB_TASK_STATUS("recover.check.sub.task.status"),

    RECOVER_PHASE_UNABLE_EXECUTE("recover.phase.unable.execute"),

    SUB_TASK_COMPLETE("sub.task.complete"),

    EXPERIMENT_RECOVER_SUCCESS("experiment.recover.success"),

    EXPERIMENT_RECOVER_ERROR("experiment.recover.error"),

    EXPERIMENT_PHASE_TRANSFER("experiment.phase.transfer"),

    ;

    private String code;

    TaskLogType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
