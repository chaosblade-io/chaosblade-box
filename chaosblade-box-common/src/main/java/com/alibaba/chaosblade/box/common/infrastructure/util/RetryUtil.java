package com.alibaba.chaosblade.box.common.infrastructure.util;

import com.alibaba.chaosblade.box.common.common.enums.CommonErrorCode;
import com.alibaba.chaosblade.box.common.infrastructure.exception.ChaosException;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author sunju
 * @author haibin.lhb
 *
 */
@UtilityClass
public class RetryUtil {

    private static final int DEFAULT_RETRY_TIMES = 1;

    public static <V> V retryIfThrowsChaosException(Callable<V> callable) throws ChaosException {
        return retryIfThrowsChaosException(callable, DEFAULT_RETRY_TIMES);
    }

    public static <V> V retryIfThrowsChaosException(Callable<V> callable, int times) throws ChaosException {
        try {
            return RetryerBuilder.<V>newBuilder()
                .retryIfExceptionOfType(ChaosException.class)
                .withStopStrategy(StopStrategies.stopAfterAttempt(times))
                .build()
                .call(callable);
        } catch (ExecutionException | RetryException e) {
            throw new ChaosException(CommonErrorCode.S_SYSTEM_ERROR);
        }
    }



    public static boolean retryIfReturnFalse(Callable<Boolean> callable) throws ChaosException {
        return retryIfReturnFalse(callable, DEFAULT_RETRY_TIMES);
    }

    public static boolean retryIfReturnFalse(Callable<Boolean> callable, int times) throws ChaosException {
        try {
            return RetryerBuilder.<Boolean>newBuilder()
                .retryIfResult(Boolean.FALSE::equals)
                .withStopStrategy(StopStrategies.stopAfterAttempt(times))
                .build()
                .call(callable);
        } catch (RetryException e) {
            return false;
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ChaosException) {
                throw (ChaosException)cause;
            }
            throw new ChaosException(CommonErrorCode.S_SYSTEM_ERROR, e);
        }
    }

}
