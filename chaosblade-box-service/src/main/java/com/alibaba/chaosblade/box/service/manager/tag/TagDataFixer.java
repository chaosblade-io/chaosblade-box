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

package com.alibaba.chaosblade.box.service.manager.tag;

import com.alibaba.chaosblade.box.common.infrastructure.util.concurrent.ThreadPool;
import com.alibaba.chaosblade.box.dao.model.ExperimentDO;
import com.alibaba.chaosblade.box.dao.model.ExperimentTagRelationDO;
import com.alibaba.chaosblade.box.dao.model.TagDO;
import com.alibaba.chaosblade.box.dao.repository.ExperimentRepository;
import com.alibaba.chaosblade.box.dao.repository.ExperimentTagRepository;
import com.alibaba.chaosblade.box.dao.repository.TagRepository;
import com.google.common.base.Strings;
import java.util.List;
import java.util.function.Consumer;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用来修复原有的tag数据
 *
 * @author haibin.lhb
 */
@Data
@Component
public class TagDataFixer implements InitializingBean {

  @Autowired private TagRepository tagRepository;

  @Autowired private ExperimentTagRepository experimentTagRepository;

  @Autowired private ExperimentRepository experimentRepository;

  @Autowired private ThreadPool threadPool;

  @Override
  public void afterPropertiesSet() throws Exception {
    threadPool
        .executor("tag-fixer")
        .execute(
            new Runnable() {
              @Override
              public void run() {
                List<ExperimentTagRelationDO> experimentTagRelationDOList =
                    experimentTagRepository.findByTagTypeAndUserIdIsNull(TagDO.TAG_TYPE_EXPERIMENT);
                experimentTagRelationDOList.forEach(
                    experimentTagRelationDO ->
                        tagRepository
                            .findById(experimentTagRelationDO.getTagId())
                            .ifPresent(
                                new Consumer<TagDO>() {
                                  @Override
                                  public void accept(TagDO tagDO) {
                                    if (Strings.isNullOrEmpty(
                                        experimentTagRelationDO.getTagName())) {
                                      experimentTagRelationDO.setTagName(tagDO.getName());
                                    }
                                    experimentRepository
                                        .findById(experimentTagRelationDO.getRelationId())
                                        .ifPresent(
                                            new Consumer<ExperimentDO>() {
                                              @Override
                                              public void accept(ExperimentDO experimentDO) {
                                                experimentTagRelationDO.setUserId(
                                                    experimentDO.getUserId());
                                              }
                                            });
                                    experimentTagRepository.update(experimentTagRelationDO);
                                  }
                                }));
              }
            });
  }
}
