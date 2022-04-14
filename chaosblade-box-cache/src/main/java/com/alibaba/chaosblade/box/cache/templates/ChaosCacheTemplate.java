package com.alibaba.chaosblade.box.cache.templates;

import com.alibaba.chaosblade.box.cache.ChaosCacheException;

public interface ChaosCacheTemplate {

    void clear(String prefixKey) throws ChaosCacheException;

}
