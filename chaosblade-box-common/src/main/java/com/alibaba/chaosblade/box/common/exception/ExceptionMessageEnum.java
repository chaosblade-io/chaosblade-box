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

package com.alibaba.chaosblade.box.common.exception;

/**
 * @author yefei
 */
public enum ExceptionMessageEnum {

    ERROR(6000, "系统异常"),
    ID_IS_NULL(6001, "ID IS NULL"),

    // DEVICE
    DEVICE_TYPE_NOT_FOUNT(6100, "不支持的设备类型"),
    DEVICE_NOT_FOUNT(6101, "机器不存在"),

    // EXPERIMENT
    EXPERIMENT_NOT_FOUNT(6200, "演练不存在"),
    EXPERIMENT_TASK_NOT_FOUNT(6201, "演练任务不存在"),
    EXPERIMENT_TASK_STOPPING(6202, "演练任务停止中"),
    EXPERIMENT_TASK_END(6203, "演练任务已结束"),
    EXPERIMENT_NAME_IS_NULL(6204, "演练名称不能为空"),
    EXPERIMENT_DEVICE_IS_NULL(6205, "演练机器不能为空"),
    EXPERIMENT_SCENE_IS_NULL(6206, "请选择演练场景"),
    EXPERIMENT_PRE_NO_FINISH(6207, "上一次演练任务未结束"),
    EXPERIMENT_SUB_TASK_NOT_FOUNT(6208, "子任务不存在"),

    // METRIC
    METRIC_NOT_FOUNT(6300, "监控类目不存在"),
    METRIC_NO_SERVICE(6301, "没有接入的监控类目"),

    // SCENE
    SCENE_IS_NULL(6400, "场景不存在"),
    SCENE_EXISTS(6401, "场景已经存在"),

    // PROBES
    PROBES_NO_FOUND(6500, "探针不存在"),
    PROBES_UNINSTALL_FAIL(6501, "探针卸载失败"),

    // TOOLS
    CHAOS_TOOLS_EXISTS(6600, "已安装演练工具"),
    CHAOS_TOOLS_UPDATE_FAIL(6601, "演练工具更新失败"),
    CHAOS_TOOLS_UNINSTALL_FAIL(6602, "演练工具卸载失败"),
    CHAOS_TOOLS_INSTALL_FAIL(6602, "演练工具安装失败"),

    // EXPERIMENT FLOW
    FLOW_GROUP_NOT_EXISTS(6700, "流程组不存在"),

    // INVOKER
    INVOKER_NOT_EXISTS(7000, "不存在对应的执行器")
    ;

    public Integer code;

    public String message;

    ExceptionMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
