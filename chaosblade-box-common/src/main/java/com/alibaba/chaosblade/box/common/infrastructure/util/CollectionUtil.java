package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.util.*;

/**
 * @author haibin
 *
 *
 */
@UtilityClass
public final class CollectionUtil {

    public static boolean isNullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <E> E[] toArray(Collection<E> collection) {
        if (isNullOrEmpty(collection)) {
            return null;
        }
        return (E[])collection.toArray();
    }

    public static List<String> splitStrings(String str, String splitFlag) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        return Lists.newArrayList(Splitter.on(splitFlag).omitEmptyStrings().split(str));
    }

    public static <E> List<E> findCommonElements(Collection<List<E>> lists) {
        if (lists.isEmpty()) { return new ArrayList<>(); }
        Iterator<List<E>> it = lists.iterator();
        List<E> commonElements = new ArrayList<E>(it.next());
        while (it.hasNext()) {
            commonElements.retainAll(it.next());
        }
        return commonElements;
    }

    /**
     * [1,2,3,4], [1,2,5,6] => [1,2]
     */
    public static <E> List<E> intersection(List<E> c1, List<E> c2) {
        List<E> result = Lists.newArrayList();
        for (E e1 : c1) {
            for (E e2 : c2) {
                if (Objects.equals(e1, e2)) {
                    result.add(e1);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * [1,2,3,4], [1,2,5,6] => [3,4]
     */
    public static <E> List<E> difference(List<E> c1, List<E> c2) {
        List<E> result = Lists.newArrayList();
        for (E e1 : c1) {
            boolean equals = false;

            for (E e2 : c2) {
                if (Objects.equals(e1, e2)) {
                    equals = true;
                    break;
                }
            }

            if (!equals) {
                result.add(e1);
            }
        }
        return result;
    }

    public static <E> boolean deepEquals(List<E> c1, List<E> c2) {
        if (null == c1 && null == c2) {
            return true;
        }
        if (null != c1 && null != c2 && c1.size() == c2.size()) {
            return difference(c1, c2).isEmpty() && difference(c2, c1).isEmpty();
        }
        return false;
    }

}
