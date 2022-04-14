package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.common.domain.PageQueryResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @author haibin
 *
 *
 */
public class PageQueryResponseConverter {

    private static PageQueryResponseConverter INSTANCE = new PageQueryResponseConverter();

    private PageQueryResponseConverter() {}

    public <T> PageQueryResponse<T> from(IPage<T> iPage) {
        PageQueryResponse<T> pageQueryResponse = new PageQueryResponse<>();
        pageQueryResponse.setContent(iPage.getRecords());
        pageQueryResponse.setCurrentPage(iPage.getCurrent());
        pageQueryResponse.setPageSize(iPage.getSize());
        pageQueryResponse.setPages(iPage.getPages());
        pageQueryResponse.setTotal(iPage.getTotal());
        return pageQueryResponse;
    }
}
