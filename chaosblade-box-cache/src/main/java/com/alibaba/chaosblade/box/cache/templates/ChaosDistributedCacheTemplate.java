package com.alibaba.chaosblade.box.cache.templates;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface ChaosDistributedCacheTemplate extends ChaosCacheTemplate {

    void prefixPut(String prefixKey, String key, Serializable value) throws ChaosCacheException;

    /**
     * 设置key失效时间 单位秒
     * @param prefixKey
     * @param key
     * @param value
     * @param expire
     * @throws ChaosCacheException
     */
    void prefixPut(String prefixKey, String key, Serializable value, int expire) throws ChaosCacheException;

    void prefixPutAll(String prefixKey, Map<String, Serializable> values) throws ChaosCacheException;

    Serializable prefixGet(String prefixKey, String key) throws ChaosCacheException;

    Map<String, Serializable> prefixGetAll(String prefixKey, Collection<String> keys) throws ChaosCacheException;

    void prefixInvalid(String prefixKey, String key) throws ChaosCacheException;

    void prefixInvalidAll(String prefixKey, Collection<String> keys) throws ChaosCacheException;

}
