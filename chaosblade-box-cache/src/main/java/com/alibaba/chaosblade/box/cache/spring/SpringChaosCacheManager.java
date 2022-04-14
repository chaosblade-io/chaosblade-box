package com.alibaba.chaosblade.box.cache.spring;

import com.alibaba.chaosblade.box.cache.ChaosCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.Collections;

/**
 * Author: sunju
 *
 * Date:   2019/11/8
 */
public class SpringChaosCacheManager extends AbstractCacheManager {

    @SuppressWarnings({"SpringJavaAutowiredMembersInspection"})
    @Autowired
    private ChaosCacheManager cacheManager;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Collections.emptyList();
    }

    @Override
    protected Cache getMissingCache(String name) {
        return new SpringChaosCache(name, this.cacheManager);
    }

}
