package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosPersistenceException;

/**
 * @author haibin
 *
 *
 */
public final class ChaosPreconditions {

    public static void checkPersistence(boolean expression, Object errorMessage) {
        if (!expression) {
            throw new ChaosPersistenceException(String.valueOf(errorMessage));
        }
    }
}
