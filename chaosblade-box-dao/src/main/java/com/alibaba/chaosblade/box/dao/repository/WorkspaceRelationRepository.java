package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.common.domain.workspace.WorkspaceRelationQuery;
import com.alibaba.chaosblade.box.common.infrastructure.constant.WorkspaceRelationTypes;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.WorkspaceRelationMapper;
import com.alibaba.chaosblade.box.dao.model.WorkspaceRelationDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
public class WorkspaceRelationRepository implements IRepository<String, WorkspaceRelationDO> {

    @Resource
    private WorkspaceRelationMapper workspaceRelationMapper;

    public List<WorkspaceRelationDO> find(WorkspaceRelationQuery query) {
        return workspaceRelationMapper.selectList(buildQuery(query));
    }

    private QueryWrapper<WorkspaceRelationDO> buildQuery(WorkspaceRelationQuery query) {
        return new QueryWrapper<WorkspaceRelationDO>()
            .in(null != query.getRelationIds() && !query.getRelationIds().isEmpty(), "relation_id",
                query.getRelationIds())
            .in(null != query.getOuterIds() && !query.getOuterIds().isEmpty(), "outer_id", query.getOuterIds())
//            .eq(null != query.getUserRole(), "user_role", query.getUserRole())
//            .eq(null != query.getMemberRole(), "member_role", query.getMemberRole())
            .in(null != query.getWorkspaceIds() && !query.getWorkspaceIds().isEmpty(), "workspace_id",
                query.getWorkspaceIds())
            .eq(!Strings.isNullOrEmpty(query.getRelationType()), "relation_type", query.getRelationType())
            .eq(!Strings.isNullOrEmpty(query.getNamespace()), "namespace", query.getNamespace())
            .eq(!Strings.isNullOrEmpty(query.getExperimentCreator()), "experiment_creator",
                query.getExperimentCreator())
            .eq(query.isExcludeDeleted(), "is_delete", false);
    }

    public int deleteWorkspaceRelation(WorkspaceRelationQuery workspaceRelationQuery, boolean logical) {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = buildQuery(workspaceRelationQuery);
        WorkspaceRelationDO workspaceRelationDO = new WorkspaceRelationDO();
        workspaceRelationDO.setIsDelete(true);
        if (logical) {
            return workspaceRelationMapper.update(workspaceRelationDO, queryWrapper);
        } else {
            return workspaceRelationMapper.delete(queryWrapper);
        }
    }

    public Integer count(WorkspaceRelationQuery query) {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = new QueryWrapper<WorkspaceRelationDO>()
            .in(null != query.getRelationIds() && !query.getRelationIds().isEmpty(), "relation_id",
                query.getRelationIds())
            .in(null != query.getOuterIds() && !query.getOuterIds().isEmpty(), "outer_id", query.getOuterIds())
//            .eq(null != query.getUserRole(), "user_role", query.getUserRole())
//            .eq(null != query.getMemberRole(), "member_role", query.getMemberRole())
            .in(null != query.getWorkspaceIds() && !query.getWorkspaceIds().isEmpty(), "workspace_id",
                query.getWorkspaceIds())
            .eq(!Strings.isNullOrEmpty(query.getRelationType()), "relation_type", query.getRelationType())
            .eq(!Strings.isNullOrEmpty(query.getNamespace()), "namespace", query.getNamespace())
            .eq("is_delete", false);

        return workspaceRelationMapper.selectCount(queryWrapper);
    }

    public IPage<WorkspaceRelationDO> findExperiments(String workspaceId, int page, int size) {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = new QueryWrapper<WorkspaceRelationDO>()
            .eq("workspace_id", workspaceId)
            .eq("relation_type", WorkspaceRelationTypes.EXPERIMENT)
            .eq("is_delete", false);
        return workspaceRelationMapper.selectPage(MyBatisUtil.getPage(page, size), queryWrapper);
    }

    public boolean addExperimentCreator(String workspaceId, String experimentId, String experimentCreator) {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = new QueryWrapper<WorkspaceRelationDO>()
            .eq("workspace_id", workspaceId)
            .eq("relation_type", WorkspaceRelationTypes.EXPERIMENT)
            .eq("outer_id", experimentId)
            .eq("is_delete", false).isNull("experiment_creator");
        WorkspaceRelationDO update = new WorkspaceRelationDO();
        update.setExperimentCreator(experimentCreator);
        return workspaceRelationMapper.update(update, queryWrapper) > 0;
    }

    public List<WorkspaceRelationDO> findExperimentsWhereExperimentCreatorIsNull() {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = new QueryWrapper<WorkspaceRelationDO>()
            .eq("relation_type", WorkspaceRelationTypes.EXPERIMENT)
            .eq("is_delete", false).isNull("experiment_creator");
        return workspaceRelationMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<WorkspaceRelationDO> findById(String relationId) {
        QueryWrapper<WorkspaceRelationDO> queryWrapper = new QueryWrapper<WorkspaceRelationDO>()
            .eq("relation_id", relationId)
            .eq("is_delete", false);
        return Optional.ofNullable(workspaceRelationMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean update(WorkspaceRelationDO workspaceRelation) {
        UpdateWrapper<WorkspaceRelationDO> updateWrapper = new UpdateWrapper<WorkspaceRelationDO>()
            .eq("relation_id", workspaceRelation.getRelationId());
        return workspaceRelationMapper.update(workspaceRelation, updateWrapper) > 0;
    }

    @Override
    public boolean add(WorkspaceRelationDO workspaceRelation) {
        return workspaceRelationMapper.insert(workspaceRelation) > 0;
    }

}
