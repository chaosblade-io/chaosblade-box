package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author haibin
 *
 *
 */
public class HashMapSettings implements Settings {

    private Map<String, Object> maps;

    public HashMapSettings(HashMap<String, Object> hashMap) {
        this.maps = hashMap;
    }

    public HashMapSettings() {
        this.maps = new HashMap<>();
    }

    @Override
    public boolean containsKey(String key) {
        return this.maps.containsKey(key);
    }

    @Override
    public Float getAsFloat(String key) {
        String value = getAsString(key);
        if (value == null) { return null; }
        return Float.valueOf(value);
    }

    @Override
    public Float getAsFloat(String key, Float defaultValue) {
        Float value = getAsFloat(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public Integer getAsInteger(String key) {
        String value = getAsString(key);
        if (value == null) { return null; }
        return Integer.valueOf(value);
    }

    @Override
    public Integer getAsInteger(String key, Integer defaultValue) {
        Integer value = getAsInteger(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public String getAsString(String key, String defaultValue) {
        String value = getAsString(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public String getAsString(String key) {
        if (containsKey(key)) {
            return String.valueOf(maps.get(key));
        }
        return null;
    }

    @Override
    public Boolean getAsBoolean(String key, Boolean defaultValue) {
        Boolean value = getAsBoolean(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public <T> T getObject(String key, Class<T> tClass,
        Supplier<T> supplierIfNotExist) {
        T result = getObject(key, tClass);
        if (result == null) {
            result = supplierIfNotExist.get();
            add(key, result);
        }
        return result;
    }

    @Override
    public Boolean getAsBoolean(String key) {
        String value = getAsString(key);
        if (value == null) { return null; }
        return Boolean.valueOf(value);
    }

    @Override
    public <T> T getObject(String key, Class<T> tClass) {
        Object object = maps.get(key);
        if (object == null) { return null; }
        return (T)object;
    }

    @Override
    public void add(String key, Object value) {
        this.maps.put(key, value);
    }

    @Override
    public void remove(String key) {
        this.maps.remove(key);
    }
}
