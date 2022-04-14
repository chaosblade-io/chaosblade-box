package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

/**
 * @author sunju
 *
 */
@UtilityClass
public class MyBatisUtil {

    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 1000;
    public static final int PAGE_1 = 1;

    public static <T> IPage<T> getPage(int pageNo, int pageSize) {
        if (pageNo < PAGE_1) {
            pageNo = PAGE_1;
        }

        if (pageSize < MIN_PAGE_SIZE) {
            pageSize = MIN_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        return new Page<>(pageNo, pageSize);
    }

}
