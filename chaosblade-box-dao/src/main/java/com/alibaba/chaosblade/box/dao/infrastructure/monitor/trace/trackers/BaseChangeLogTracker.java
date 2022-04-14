package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.trackers;

import com.alibaba.chaosblade.box.common.infrastructure.constant.ChangelogTypes;
import com.alibaba.chaosblade.box.common.infrastructure.constant.CommonConstant;
import com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace.ChangelogTracker;
import com.alibaba.chaosblade.box.dao.infrastructure.service.ChangelogService;
import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author haibin
 *
 *
 */
public abstract class BaseChangeLogTracker implements ChangelogTracker {

    @Autowired
    private ChangelogService changelogService;

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
