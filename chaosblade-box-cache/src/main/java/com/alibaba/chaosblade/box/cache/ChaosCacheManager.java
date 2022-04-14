package com.alibaba.chaosblade.box.cache;

import com.alibaba.chaosblade.box.cache.templates.ChaosDistributedCacheTemplate;
import com.alibaba.chaosblade.box.cache.templates.ChaosLocalCacheTemplate;
import com.alibaba.chaosblade.box.cache.utils.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/11/7
 */
@Slf4j
public class ChaosCacheManager {

    private static final String DEFAULT_PREFIX_KEY = "chaos";

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
    @Autowired(required = false)
    private ChaosLocalCacheTemplate localCacheTemplate;

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
    @Autowired(required = false)
    private ChaosDistributedCacheTemplate distributedCacheTemplate;

    private final Executor syncWorker = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private final boolean autoSync;

    @Getter
    @Setter
    @Accessors(fluent = true)
    private boolean suppressException;

    public ChaosCacheManager(ChaosCacheProperties properties) {
        this.autoSync = properties.isAutoSync();
        this.suppressException = properties.isSuppressException();
    }

    public void put(String key, Serializable value) throws ChaosCacheException {
        this.prefixPut(DEFAULT_PREFIX_KEY, key, value);
    }

    public void prefixPut(String prefixKey, String key, Serializable value) throws ChaosCacheException {
        try {
            if (null != localCacheTemplate) {
                localCacheTemplate.put(wrap(prefixKey, key), value);
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.prefixPut(prefixKey, key, value);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Put failed.", e);
            handleThrowable(e);
        }
    }


    public void prefixPut(String prefixKey, String key, Serializable value, int expire) throws ChaosCacheException {
        try {
            if (null != localCacheTemplate) {
                localCacheTemplate.put(wrap(prefixKey, key), value);
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.prefixPut(prefixKey, key, value, expire);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Put failed.", e);
            handleThrowable(e);
        }
    }

    public void putAll(Map<String, Serializable> values) throws ChaosCacheException {
        this.prefixPutAll(DEFAULT_PREFIX_KEY, values);
    }

    public void prefixPutAll(String prefixKey, Map<String, Serializable> values) throws ChaosCacheException {
        try {
            Map<String, Serializable> entries = new HashMap<>();
            for (Map.Entry<String, Serializable> entry : values.entrySet()) {
                entries.put(wrap(prefixKey, entry.getKey()), entry.getValue());
            }

            if (null != localCacheTemplate) {
                localCacheTemplate.putAll(entries);
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.prefixPutAll(prefixKey, values);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Put all failed.", e);
            handleThrowable(e);
        }
    }

    public Serializable get(String key) throws ChaosCacheException {
        return this.prefixGet(DEFAULT_PREFIX_KEY, key);
    }

    public Serializable prefixGet(String prefixKey, String key) throws ChaosCacheException {
        try {
            String wrapperKey = wrap(prefixKey, key);
            Serializable value = null;

            if (null != localCacheTemplate) {
                value = localCacheTemplate.get(wrapperKey);
                if (null != value) {

                    sync(prefixKey, key);
                    return value;
                }
            }

            if (null != distributedCacheTemplate) {
                value = distributedCacheTemplate.prefixGet(prefixKey, key);
                writeBack(wrapperKey, value);
            }

            return value;
        } catch (Throwable e) {
            log.error("[Chaos Cache] Get failed.", e);
            handleThrowable(e);
        }

        return null;
    }

    public Map<String, Serializable> getAll(Collection<String> keys) throws ChaosCacheException {
        return this.prefixGetAll(DEFAULT_PREFIX_KEY, keys);
    }

    public Map<String, Serializable> prefixGetAll(String prefixKey, Collection<String> keys) throws ChaosCacheException {
        try {
            Collection<String> wrapperKeys = keys.stream()
                    .map(key -> wrap(prefixKey, key))
                    .collect(Collectors.toList());

            Map<String, Serializable> values = null;

            if (null != localCacheTemplate) {
                values = localCacheTemplate.getAll(wrapperKeys);
                if (null != values && !values.isEmpty()) {
                    Map<String, Serializable> entries = new HashMap<>();
                    for (Map.Entry<String, Serializable> entry : values.entrySet()) {
                        entries.put(unwrap(prefixKey, entry.getKey()), entry.getValue());
                    }

                    sync(prefixKey, keys);
                    return entries;
                }
            }

            if (null != distributedCacheTemplate) {
                values = distributedCacheTemplate.prefixGetAll(prefixKey, keys);
                writeBack(prefixKey, values);
            }

            return values;
        } catch (Throwable e) {
            log.error("[Chaos Cache] Get all failed.", e);
            handleThrowable(e);
        }

        return null;
    }

    public void invalid(String key) throws ChaosCacheException {
        this.prefixInvalid(DEFAULT_PREFIX_KEY, key);
    }

    public void prefixInvalid(String prefixKey, String key) throws ChaosCacheException {
        try {
            if (null != localCacheTemplate) {
                localCacheTemplate.invalid(wrap(prefixKey, key));
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.prefixInvalid(prefixKey, key);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Invalid failed.", e);
            handleThrowable(e);
        }
    }

    public void invalidAll(Collection<String> keys) throws ChaosCacheException {
        this.prefixInvalidAll(DEFAULT_PREFIX_KEY, keys);
    }

    public void prefixInvalidAll(String prefixKey, Collection<String> keys) throws ChaosCacheException {
        try {
            if (null != localCacheTemplate) {
                localCacheTemplate.invalidAll(keys.stream().map(key -> wrap(prefixKey, key)).collect(Collectors.toList()));
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.prefixInvalidAll(prefixKey, keys);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Invalid all failed.", e);
            handleThrowable(e);
        }
    }

    public void clear() throws ChaosCacheException {
        this.clear(DEFAULT_PREFIX_KEY);
    }

    public void clear(String prefixKey) throws ChaosCacheException {
        try {
            if (null != localCacheTemplate) {
                localCacheTemplate.clear(prefixKey);
            }
            if (null != distributedCacheTemplate) {
                distributedCacheTemplate.clear(prefixKey);
            }
        } catch (Throwable e) {
            log.error("[Chaos Cache] Clear failed.", e);
            handleThrowable(e);
        }
    }

    private void sync(String prefixKey, String key) {
        if (autoSync && null != distributedCacheTemplate) {
            syncWorker.execute(() -> {
                try {
                    Serializable value = distributedCacheTemplate.prefixGet(prefixKey, key);
                    String wrapperKey = wrap(prefixKey, key);
                    writeBack(wrapperKey, value);
                } catch (Exception e) {
                    log.error("[Chaos Cache][Auto Sync] Sync cache failed.", e);
                }
            });
        }
    }

    private void sync(String prefixKey, Collection<String> keys) {
        if (autoSync && null != distributedCacheTemplate) {
            syncWorker.execute(() -> {
                try {
                    Map<String, Serializable> values = distributedCacheTemplate.prefixGetAll(prefixKey, keys);
                    writeBack(prefixKey, values);
                } catch (Exception e) {
                    log.error("[Chaos Cache][Auto Sync] Sync cache failed.", e);
                }
            });
        }
    }

    private void writeBack(String key, Serializable value) {
        if (autoSync && null != localCacheTemplate && null != value) {
            syncWorker.execute(() -> {
                try {
                    localCacheTemplate.put(key, value);
                } catch (Exception e) {
                    log.error("[Chaos Cache][Auto Sync] Write-back cache failed.", e);
                }
            });
        }
    }

    private void writeBack(String prefixKey, Map<String, Serializable> values) {
        if (autoSync && null != localCacheTemplate && null != values) {
            syncWorker.execute(() -> {
                try {
                    Map<String, Serializable> entries = new HashMap<>();
                    for (Map.Entry<String, Serializable> entry : values.entrySet()) {
                        entries.put(wrap(prefixKey, entry.getKey()), entry.getValue());
                    }

                    localCacheTemplate.putAll(entries);
                } catch (Exception e) {
                    log.error("[Chaos Cache][Auto Sync] Write-back cache failed.", e);
                }
            });
        }
    }

    private String wrap(String prefixKey, String key) {
        return Strings.join(prefixKey, ":", key);
    }

    private String unwrap(String prefixKey, String key) {
        return key.replaceFirst(Strings.join(prefixKey, ":"), "");
    }

    private void handleThrowable(Throwable throwable) throws ChaosCacheException {
        if (!suppressException) {
            if (throwable instanceof ChaosCacheException) {
                throw (ChaosCacheException) throwable;
            }
            throw new ChaosCacheException(throwable);
        }
    }

}
