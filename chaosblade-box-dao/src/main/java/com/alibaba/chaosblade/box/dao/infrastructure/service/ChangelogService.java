package com.alibaba.chaosblade.box.dao.infrastructure.service;

import com.alibaba.chaosblade.box.dao.model.ChangelogDO;
import com.alibaba.chaosblade.box.dao.model.base.PageableQueryWrapper;
import com.alibaba.chaosblade.box.dao.model.base.PageableResponse;
import com.alibaba.chaosblade.box.dao.query.ChangelogQuery;
import com.alibaba.chaosblade.box.dao.repository.ChangelogRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sunju
 *
 */
@Service
public class ChangelogService {

    @Resource
    private ChangelogRepository changelogRepository;

    public void addChangelog(ChangelogDO changelog) {
        changelogRepository.add(changelog);
    }




    public PageableResponse<ChangelogDO> getChangelogs(int pageNo, int pageSize, ChangelogQuery query) {
        return changelogRepository.findChangelogPageable(
            PageableQueryWrapper.of(query)
                .pageNumber(pageNo)
                .pageSize(pageSize)
                .asc(false)
                .column(PageableQueryWrapper.TYPE_ORDER_BY, "gmt_create")
        );
    }

}
