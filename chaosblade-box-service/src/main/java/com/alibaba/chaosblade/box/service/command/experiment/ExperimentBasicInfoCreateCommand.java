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

package com.alibaba.chaosblade.box.service.command.experiment;

import com.alibaba.chaosblade.box.common.commands.SpringBeanCommand;
import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentBaseInfoRequest;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;
import com.alibaba.chaosblade.box.common.infrastructure.util.CollectionUtil;
import com.alibaba.chaosblade.box.dao.infrastructure.experiment.assembler.ExperimentDOAssembler;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.service.manager.tag.TagManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author haibin */
@Slf4j
@Component
public class ExperimentBasicInfoCreateCommand
    extends SpringBeanCommand<ExperimentBaseInfoRequest, Response<String>> {

  @Autowired private ExperimentRepository experimentRepository;

  @Autowired private TagManager tagManager;

  @Override
  public Response<String> execute(ExperimentBaseInfoRequest experimentBasicInfoRequest) {
    ExperimentDO experimentDO =
        ExperimentDOAssembler.assembleExperimentDO(experimentBasicInfoRequest);
    experimentDO.setOuterId(experimentBasicInfoRequest.getOuterId());
    experimentDO.setState(ExperimentStateEnum.DRAFT.getValue());
    experimentRepository.add(experimentDO);
    handleTags(experimentBasicInfoRequest, experimentDO);
    return Response.okWithData(experimentDO.getExperimentId());
  }

  protected void handleTags(
      ExperimentBaseInfoRequest experimentBasicInfoRequest, ExperimentDO experimentDO) {
    List<String> tags = experimentBasicInfoRequest.getTags();
    if (!CollectionUtil.isNullOrEmpty(tags)) {
      tagManager.addTags(
          experimentBasicInfoRequest.getUserId(),
          experimentDO.getExperimentId(),
          TagDO.TAG_TYPE_EXPERIMENT,
          tags);
    }
  }
}
