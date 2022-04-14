package com.alibaba.chaosblade.box.common.experiment.task.flow.util;

import java.io.InputStream;

/**
 * @author haibin
 *
 * 
 */
public final class IOUtils {

    public static void closeInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        } catch (Throwable ignore) {

        }
    }

}
