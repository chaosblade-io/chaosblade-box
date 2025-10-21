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

package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.ChangelogTracker;
import com.alibaba.chaosblade.box.dao.infrastructure.service.ChangelogService;
import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import org.springframework.beans.factory.annotation.Autowired;

/** @author haibin */
public abstract class BaseChangeLogTracker implements ChangelogTracker {

  @Autowired private ChangelogService changelogService;

  @Override
  public void track(ChangelogDO changelogDO) {
    if (changelogDO.getPropertyDescription() == null) {
      changelogDO.setPropertyDescription(CommonConstant.BLANK);
    }
    if (changelogDO.getPropertyId() == null) {
      changelogDO.setPropertyId(CommonConstant.BLANK);
    }
    if (changelogDO.getPropertyType() == null) {
      changelogDO.setPropertyType(CommonConstant.BLANK);
    }
    if (changelogDO.getPropertyChangeType() == null) {
      changelogDO.setPropertyChangeType(CommonConstant.BLANK);
    }
    if (changelogDO.getChangeDescription() == null) {
      changelogDO.setChangeDescription(CommonConstant.BLANK);
    }
    changelogService.addChangelog(changelogDO);
  }

  public static String getChangeActionTypeDescription(String changeTypeStr) {
    for (ChangelogTypes.ChangeActionType changeType : ChangelogTypes.ChangeActionType.values()) {
      if (changeType.getName().equalsIgnoreCase(changeTypeStr)) {
        return changeType.getDesc();
      }
    }
    return changeTypeStr;
  }
}
