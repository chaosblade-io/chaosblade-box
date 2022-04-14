package com.alibaba.chaosblade.box.common.common.domain.search;

import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class SearchResult<T> {

    /**
     * 当前页数
     */
    private int current;

    /**
     * 每页的数目
     */
    private int size;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 总数目
     */
    private long total;

    /**
     * 搜索结果列表
     */
    private List<T> content;

}
