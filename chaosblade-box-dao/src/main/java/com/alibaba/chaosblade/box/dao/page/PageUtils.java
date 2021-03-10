/*
 * Copyright 1999-2021 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.chaosblade.box.dao.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author yefei
 */
public class PageUtils {

    private static final ThreadLocal<Page> THREAD_LOCAL  = new ThreadLocal();

    public static void startPage(PageQuery pageQuery) {
        if (!pageQuery.isPaged()) {
            return;
        }
        THREAD_LOCAL.set(PageHelper.startPage(pageQuery.getPage(), pageQuery.getPageSize()));
    }

    public static Page getPage() {
        return THREAD_LOCAL.get();
    }

    public static void clearPage() {
        PageHelper.clearPage();
        THREAD_LOCAL.remove();
    }
}
