package com.alibaba.chaosblade.box.cache.templates.local;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;
import com.alibaba.chaosblade.box.cache.ChaosCacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.alibaba.chaosblade.box.cache.config.LocalCacheConfig;
import com.alibaba.chaosblade.box.cache.templates.ChaosLocalCacheTemplate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import net.openhft.chronicle.bytes.Bytes;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/11/7
 */
public class OffHeapMemoryCacheTemplate implements ChaosLocalCacheTemplate {

    private final Cache<String, Bytes> referenceCache;

    private final Lock lock = new ReentrantLock(true);

    public OffHeapMemoryCacheTemplate(ChaosCacheProperties properties) {
        LocalCacheConfig config = properties.getLocalCache();

        referenceCache = Caffeine.newBuilder()
                                 .maximumSize(defaultTo(config.getMaxSize(), 100L))
                                 .expireAfterAccess(defaultTo(config.getExpireAfterAccess(), 15 * 60L), TimeUnit.SECONDS)
                                 .expireAfterWrite(defaultTo(config.getExpireAfterWrite(), 15 * 60L), TimeUnit.SECONDS)
                                 .removalListener((RemovalListener<String, Bytes>) (key, value, cause) -> {
                                     try {
                                         lock.lock();

                                         if (null != value && value.refCount() > 0L) {
                                             value.release();
                                         }
                                     } finally {
                                         lock.unlock();
                                     }
                                 })
                                 .build();
    }

    @Override
    public void put(String key, Serializable value) throws ChaosCacheException {
        try {
            lock.lock();

            Bytes exist = referenceCache.getIfPresent(key);
            if (null != exist) {
                referenceCache.invalidate(key);
            }

            CacheEntity entity = CacheEntity.create(key, value);
            Bytes bytes = Bytes.allocateDirect(toBytes(entity));

            referenceCache.put(key, bytes);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<String, Serializable> values) throws ChaosCacheException {
        for (Map.Entry<String, Serializable> entry : values.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Serializable get(String key) throws ChaosCacheException {
        try {
            lock.lock();

            Bytes bytes = referenceCache.getIfPresent(key);
            if (null == bytes || bytes.length() == 0) {
                return null;
            }

            bytes.compact();

            byte[] byteArray = bytes.toByteArray();
            if (byteArray.length == 0) {
                return null;
            }

            CacheEntity entity = fromBytes(byteArray);
            if (null == entity) {
                return null;
            }

            return entity.value();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<String, Serializable> getAll(Collection<String> keys) throws ChaosCacheException {
        Map<String, Serializable> values = new HashMap<>();
        for (String key : keys) {
            Serializable value = this.get(key);
            if (null != value) {
                values.put(key, value);
            }
        }
        return values;
    }

    @Override
    public void invalid(String key) {
        referenceCache.invalidate(key);
    }

    @Override
    public void invalidAll(Collection<String> keys) {
        keys.forEach(this::invalid);
    }

    @Override
    public void clear(String prefixKey) throws ChaosCacheException {
        List<String> keys = referenceCache.asMap()
                                          .keySet()
                                          .stream()
                                          .filter(key -> key.startsWith(prefixKey))
                                          .collect(Collectors.toList());

        referenceCache.invalidateAll(keys);
    }

    private long defaultTo(long value, long defaultValue) {
        if (value == 0L) {
            return defaultValue;
        }
        return value;
    }

    private byte[] toBytes(Object object) throws ChaosCacheException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ChaosCacheException("Read object failed.", e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private <T> T fromBytes(byte[] bytes) throws ChaosCacheException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ChaosCacheException("Write the object failed.", e);
        }
    }

    @Getter(value = AccessLevel.PRIVATE)
    @Accessors(fluent = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor(staticName = "create", access = AccessLevel.PRIVATE)
    private static class CacheEntity implements Serializable {

        String key;
        Serializable value;

    }

}