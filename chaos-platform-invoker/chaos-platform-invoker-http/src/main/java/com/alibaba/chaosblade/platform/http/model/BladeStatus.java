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

package com.alibaba.chaosblade.platform.http.model;

/**
 * @author Changjun Xiao
 */
public interface BladeStatus {
    /**
     * 执行错误
     */
    String ERROR = "Error";
    /**
     * 正在挂载中
     */
    String RUNNING = "Running";
    /**
     * 已撤销（已卸载）
     */
    String REVOKED = "Revoked";
    /**
     * 实验执行成功
     */
    String SUCCESS = "Success";
    /**
     * 实验已销毁
     */
    String DESTROYED = "Destroyed";

}
