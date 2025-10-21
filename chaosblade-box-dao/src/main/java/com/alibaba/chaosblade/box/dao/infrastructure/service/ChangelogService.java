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

package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ChangelogQuery;
import com.alibaba.chaosblade.box.dao.repository.ChangelogRepository;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/** @author sunju */
@Service
public class ChangelogService {

  @Resource private ChangelogRepository changelogRepository;

  public void addChangelog(ChangelogDO changelog) {
    changelogRepository.add(changelog);
  }

  public PageableResponse<ChangelogDO> getChangelogs(
      int pageNo, int pageSize, ChangelogQuery query) {
    return changelogRepository.findChangelogPageable(
        PageableQueryWrapper.of(query)
            .pageNumber(pageNo)
            .pageSize(pageSize)
            .asc(false)
            .column(PageableQueryWrapper.TYPE_ORDER_BY, "gmt_create"));
  }
}
