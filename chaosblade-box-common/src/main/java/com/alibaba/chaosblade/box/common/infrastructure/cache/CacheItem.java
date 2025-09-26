package com.alibaba.chaosblade.box.common.infrastructure.cache;

import java.io.Serializable;
import lombok.Data;

/** @author haibin */
@Data
public class CacheItem<T> implements Serializable {

  private T data;
}
