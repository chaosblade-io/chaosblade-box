package com.alibaba.chaosblade.box.cache.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class CacheKeyBuilder {

    public static String buildCacheKey(String prefix, String... content) {
        Preconditions.checkArgument(StringUtils.isNotBlank(prefix));
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("::");

        for(int i = 0; i < content.length; ++i) {
            sb.append(content[i]);
            if (i < content.length - 1) {
                sb.append(":");
            }
        }

        return sb.toString();
    }
}
