package com.alibaba.chaosblade.box.dao.model.base;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Data
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
public class PageableResponse<T> {

    /**
     * 当前页
     */
    final int page;

    /**
     * 每页数据条数
     */
    final int pageSize;

    /**
     * 总页数
     */
    long pages;

    /**
     * 总数据条数
     */
    long total;

    /**
     * 当前页的数据
     */
    Collection<T> data;

    public boolean isHasMore() {
        return sizeOf(data) >= pageSize || page < pages;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getPages() {
        return pages;
    }

    public long getTotal() {
        return total;
    }

    public Collection<T> getData() {
        return data;
    }

    public static <T> PageableResponse<T> empty() {
        return PageableResponse.of(1, 1);
    }

    public static <T> PageableResponse<T> of(int page, int pageSize, Collection<T> data) {
        return PageableResponse.<T>of(page, pageSize).data(data);
    }

    public static <T> PageableResponse<T> of(long page, long pageSize, Collection<T> data, long pages, long total) {
        return PageableResponse.<T>of(Integer.parseInt(page + ""), Integer.parseInt(pageSize + "")).data(data).pages(
            pages).total(total);
    }

    public static <F, T> PageableResponse<T> clone(PageableResponse<F> pageableResponse, Collection<T> data) {
        return PageableResponse.<T>of(pageableResponse.page(), pageableResponse.pageSize())
            .pages(pageableResponse.pages())
            .total(pageableResponse.total())
            .data(data);
    }

    private int sizeOf(Collection<T> collection) {
        return null == collection ? 0 : collection.size();
    }
}
