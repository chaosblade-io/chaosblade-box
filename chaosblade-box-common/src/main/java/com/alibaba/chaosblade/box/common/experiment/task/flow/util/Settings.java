package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.function.Supplier;

/**
 * @author haibin
 *
 *
 */
public interface Settings {

    public boolean containsKey(String key);

    public Float getAsFloat(String key);

    public Float getAsFloat(String key, Float defaultValue);

    public Integer getAsInteger(String key);

    public Integer getAsInteger(String key, Integer defaultValue);

    public String getAsString(String key, String defaultValue);

    public String getAsString(String key);

    public Boolean getAsBoolean(String key, Boolean defaultValue);

    public <T> T getObject(String key, Class<T> tClass, Supplier<T> supplierIfNotExist);

    public Boolean getAsBoolean(String key);

    public <T> T getObject(String key, Class<T> tClass);

    public void add(String key, Object value);

    public void remove(String key);

}


