package com.alibaba.chaosblade.box.common.experiment.request;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

/**
 * @author jiumu
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskRequest {

    String experimentId;

    Date startTime;

    Date endTime;

    /**
     * 按应用进行统计
     */
    Set<String> aoneApps;

    /**
     * 按类型进行统计
     */
    Set<String> appDescs;

    /**
     * 按结果进行统计
     */
    Set<ResultEnum> resultEnums;

}
