package com.alibaba.chaosblade.box.cache.templates.local;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;
import com.alibaba.chaosblade.box.cache.ChaosCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.alibaba.chaosblade.box.cache.config.LocalCacheConfig;
import com.alibaba.chaosblade.box.cache.templates.ChaosLocalCacheTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/11/7
 */
public class HeapCacheTemplate implements ChaosLocalCacheTemplate {

    private final Cache<String, Serializable> cache;

    public HeapCacheTemplate(ChaosCacheProperties properties) {
        LocalCacheConfig config = properties.getLocalCache();

        cache = Caffeine.newBuilder()
                        .maximumSize(defaultTo(config.getMaxSize(), 100L))
                        .expireAfterAccess(defaultTo(config.getExpireAfterAccess(), 15 * 60L), TimeUnit.SECONDS)
                        .expireAfterWrite(defaultTo(config.getExpireAfterWrite(), 15 * 60L), TimeUnit.SECONDS)
                        .build();
    }

    @Override
    public void put(String key, Serializable value) {
        cache.put(key, value);
    }

    @Override
    public void putAll(Map<String, Serializable> values) {
        cache.putAll(values);
    }

    @Override
    public Serializable get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public Map<String, Serializable> getAll(Collection<String> keys) {
        return cache.getAllPresent(keys);
    }

    @Override
    public void invalid(String key) {
        cache.invalidate(key);
    }

    @Override
    public void invalidAll(Collection<String> keys) {
        cache.invalidateAll(keys);
    }

    @Override
    public void clear(String prefixKey) throws ChaosCacheException {
        List<String> keys = cache.asMap()
                                 .keySet()
                                 .stream()
                                 .filter(key -> key.startsWith(prefixKey))
                                 .collect(Collectors.toList());

        cache.invalidateAll(keys);
    }

    private long defaultTo(long value, long defaultValue) {
        if (value == 0L) {
            return defaultValue;
        }
        return value;
    }

}
