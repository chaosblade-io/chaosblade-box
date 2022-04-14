package com.alibaba.chaosblade.box.cache.templates;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ChaosLocalCacheTemplate extends ChaosCacheTemplate {

    void put(String key, Serializable value) throws ChaosCacheException;

    void putAll(Map<String, Serializable> values) throws ChaosCacheException;

    Serializable get(String key) throws ChaosCacheException;

    Map<String, Serializable> getAll(Collection<String> keys) throws ChaosCacheException;

    void invalid(String key) throws ChaosCacheException;

    void invalidAll(Collection<String> keys) throws ChaosCacheException;

    void clear(String prefixKey) throws ChaosCacheException;

}
