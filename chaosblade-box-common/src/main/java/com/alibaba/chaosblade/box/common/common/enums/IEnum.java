package com.alibaba.chaosblade.box.common.common.enums;

/**
 * @author haibin
 *
 *
 */

import java.io.Serializable;

public interface IEnum<T extends Serializable> {

    /**
     * 枚举数据库存储值
     */
    T getValue();

}