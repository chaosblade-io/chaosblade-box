package com.alibaba.chaosblade.box.dao.model.base;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author sunju
 *
 */
@Data
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "of")
public class PageableQueryWrapper<T> {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 50;
    public static final int TYPE_ORDER_BY = 0;
    public static final int TYPE_GROUP_BY = 1;

    /**
     * raw query conditions
     */
    final T query;

    /**
     * page number, default value is 1
     */
    int pageNumber;

    /**
     * page size, default value is 50
     */
    int pageSize;

    /**
     * order by asc or plainText
     */
    boolean asc;

    /**
     *  columns for order
     */
    String[] orderBy;

    /**
     * columns for groupBy
     */
    String[] groupBy;

    public int pageNumber() {
        if (this.pageNumber <= 0L) {
            return DEFAULT_PAGE_NUMBER;
        }
        return this.pageNumber;
    }

    public int pageSize() {
        if (this.pageSize <= 0L) {
            return DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    public int offset() {
        return (pageNumber() - 1) * pageSize();
    }



    /**
     * add column for order
     *
     * @param column column name
     */
    public PageableQueryWrapper<T> column(int type, String column) {
        if (TYPE_ORDER_BY == type) {
            this.orderBy = append(this.orderBy, column);
        }
        if (TYPE_GROUP_BY == type) {
            this.groupBy = append(this.groupBy, column);
        }
        return this;
    }

    private String[] append(String[] array, String element) {
        if (null == array) {
            return new String[] {element};
        } else {
            String[] copied = new String[array.length + 1];

            System.arraycopy(array, 0, copied, 0, array.length);
            copied[copied.length - 1] = element;

            return copied;
        }
    }
}
