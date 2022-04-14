package com.alibaba.chaosblade.box.common.infrastructure.monitor.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author haibin
 *
 *
 */
public class ResultCodeRecorder implements IRecord<ResultCodeRecord> {

    private final Logger logger = LoggerFactory.getLogger("request_trace_log");

    @Override
    public void record(ResultCodeRecord recordObject) {
        logger.info("&|{}|&|{}|&|{}|&|{}|&|{}|&|{}|&|{}|&|{}|&|{}", recordObject.getTraceId(),
            recordObject.getEntrance(),
            recordObject.getSuccess(),
            recordObject.getCodeState(), recordObject.getCode(),
            recordObject.getSource(), recordObject.getCost(), recordObject.getUserId(),
            recordObject.getExtraInfo().toString());
    }

}
