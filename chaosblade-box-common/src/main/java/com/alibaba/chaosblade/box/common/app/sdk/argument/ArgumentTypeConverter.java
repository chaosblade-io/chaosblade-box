package com.alibaba.chaosblade.box.common.app.sdk.argument;

/**
 * @author sunju
 *
 * @param <F> source type
 * @param <T> target type
 */
@FunctionalInterface
public interface ArgumentTypeConverter<F, T> {

    /**
     * convert type
     *
     * @param from instance of source type
     * @return instance of target type
     */
    T convert(F from);

}
