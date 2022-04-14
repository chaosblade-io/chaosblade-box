package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lambda-styled 'IF' syntax implementation.
 *
 * Use like:
 *
 * <code>
 *     IF.on(someObject)
 *     .when(obj -> {
 *         //do something
 *         return true; // or false
 *     })
 *     .isTrue(obj -> {
 *         //do something if true
 *     })
 *     .orElse(obj -> {
 *         //do something if false
 *     });
 * </code>
 *
 * @author sunju
 *
 */
public final class IF<T> {

    private final T object;

    private boolean isTrue;

    private IF(T object) {
        this.object = object;
    }

    public static <T> IF<T> on(T object) {
        return new IF<>(object);
    }

    public IF<T> when(Predicate<T> predicate) {
        isTrue = predicate.test(this.object);
        return this;
    }

    public IF<T> isTrue(Consumer<T> consumer) {
        if (isTrue) {
            consumer.accept(this.object);
        }
        return this;
    }

    public void orElse(Consumer<T> consumer) {
        if (!isTrue) {
            consumer.accept(this.object);
        }
    }

}
