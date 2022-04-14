package com.alibaba.chaosblade.box.dao.infrastructure.monitor.trace;


import com.alibaba.chaosblade.box.dao.model.ChangelogDO;

import java.util.Date;

/**
 * @author haibin
 *
 *
 */
public final class ChangelogDOBuilder {
    private ChangelogDO changelogDO;

    private ChangelogDOBuilder() { changelogDO = new ChangelogDO(); }

    public static ChangelogDOBuilder aChangelogDO() { return new ChangelogDOBuilder(); }

    public ChangelogDOBuilder withId(Long id) {
        changelogDO.setId(id);
        return this;
    }

    public ChangelogDOBuilder withGmtCreate(Date gmtCreate) {
        changelogDO.setGmtCreate(gmtCreate);
        return this;
    }

    public ChangelogDOBuilder withGmtModified(Date gmtModified) {
        changelogDO.setGmtModified(gmtModified);
        return this;
    }

    public ChangelogDOBuilder withChangelogId(String changelogId) {
        changelogDO.setChangelogId(changelogId);
        return this;
    }

    public ChangelogDOBuilder withChangeType(String changeType) {
        changelogDO.setChangeType(changeType);
        return this;
    }

    public ChangelogDOBuilder withChangeDescription(String changeDescription) {
        changelogDO.setChangeDescription(changeDescription);
        return this;
    }

    public ChangelogDOBuilder withTypeDescription(String typeDescription) {
        changelogDO.setTypeDescription(typeDescription);
        return this;
    }

    public ChangelogDOBuilder withTargetId(String targetId) {
        changelogDO.setTargetId(targetId);
        return this;
    }

    public ChangelogDOBuilder withTargetDescription(String targetDescription) {
        changelogDO.setTargetDescription(targetDescription);
        return this;
    }

    public ChangelogDOBuilder withTargetType(String targetType) {
        changelogDO.setTargetType(targetType);
        return this;
    }

    public ChangelogDOBuilder withOperatorId(String operatorId) {
        changelogDO.setOperatorId(operatorId);
        return this;
    }

    public ChangelogDOBuilder withOperatorDescription(String operatorDescription) {
        changelogDO.setOperatorDescription(operatorDescription);
        return this;
    }

    public ChangelogDOBuilder withOperatorType(String operatorType) {
        changelogDO.setOperatorType(operatorType);
        return this;
    }

    public ChangelogDOBuilder withPropertyId(String propertyId) {
        changelogDO.setPropertyId(propertyId);
        return this;
    }

    public ChangelogDOBuilder withPropertyDescription(String propertyDescription) {
        changelogDO.setPropertyDescription(propertyDescription);
        return this;
    }

    public ChangelogDOBuilder withPropertyType(String propertyType) {
        changelogDO.setPropertyType(propertyType);
        return this;
    }

    public ChangelogDOBuilder withPropertyChangeType(String propertyChangeType) {
        changelogDO.setPropertyChangeType(propertyChangeType);
        return this;
    }

    public ChangelogDOBuilder but() {
        return aChangelogDO().withId(changelogDO.getId()).withGmtCreate(changelogDO.getGmtCreate()).withGmtModified(
            changelogDO.getGmtModified()).withChangelogId(changelogDO.getChangelogId()).withChangeType(
            changelogDO.getChangeType()).withChangeDescription(changelogDO.getChangeDescription()).withTargetId(
            changelogDO.getTargetId()).withTargetDescription(changelogDO.getTargetDescription()).withTargetType(
            changelogDO.getTargetType()).withOperatorId(changelogDO.getOperatorId()).withOperatorDescription(
            changelogDO.getOperatorDescription()).withOperatorType(changelogDO.getOperatorType()).withPropertyId(
            changelogDO.getPropertyId()).withPropertyDescription(changelogDO.getPropertyDescription()).withPropertyType(
            changelogDO.getPropertyType()).withPropertyChangeType(changelogDO.getPropertyChangeType());

    }

    public ChangelogDO build() { return changelogDO; }
}
