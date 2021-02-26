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

import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskCompleteListener;
import com.alibaba.chaosblade.platform.service.task.listener.ExperimentTaskStartListener;
import org.slf4j.Logger;

import java.util.concurrent.Executor;

/**
 * @author yefei
 */
public interface ActivityTaskExecuteContext {

    /**
     * @return
     */
    Logger getContextLogger();

    /**
     *
     */
    void fireExecute(ActivityTaskExecutePipeline activityTaskExecutePipeline);

    /**
     * @return
     */
    Executor executor();

    /**
     *
     * @return
     */
    Timer timer();

    /**
     * @param experimentTaskStartListener
     */
    void addExperimentTaskStartListener(ActivityTaskExecutePipeline activityTaskExecutePipeline,
                                        ExperimentTaskStartListener experimentTaskStartListener);


    /**
     * @param experimentTaskCompleteListener
     */
    void addExperimentTaskCompleteListener(ActivityTaskExecutePipeline activityTaskExecutePipeline,
                                           ExperimentTaskCompleteListener experimentTaskCompleteListener);

}
