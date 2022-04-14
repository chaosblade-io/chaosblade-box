package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import com.alibaba.chaosblade.box.common.experiment.task.flow.exception.ChaosAppException;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author sunju
 */
@UtilityClass
public class ReflectUtil {

    public static Method findMethodSilently(Class clazz, String methodName) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.getName().equals(methodName))
            .findFirst()
            .orElse(null);
    }

    public static Field[] getAllDeclaredFields(Class<?> sub, Class<?> root) {
        List<Field> allFields = Lists.newArrayList();

        getAllDeclaredFields(sub, root, allFields);

        Field[] fields = new Field[allFields.size()];
        return allFields.toArray(fields);
    }

    private static void getAllDeclaredFields(Class<?> sub, Class<?> root, List<Field> allFields) {
        Field[] fields = sub.getDeclaredFields();
        allFields.addAll(Arrays.asList(fields));

        if (sub != root && root.isAssignableFrom(sub)) {
            getAllDeclaredFields(sub.getSuperclass(), root, allFields);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ChaosAppException("Instantiation failed for class[" + clazz + "], please make sure class has no arguments constructor.", e);
        }
    }

    public static void invokeSetterByFieldNameSilently(Object object, String fieldName, Object value) {
        try {
            Method method = getSetter(object.getClass(), fieldName, value);

            method.invoke(object, value);

        } catch (NoSuchMethodException e) {
            try {
                Field field = object.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                field.set(object, value);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public static Method getGetter(Class<?> type, String fieldName) throws NoSuchMethodException {
        Method method;

        try {
            method = type.getMethod(getGetterName(fieldName));
        } catch (NoSuchMethodException e) {
            method = type.getMethod(getGetterNameForBoolean(fieldName));
        }
        return method;
    }

    public static Method getSetter(Class<?> type, String fieldName, Object value) throws NoSuchMethodException {
        Method method;

        try {
            method = type.getMethod(getSetterName(fieldName), value.getClass());
        } catch (NoSuchMethodException e) {
            if (Primitives.isWrapperType(value.getClass())) {
                method = type.getMethod(getSetterName(fieldName), Primitives.unwrap(value.getClass()));
            }
            else if (Primitives.allPrimitiveTypes().contains(value.getClass())) {
                method = type.getMethod(getSetterName(fieldName), Primitives.wrap(value.getClass()));
            }
            else {
                throw e;
            }
        }
        return method;
    }

    private static String getGetterName(String fieldName) {
        return "get" + convertFirstCharToUpperCase(fieldName);
    }

    private static String getGetterNameForBoolean(String fieldName) {
        return "is" + convertFirstCharToUpperCase(fieldName);
    }

    private static String getSetterName(String fieldName) {
        return "set" + convertFirstCharToUpperCase(fieldName);
    }

    private static String convertFirstCharToUpperCase(String string) {
        char first = string.charAt(0);
        return string.replaceFirst(String.valueOf(first), String.valueOf(Character.toUpperCase(first)));
    }

}
