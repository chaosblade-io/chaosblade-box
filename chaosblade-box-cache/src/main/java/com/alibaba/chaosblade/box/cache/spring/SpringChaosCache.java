package com.alibaba.chaosblade.box.cache.spring;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;
import com.alibaba.chaosblade.box.cache.ChaosCacheManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.io.Serializable;
import java.util.concurrent.Callable;


public class SpringChaosCache extends AbstractValueAdaptingCache {

    private final String name;

    private final ChaosCacheManager cacheManager;




    public SpringChaosCache(String name, ChaosCacheManager cacheManager) {
        super(false);
        this.name = name;
        this.cacheManager = cacheManager;
    }

    @Override
    protected Object lookup(Object key) {
        return cacheManager.prefixGet(getName(), determineCacheKey(key));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheManager;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = this.lookup(key);
        if (null != value) {
            return (T) value;
        }

        if (null == valueLoader) {
            throw new ChaosCacheException("ValueLoader cannot be null.");
        }

        try {
            T object = valueLoader.call();
            this.put(key, object);
            return object;
        } catch (Exception e) {
            throw new ChaosCacheException("Load value failed.", e);
        }
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = this.lookup(key);
        return SerializableValueWrapper.wrap(determineCacheValue(value));
    }

    @Override
    public void put(Object key, Object value) {
        this.cacheManager.prefixPut(getName(), determineCacheKey(key), determineCacheValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        this.put(key, value);
        return SerializableValueWrapper.wrap(determineCacheValue(value));
    }

    @Override
    public void evict(Object key) {
        this.cacheManager.prefixInvalid(getName(), determineCacheKey(key));
    }

    @Override
    public void clear() {
        this.cacheManager.clear(getName());
    }

    private String determineCacheKey(Object key) {
        if (key instanceof String) {
            return (String) key;
        }
        throw new ChaosCacheException("Only support java.lang.String typed key.");
    }

    private Serializable determineCacheValue(Object value) {
        if (value instanceof Serializable) {
            return (Serializable) value;
        }
        throw new ChaosCacheException("Only support java.io.Serializable typed value.");
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor(staticName = "wrap", access = AccessLevel.PRIVATE)
    private static class SerializableValueWrapper implements ValueWrapper {

        Serializable value;

        @Override
        public Serializable get() {
            return this.value;
        }

    }

}
