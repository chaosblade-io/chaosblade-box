package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.domain.workspace.WorkspaceQuery;
import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.WorkspaceMapper;
import com.alibaba.chaosblade.box.dao.model.WorkspaceDO;
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
public class WorkspaceRepository implements IRepository<String, WorkspaceDO> {

    @Resource
    private WorkspaceMapper workspaceMapper;

    public List<WorkspaceDO> find(WorkspaceQuery query) {
        QueryWrapper<WorkspaceDO> queryWrapper = new QueryWrapper<WorkspaceDO>()
            .in(null != query.getWorkspaceIds() && !query.getWorkspaceIds().isEmpty(), "workspace_id",
                query.getWorkspaceIds())
            .eq(!Strings.isNullOrEmpty(query.getUserId()), "user_id", query.getUserId())
            .in(null != query.getUserIds() && !query.getUserIds().isEmpty(), "user_id", query.getUserIds())
            .eq(null != query.getType(), "type", query.getType())
            .eq(!Strings.isNullOrEmpty(query.getName()), "name", query.getName())
//            .eq(null != query.getUserRole(), "user_role", query.getUserRole())
            .eq("is_delete", false);
        return workspaceMapper.selectList(queryWrapper);
    }

    public IPage<WorkspaceDO> findPageable(int page, int size) {
        return workspaceMapper.selectPage(MyBatisUtil.getPage(page, size),
            new QueryWrapper<WorkspaceDO>().eq("is_delete", false));
    }

    public List<WorkspaceDO> findByUserId(String userId, boolean excludeDelete) {
        QueryWrapper<WorkspaceDO> queryWrapper = new QueryWrapper<WorkspaceDO>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq(excludeDelete, "is_deleted", false);
        return workspaceMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<WorkspaceDO> findById(String workspaceId) {
        QueryWrapper<WorkspaceDO> queryWrapper = new QueryWrapper<WorkspaceDO>()
            .eq("workspace_id", workspaceId)
            .eq("is_delete", false);
        return Optional.ofNullable(workspaceMapper.selectOne(queryWrapper));
    }

    @Override
    public boolean update(WorkspaceDO workspace) {
        UpdateWrapper<WorkspaceDO> updateWrapper = new UpdateWrapper<WorkspaceDO>()
            .eq("workspace_id", workspace.getWorkspaceId());
        return workspaceMapper.update(workspace, updateWrapper) > 0;
    }

    @Override
    public boolean add(WorkspaceDO workspace) {
        return workspaceMapper.insert(workspace) > 0;
    }
}
