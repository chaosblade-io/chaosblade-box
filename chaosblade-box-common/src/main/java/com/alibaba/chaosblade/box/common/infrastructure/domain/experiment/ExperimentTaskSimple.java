package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment;

import com.alibaba.chaosblade.box.common.common.enums.ResultEnum;
import com.alibaba.chaosblade.box.common.common.enums.StateEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author jiumu
 * 
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperimentTaskSimple implements Serializable {

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
     * 结束时间
     */
    Date endTime;

    /**
     * 运行状态
     */
    StateEnum state;

    /**
     * 结果
     */
    ResultEnum result;

    /**
     * 任务类型
     */
    String type;

    /**
     * 演练错误信息
     */
    String message;

    /**
     * 演练中涉及的aone应用名列表
     */
    Set<String> aoneApps;

    /**
     * 演练中涉及的主机地址
     */
    Set<String> hostIps;

    /**
     * 演练中涉及的小程序类型信息
     */
    Set<String> appDescs;

}
