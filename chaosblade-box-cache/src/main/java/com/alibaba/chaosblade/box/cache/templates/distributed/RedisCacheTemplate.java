package com.alibaba.chaosblade.box.cache.templates.distributed;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;
import com.alibaba.chaosblade.box.cache.ChaosCacheProperties;
import com.alibaba.chaosblade.box.cache.config.DistributedCacheConfig;
import com.alibaba.chaosblade.box.cache.templates.ChaosDistributedCacheTemplate;
import com.alibaba.chaosblade.box.cache.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author: sunju
 *
 * Date:   2019/11/8
 */
@Slf4j
@Component
public class RedisCacheTemplate implements ChaosDistributedCacheTemplate {

    private static final String PREFIX = "chaos:redis:";

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private final int expireAfterWrite;

    private final int expireAfterAccess;

    public RedisCacheTemplate(ChaosCacheProperties properties) {
        DistributedCacheConfig config = properties.getDistributedCache();

        expireAfterWrite = config.getExpireAfterWrite();
        expireAfterAccess = config.getExpireAfterAccess();
    }

    @Override
    public void prefixPut(String prefixKey, String key, Serializable value) {
        try {
            if (expireAfterWrite > 0) {
                redisTemplate.opsForValue().set(wrap(prefixKey, key), value, expireAfterWrite, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(wrap(prefixKey, key), value);
            }
        } catch (Exception e) {
            log.error("[RedisCacheTemplate] put error, key: {}, value: {}",wrap(prefixKey, key),value,e);
        }
    }

    @Override
    public void prefixPut(String prefixKey, String key, Serializable value, int expire) throws ChaosCacheException {
        try {
            redisTemplate.opsForValue().set(wrap(prefixKey, key), value, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[RedisCacheTemplate] put error, key: {}, value: {}",wrap(prefixKey, key),value,e);
        }
    }

    @Override
    public void prefixPutAll(String prefixKey, Map<String, Serializable> values) {
        redisTemplate.execute(new SessionCallback<List<Object>>() {

            @SuppressWarnings({"unchecked"})
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                for (Map.Entry<String, Serializable> entry : values.entrySet()) {
                    if (expireAfterWrite > 0) {
                        operations.opsForValue().set((K) wrap(prefixKey, entry.getKey()), (V) entry.getValue(), expireAfterWrite, TimeUnit.SECONDS);
                    } else {
                        operations.opsForValue().set((K) wrap(prefixKey, entry.getKey()), (V) entry.getValue());
                    }
                }

                return operations.exec();
            }

        });
    }

    @Override
    public Serializable prefixGet(String prefixKey, String key) {

        Serializable result = null;
        try {
            String wrapperKey = wrap(prefixKey, key);
            result = redisTemplate.opsForValue().get(wrapperKey);
            if (expireAfterAccess > 0) {
                redisTemplate.expire(wrapperKey, expireAfterAccess, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("[RedisCacheTemplate] get error, key: {}",wrap(prefixKey, key),e);
        }
        return result;
    }

    @Override
    public Map<String, Serializable> prefixGetAll(String prefixKey, Collection<String> keys) {
        List<Object> values = redisTemplate.execute(new SessionCallback<List<Object>>() {

            @SuppressWarnings({"unchecked"})
            @Override
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();

                for (String key : keys) {
                    String wrapperKey = wrap(prefixKey, key);
                    operations.opsForValue().get(wrapperKey);
                    if (expireAfterAccess > 0) {
                        operations.expire((K) wrapperKey, expireAfterAccess, TimeUnit.SECONDS);
                    }
                }

                return operations.exec();
            }

        });

        Map<String, Serializable> data = new HashMap<>();

        if (null != values && !values.isEmpty()) {
            int step = values.size() / keys.size();
            int i = 0;
            for (String key : keys) {
                data.put(key, (Serializable) values.get(i));
                i += step;
            }
        }

        return data;
    }

    @Override
    public void prefixInvalid(String prefixKey, String key) {
        redisTemplate.delete(wrap(prefixKey, key));
    }

    @Override
    public void prefixInvalidAll(String prefixKey, Collection<String> keys) throws ChaosCacheException {
        redisTemplate.delete(keys.stream().map(key -> wrap(prefixKey, key)).collect(Collectors.toList()));
    }

    @Override
    public void clear(String prefixKey) throws ChaosCacheException {
        Set<String> keys = redisTemplate.keys(wrap(prefixKey, "*"));
        redisTemplate.delete(keys);
    }

    private String wrap(String prefixKey, String key) {
        return Strings.join(PREFIX, prefixKey, ":", key);
    }

}
