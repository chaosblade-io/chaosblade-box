/*
 * Copyright 2025 The ChaosBlade Authors
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

package com.alibaba.chaosblade.box.dao.infrastructure.experiment.task;

import com.alibaba.chaosblade.box.dao.infrastructure.event.ChaosEventDispatcher;
import com.alibaba.chaosblade.box.dao.infrastructure.manager.MiniAppTaskManager;
import com.alibaba.chaosblade.box.dao.model.ActivityTaskDO;
import com.alibaba.chaosblade.box.dao.repository.ActivityTaskRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin.lhb */
public class ActivityTaskDomainWrapper {

  private static Logger logger = LoggerFactory.getLogger(ActivityTaskDomainWrapper.class);

  public ActivityTaskDomainWrapper(ActivityTaskDO activityTaskDO) {
    this.activityTaskDO = activityTaskDO;
  }

  public void setActivityTaskDO(ActivityTaskDO activityTaskDO) {
    this.activityTaskDO = activityTaskDO;
  }

  @Getter private ActivityTaskDO activityTaskDO;

  @Autowired private ActivityTaskRepository activityTaskRepository;

  @Autowired private MiniAppTaskManager miniAppTaskManager;

  @Autowired private ChaosEventDispatcher chaosEventDispatcher;
}
