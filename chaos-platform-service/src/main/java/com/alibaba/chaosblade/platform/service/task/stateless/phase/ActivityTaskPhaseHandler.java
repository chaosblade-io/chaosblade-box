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

package com.alibaba.chaosblade.platform.service.task.stateless.phase;

import com.alibaba.chaosblade.platform.cmmon.DeviceMeta;
import com.alibaba.chaosblade.platform.invoker.RequestCommand;
import com.alibaba.chaosblade.platform.service.task.ActivityTask;
import com.alibaba.chaosblade.platform.service.task.ActivityTaskExecuteContext;
import com.alibaba.chaosblade.platform.service.task.stateless.ActivityTaskHandler;

/**
 * @author yefei
 */
public interface ActivityTaskPhaseHandler extends ActivityTaskHandler {

    @Override
    default void handle(ActivityTaskExecuteContext context, ActivityTask activityTask) {
        throw new UnsupportedOperationException("phase handler unsupported");
    }

    /**
     * @param activityTask
     * @param deviceMeta
     * @return
     */
    RequestCommand requestCommand(ActivityTask activityTask, DeviceMeta deviceMeta);
}
