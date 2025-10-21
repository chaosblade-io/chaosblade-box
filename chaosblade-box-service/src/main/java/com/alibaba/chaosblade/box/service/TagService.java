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

package com.alibaba.chaosblade.box.service;

import com.alibaba.chaosblade.box.common.common.domain.Response;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagCreatingRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.tag.TagSearchRequest;
import com.alibaba.chaosblade.box.common.infrastructure.exception.TagException;
import java.util.List;
import java.util.Set;

/** @author sunju */
public interface TagService {

  /**
   * 查询标签
   *
   * @param tagSearchRequest
   * @return
   */
  Response<List<String>> search(TagSearchRequest tagSearchRequest);

  /**
   * save tag entity to database
   *
   * @param request create tag request data
   * @return tag id (generate by uuid random)
   * @throws TagException throw exception when required field missing or insert db failed
   */
  Response<String> addTag(TagCreatingRequest request) throws TagException;

  /**
   * 用户演练标签
   *
   * @param userId
   * @return
   */
  Response<List<String>> listExperimentTags(String userId);

  /**
   * 根据类型和关键字搜索用户标签
   *
   * @param tagSearchRequest
   * @return
   */
  Response<List<String>> listUserTagsByType(TagSearchRequest tagSearchRequest);

  /**
   * 搜索用户和系统经验库标签
   *
   * @param tagSearchRequest
   * @return
   */
  Response<Set<String>> listUserAndAdminTags(TagSearchRequest tagSearchRequest);
}
