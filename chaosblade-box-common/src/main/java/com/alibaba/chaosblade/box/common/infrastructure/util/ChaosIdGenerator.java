package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * @author haibin
 *
 *
 */
public final class ChaosIdGenerator {

    public static String generateId() {
        return IdWorker.getIdStr();
    }
}
