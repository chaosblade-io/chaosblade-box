package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author haibin
 *
 *
 */
public class ExtensionLoader {

    public static <T> List<T> loadByServiceLoader(Class<T> tClass, ClassLoader classLoader) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(tClass, classLoader);
        Iterator<T> iterator = serviceLoader.iterator();
        List<T> tList = new ArrayList<>();
        while (iterator.hasNext()) {
            tList.add(iterator.next());
        }
        return tList;
    }
}
