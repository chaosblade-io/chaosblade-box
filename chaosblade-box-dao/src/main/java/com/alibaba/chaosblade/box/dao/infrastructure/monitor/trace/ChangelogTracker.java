package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace;


import com.alibaba.chaosblade.box.dao.model.ChangelogDO;

/**
 * @author sunju
 *
 */
public interface ChangelogTracker {

    /**
     * targetType
     *
     * @param targetType
     * @return
     */
    boolean support(String targetType);

    void track(ChangelogDO changelogDO);

}
