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

package com.alibaba.chaosbox.invoker.http.constant;

/**
 * @author Changjun Xiao
 */
public interface Blade {
    /**
     * 混沌实验准备命令
     */
    String PREPARE = "prepare";
    /**
     * 混沌实验创建命令
     */
    String CREATE = "create";
    /**
     * 混沌实验销毁命令
     */
    String DESTROY = "destroy";
    /**
     * 混沌实验撤销命令
     */
    String REVOKE = "revoke";
    /**
     * 混沌实验状态命令
     */
    String STATUS = "status";

    /**
     * 查询混沌实验所需参数
     */
    String QUERY = "query";
}
