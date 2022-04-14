package com.alibaba.chaosblade.box.dao.infrastructure.monitor.log;

import com.alibaba.chaosblade.box.common.infrastructure.monitor.log.ResultCodeRecorder;

/**
 * @author haibin
 *
 *
 */
public class RecordsRepo {

    private static ResultCodeRecorder resultCodeRecorder = new ResultCodeRecorder();

    private static MiniAppInvocationRecorder miniAppInvocationRecorder = new MiniAppInvocationRecorder();

    public static ResultCodeRecorder getResultCodeRecord() {
        return resultCodeRecorder;
    }

    public static MiniAppInvocationRecorder getMiniAppInvocationRecorder() {
        return miniAppInvocationRecorder;
    }

}
