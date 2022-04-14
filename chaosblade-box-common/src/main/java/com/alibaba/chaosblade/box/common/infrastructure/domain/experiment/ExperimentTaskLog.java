package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author jiumu
 *
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskLog {

    /**
     * 演练名字
     */
    String experimentName;

    /**
     * 演练ID
     */
    String experimentId;

    /**
     * 演练任务Id
     */
    String taskId;

    /**
     * 演练开始时间
     */
    Date startTime;

    /**
     * 结束事件
     */
    Date endTime;

    /**
     * 演练执行人
     */
    String userName;

    /**
     * 结果
     */
    ResultEnum result;

    /**
     * 演练日志
     */
    Map<Long, String> logs;

    /**
     * 演练中涉及的aone应用名列表
     */
    Set<String> aoneApps;

}
