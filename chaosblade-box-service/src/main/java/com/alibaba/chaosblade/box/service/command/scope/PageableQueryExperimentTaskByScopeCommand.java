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

package com.alibaba.chaosblade.box.service.command.scope;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.dao.model.ExperimentHostRelationDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.repository.ExperimentHostRelationRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTaskRepository;
import com.alibaba.chaosblade.box.service.model.scope.PageableScopeExperimentTaskQueryRequest;
import com.alibaba.chaosblade.box.service.model.scope.ScopeExperimentTask;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Component
public class PageableQueryExperimentTaskByScopeCommand
    extends SpringBeanCommand<
        PageableScopeExperimentTaskQueryRequest, PageableResponse<ScopeExperimentTask>> {

  @Autowired private ExperimentHostRelationRepository experimentHostRelationRepository;

  @Autowired private ExperimentTaskRepository experimentTaskRepository;
  @Autowired private ExperimentRepository experimentRepository;

  @Override
  public PageableResponse<ScopeExperimentTask> execute(
      PageableScopeExperimentTaskQueryRequest pageableScopeExperimentTaskQueryRequest) {
    String configuration = pageableScopeExperimentTaskQueryRequest.getConfigurationId();
    if (Strings.isNullOrEmpty(configuration)) {
      return PageableResponse.of(0, 0);
    }
    IPage<ExperimentHostRelationDO> experimentHostRelationDOIPage =
        experimentHostRelationRepository.findExperimentTaskByConfigurationIdOrderByGmtCreateDesc(
            configuration,
            pageableScopeExperimentTaskQueryRequest.getPage(),
            pageableScopeExperimentTaskQueryRequest.getSize());
    PageableResponse<ScopeExperimentTask> pageableResponse =
        PageableResponse.of(
            pageableScopeExperimentTaskQueryRequest.getPage(),
            pageableScopeExperimentTaskQueryRequest.getSize());
    pageableResponse.data(
        experimentHostRelationDOIPage.getRecords().stream()
            .map(
                new Function<ExperimentHostRelationDO, ScopeExperimentTask>() {
                  @Override
                  public ScopeExperimentTask apply(
                      ExperimentHostRelationDO experimentHostRelationDO) {
                    return convert(experimentHostRelationDO);
                  }
                })
            .filter(
                new Predicate<ScopeExperimentTask>() {
                  @Override
                  public boolean test(ScopeExperimentTask scopeExperimentTask) {
                    return scopeExperimentTask != null;
                  }
                })
            .collect(Collectors.toList()));
    pageableResponse.total(experimentHostRelationDOIPage.getTotal());
    return pageableResponse;
  }

  private ScopeExperimentTask convert(ExperimentHostRelationDO experimentHostRelationDO) {
    String experimentTaskId = experimentHostRelationDO.getOuterId();
    ExperimentTaskDO experimentTaskDO =
        experimentTaskRepository.findById(experimentTaskId).orElse(null);
    if (experimentTaskDO == null) {
      return null;
    }
    ScopeExperimentTask scopeExperimentTask = new ScopeExperimentTask();
    scopeExperimentTask.setName(experimentTaskDO.getName());
    if (Strings.isNullOrEmpty(scopeExperimentTask.getName())) {
      experimentRepository
          .findById(experimentTaskDO.getExperimentId())
          .ifPresent(experimentDO -> scopeExperimentTask.setName(experimentDO.getName()));
    }
    scopeExperimentTask.setStartTime(experimentTaskDO.getGmtCreate());
    scopeExperimentTask.setEndTime(experimentTaskDO.getGmtEnd());
    scopeExperimentTask.setResult(experimentTaskDO.getResultEnum());
    scopeExperimentTask.setState(experimentTaskDO.getStateEnum());
    scopeExperimentTask.setExperimentId(experimentTaskDO.getExperimentId());
    scopeExperimentTask.setTaskId(experimentTaskDO.getTaskId());
    return scopeExperimentTask;
  }
}
