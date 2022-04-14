package com.alibaba.chaosblade.box.common.common.util;


import com.alibaba.chaosblade.box.common.infrastructure.util.resources.ResourceBundleHolder;

/**
 * @author haibin.lhb
 *
 * 
 */
public class Messages {
    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    public static String getMessage(String key) {
        try {
            return holder.format(key);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getMessage(String key, String defaultValue) {
        try {
            return holder.format(key);
        } catch (Exception exception) {
            return defaultValue;
        }
    }
}
