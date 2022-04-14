package com.alibaba.chaosblade.box.dao.infrastructure.monitor.metric;

import com.alibaba.chaosblade.box.common.experiment.task.flow.util.Pair;
import com.alibaba.chaosblade.box.dao.model.ExperimentTaskDO;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author haibin
 *
 * 
 */
public class MetricUtil {

    public static Pair<String, String> getMetricDateRangeInTimeStamp(ExperimentTaskDO experimentTaskDO) {
        Date from = DateUtils.addMinutes(experimentTaskDO.getGmtCreate(), -5);
        Date to = new Date();
        if (experimentTaskDO.isFinished()) {
            to = DateUtils.addMinutes(experimentTaskDO.getGmtEnd(), 5);
        }
        return Pair.of(String.valueOf(from.getTime()), String.valueOf(to.getTime()));
    }
}
