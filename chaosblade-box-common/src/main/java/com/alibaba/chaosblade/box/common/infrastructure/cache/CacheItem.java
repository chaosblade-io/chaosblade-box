package com.alibaba.chaosblade.box.common.infrastructure.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * @author haibin
 *
 *
 */
@Data
public class CacheItem<T> implements Serializable {

    private T data;
}
