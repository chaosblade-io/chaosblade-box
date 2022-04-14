package com.alibaba.chaosblade.box.common.infrastructure.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class WaitUtil {

    public static boolean waitFor(Predicate<Void> predicate, long time, TimeUnit timeUnit) {
        Date startTime = new Date();
        while ((System.currentTimeMillis() - startTime.getTime()) < timeUnit.toMillis(time)) {
            if (predicate.test(null)) {
                return true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
        return predicate.test(null);

    }
    public static <T> T waitFor(Supplier<T> supplier, Predicate<T> predicate, long time, TimeUnit timeUnit) {
        Date startTime = new Date();
        T result = supplier.get();
        while ((System.currentTimeMillis() - startTime.getTime()) < timeUnit.toMillis(time)) {
            if (predicate.test(result)) {
                return result;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
            result = supplier.get();
        }
        return result;
    }

    public static <T> T waitFor(Supplier<T> supplier, Predicate<T> predicate, long time, TimeUnit timeUnit, long interval) {
        Date startTime = new Date();
        T result = supplier.get();
        while ((System.currentTimeMillis() - startTime.getTime()) < timeUnit.toMillis(time)) {
            if (predicate.test(result)) {
                return result;
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
            }
            result = supplier.get();
        }
        return result;
    }

}
