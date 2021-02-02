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

package com.alibaba.chaosblade.platform.http.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.chaosblade.platform.http.constant.Blade;

/**
 * @author Changjun Xiao
 */
public class CliUtil {

    public static final String CREATE = "create";
    public static final String DESTROY = "destroy";

    public static final String NODE_KIND = "node";
    public static final String POD_KIND = "pod";
    public static final String CONTAINER_KIND = "container";

    /**
     * 构建状态查询的命令
     *
     * @param uid
     * @param type
     * @return
     */
    public static String buildStatusCmd(String uid, String type) {
        StringBuilder sb = new StringBuilder(Blade.STATUS);
        sb.append(" ").append("--type").append(" ").append(type.trim());
        if (!StrUtil.isBlank(uid)) {
            sb.append(" ").append("--uid").append(" ").append(uid);
        }
        return sb.toString();
    }

    /**
     * 构建 query 命令, query TARGET FILED
     *
     * @param target
     * @param field
     * @return
     */
    public static String buildQueryCmd(String target, String field) {
        StringBuilder sb = new StringBuilder(Blade.QUERY);
        sb.append(" ").append(target).append(" ").append(field);
        return sb.toString();
    }
}
