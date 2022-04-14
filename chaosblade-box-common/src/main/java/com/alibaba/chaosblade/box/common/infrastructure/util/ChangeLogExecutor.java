package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author haibin.lhb
 *
 *
 */
public class ChangeLogExecutor {

    public static <T> T executeWithChangeLog(Supplier<T> supplier, Consumer<T> consumer) {
        T t = supplier.get();
        try {
            consumer.accept(t);
        } catch (Exception ignored) {}
        return t;
    }

}
