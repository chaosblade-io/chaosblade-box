package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import jodd.typeconverter.TypeConverter;
import jodd.typeconverter.TypeConverterManager;
import lombok.experimental.UtilityClass;

/**
 * @author sunju
 *
 */
@UtilityClass
public class TypeUtil {

    public static <F, T> T convert(F object, Class<T> targetType) {
        if (null == object || object.getClass() == targetType) {
            return (T) object;
        }
        try {
            TypeConverter<T> converter = TypeConverterManager.get().lookup(targetType);
            if (null != converter) {
                return converter.convert(object);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
