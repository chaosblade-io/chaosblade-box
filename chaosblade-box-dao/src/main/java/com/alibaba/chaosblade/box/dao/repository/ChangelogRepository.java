package com.alibaba.chaosblade.box.dao.repository;

import com.alibaba.chaosblade.box.common.infrastructure.util.MyBatisUtil;
import com.alibaba.chaosblade.box.dao.mapper.ChangelogMapper;
import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ChangelogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author sunju
 *
 */
@Repository
public class ChangelogRepository implements IRepository<String, ChangelogDO> {

    @Resource
    private ChangelogMapper changelogMapper;

    @Override
    public Optional<ChangelogDO> findById(String changelogId) {
        return Optional.ofNullable(changelogMapper.selectOne(
            new QueryWrapper<ChangelogDO>()
                .eq("changelog_id", changelogId)
        ));
    }

    public PageableResponse<ChangelogDO> findChangelogPageable(
        PageableQueryWrapper<ChangelogQuery> pageableQueryWrapper) {
        int pageNo = pageableQueryWrapper.pageNumber();
        int pageSize = pageableQueryWrapper.pageSize();
        ChangelogQuery query = pageableQueryWrapper.query();
        ChangelogDO changelogDO = new ChangelogDO();
        changelogDO.setChangeType(query.getActionType());
        changelogDO.setOperatorId(query.getOperatorId());
        changelogDO.setOperatorType(query.getOperatorType());
        changelogDO.setTargetType(query.getTargetType());
        changelogDO.setTargetId(query.getTargetId());
        QueryWrapper<ChangelogDO> queryWrapper = new QueryWrapper<ChangelogDO>(changelogDO);
        if (query.getFrom() != null) {
            queryWrapper.gt("gmt_create", query.getFrom());
        }
        if (query.getTo() != null) {
            queryWrapper.lt("gmt_create", query.getTo());
        }
        queryWrapper.orderByDesc("gmt_create");
        IPage<ChangelogDO> result = changelogMapper.selectPage(
            MyBatisUtil.getPage(pageableQueryWrapper.pageNumber(), pageableQueryWrapper.pageSize()), queryWrapper);
        return PageableResponse.of(pageNo, pageSize, result.getRecords())
            .pages(result.getPages())
            .total(result.getTotal());
    }

    @Override
    public boolean update(ChangelogDO changelog) {
        return changelogMapper.update(
            changelog,
            new UpdateWrapper<ChangelogDO>().eq("changelog_id", changelog.getChangelogId())
        ) > 0;
    }

    @Override
    public boolean add(ChangelogDO changelog) {
        return changelogMapper.insert(changelog) > 0;
    }
}
