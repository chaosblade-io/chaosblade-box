package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;


import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentRelation;
import com.alibaba.chaosblade.box.common.common.domain.experiment.ExperimentSchedulerConfig;
import com.alibaba.chaosblade.box.common.common.domain.experiment.WorkspaceShortInfo;
import com.alibaba.chaosblade.box.common.common.enums.ExperimentStateEnum;

import java.util.Date;
import java.util.List;

/**
 * @author haibin
 *
 *
 */

public class ExperimentBasicInfo {

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public ExperimentStateEnum getState() {
        return state;
    }

    public void setState(ExperimentStateEnum state) {
        this.state = state;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getExperimentTaskId() {
        return experimentTaskId;
    }

    public void setExperimentTaskId(String experimentTaskId) {
        this.experimentTaskId = experimentTaskId;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public ExperimentSchedulerConfig getSchedulerConfig() {
        return schedulerConfig;
    }

    public void setSchedulerConfig(ExperimentSchedulerConfig schedulerConfig) {
        this.schedulerConfig = schedulerConfig;
    }

    private String experimentId;

    private String name;

    private String description;

    private List<String> tags;

    private List<String> miniAppDesc;

    private List<WorkspaceShortInfo> workspaces;

    private List<ExperimentRelation> relations;

    private Date gmtCreate;
    private String level;
    private String ownerUserId;

    private ExperimentStateEnum state;

    private String namespace;

    private String experimentTaskId;

    private Date gmtModified;

    private String outerId;

    private ExperimentSchedulerConfig schedulerConfig;

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    private Integer source;

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getMiniAppDesc() {
        return miniAppDesc;
    }

    public void setMiniAppDesc(List<String> miniAppDesc) {
        this.miniAppDesc = miniAppDesc;
    }

    public List<WorkspaceShortInfo> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<WorkspaceShortInfo> workspaces) {
        this.workspaces = workspaces;
    }

    public List<ExperimentRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<ExperimentRelation> relations) {
        this.relations = relations;
    }

    public String getLevel() {

        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOwnerUserId() {

        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
}
