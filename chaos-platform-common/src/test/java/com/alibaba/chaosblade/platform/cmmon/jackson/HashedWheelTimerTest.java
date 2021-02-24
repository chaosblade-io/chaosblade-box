package com.alibaba.chaosblade.platform.cmmon.jackson;

import com.alibaba.chaosblade.platform.cmmon.utils.timer.HashedWheelTimer;
import com.alibaba.chaosblade.platform.cmmon.utils.timer.Timer;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author yefei
 */
public class HashedWheelTimerTest {

    @Test
    public void delayTest() throws Exception {
        Timer timer = new HashedWheelTimer();
        long millis = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        int delay = 3000;
        timer.newTimeout(timeout -> {
            System.out.println("hello");
            Assert.assertTrue(System.currentTimeMillis() - millis > delay);
            countDownLatch.countDown();
        }, delay, TimeUnit.MILLISECONDS);
        countDownLatch.await();
    }
}
