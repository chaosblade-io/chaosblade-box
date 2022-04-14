package com.alibaba.chaosblade.box.common.infrastructure.util;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author haibin
 *
 *
 */
public class MethodInvokeUtil {

    public static void invokeWithStopWatch(ThrowExceptionRunnable runnable, Logger logger, String runnabeDesc)
        throws Exception {
        logger.info("Start {}", runnabeDesc);
        StopWatch stopWatch = StopWatch.createStarted();
        try {
            runnable.run();
        } finally {
            stopWatch.stop();
            logger.info("End {},cost {} in milliseconds", runnabeDesc, stopWatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    public interface ThrowableHandler<T> {
        public T handle(Throwable throwable);
    }

    public static <T> T invoke(Callable<T> callable, ThrowableHandler<T> tThrowableHandler) {
        try {
            return callable.call();
        } catch (Exception exception) {
            return tThrowableHandler.handle(exception);
        }
    }

}
