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

import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.ExperimentBasicInfo;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.service.WorkspaceService;
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
public class ExperimentToBasicInfoConverter extends Converter<ExperimentDO, ExperimentBasicInfo> {

  @Autowired private TagManager tagManager;

  //    @Autowired
  //    private WorkspaceService workspaceService;
  @Resource private WorkspaceService workspaceService;

  @Override
  protected ExperimentBasicInfo doForward(ExperimentDO experimentDO) {
    String experimentId = experimentDO.getExperimentId();
    ExperimentBasicInfo experimentBasicInfo = new ExperimentBasicInfo();
    experimentBasicInfo.setDescription(experimentDO.getDescription());
    experimentBasicInfo.setExperimentId(experimentId);
    experimentBasicInfo.setName(experimentDO.getName());
    experimentBasicInfo.setOwnerUserId(experimentDO.getOwnerUserId());
    experimentBasicInfo.setGmtCreate(experimentDO.getGmtCreate());
    experimentBasicInfo.setGmtModified(experimentDO.getGmtModified());
    experimentBasicInfo.setSchedulerConfig(experimentDO.getSchedulerConfig());
    experimentBasicInfo.setNamespace(experimentDO.getNamespace());
    //        experimentBasicInfo.setRegionId(experimentDO.getRegionId());
    experimentBasicInfo.setState(experimentDO.getExperimentStateEnum());
    experimentBasicInfo.setExperimentTaskId(experimentDO.getExperimentTaskId());
    experimentBasicInfo.setLevel(experimentDO.getLevel());
    experimentBasicInfo.setSource(experimentDO.getSource());
    experimentBasicInfo.setMiniAppDesc(
        Strings.isNullOrEmpty(experimentDO.getMiniAppDesc())
            ? null
            : JSON.parseObject(
                experimentDO.getMiniAppDesc(), new TypeReference<ArrayList<String>>() {}));
    experimentBasicInfo.setTags(
        tagManager.findTagsByExperimentId(experimentDO.getExperimentId()).stream()
            .map(TagDO::getName)
            .collect(Collectors.toList()));
    experimentBasicInfo.setWorkspaces(
        workspaceService.getWorkspacesShortInfoByExperimentId(experimentDO.getExperimentId()));
    return experimentBasicInfo;
  }

  @Override
  protected ExperimentDO doBackward(ExperimentBasicInfo experimentBasicInfo) {
    throw new UnsupportedOperationException("Not support yet");
  }
}
