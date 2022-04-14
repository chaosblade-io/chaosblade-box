package com.alibaba.chaosblade.box.cache.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Author: sunju
 *
 * Date:   2019/11/8
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistributedCacheConfig {

    String name;
    int expireAfterAccess;
    int expireAfterWrite;

}
