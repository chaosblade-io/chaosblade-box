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

package com.alibaba.chaosblade.box.service.infrastructure.convertor;

import com.alibaba.chaosblade.box.common.commands.CommandBus;
import com.alibaba.chaosblade.box.common.common.domain.task.ExperimentTask;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.response.UserExperiment;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.service.UserService;
import com.alibaba.chaosblade.box.service.auth.perimission.PermissionResult;
import com.alibaba.chaosblade.box.service.command.experiment.ExperimentAppRiskCommand;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Converter;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin.lhb */
@Component
public class ExperimentDOTOUserExperimentConverter extends Converter<ExperimentDO, UserExperiment> {

  @Resource private TagManager tagManager;

  @Autowired private CommandBus commandBus;

  @Autowired private ExperimentTaskRepository experimentTaskRepository;

  @Autowired private UserService userService;

  @Override
  protected UserExperiment doForward(ExperimentDO experimentDO) {
    UserExperiment userExperiment = new UserExperiment();
    userExperiment.setExperimentId(experimentDO.getExperimentId());
    userExperiment.setCreateTime(experimentDO.getGmtCreate());
    userExperiment.setName(experimentDO.getName());
    userExperiment.setState(experimentDO.getExperimentStateEnum());
    userExperiment.setSchedulerConfig(experimentDO.getSchedulerConfig());
    userExperiment.setTags(
        tagManager.findTagsByExperimentId(experimentDO.getExperimentId()).stream()
            .map(TagDO::getName)
            .collect(Collectors.toList()));
    userExperiment.setMiniAppDesc(
        Strings.isNullOrEmpty(experimentDO.getMiniAppDesc())
            ? null
            : JSON.parseObject(
                experimentDO.getMiniAppDesc(), new TypeReference<ArrayList<String>>() {}));
    if (experimentDO.getExperimentTaskId() != null) {
      setTaskState(userExperiment, experimentDO.getExperimentTaskId());
    }
    userExperiment.setCreator(experimentDO.getUserId());
    userExperiment.setExperimentAppRisks(
        commandBus
            .syncRun(ExperimentAppRiskCommand.class, experimentDO.getExperimentId())
            .getResult());
    userExperiment.setPermission(PermissionResult.ALL.getPermission());
    return userExperiment;
  }

  @Override
  protected ExperimentDO doBackward(UserExperiment userExperiment) {
    return null;
  }

  private void setTaskState(UserExperiment userExperiment, String taskId) {
    experimentTaskRepository
        .findById(taskId)
        .ifPresent(
            experimentTaskDO -> {
              ExperimentTask experimentTask = new ExperimentTask();
              experimentTask.setTaskId(experimentTaskDO.getTaskId());
              experimentTask.setCreator(userService.getUserByUserId(experimentTaskDO.getUserId()));
              experimentTask.setResult(experimentTaskDO.getResultEnum());
              experimentTask.setState(experimentTaskDO.getStateEnum());
              experimentTask.setStartTime(experimentTaskDO.getGmtCreate());
              userExperiment.setTask(experimentTask);
            });
  }
}
