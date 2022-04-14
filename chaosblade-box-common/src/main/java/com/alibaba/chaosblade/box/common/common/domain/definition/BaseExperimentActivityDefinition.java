package com.alibaba.chaosblade.box.common.common.domain.definition;

import com.alibaba.chaosblade.box.common.common.annotation.ApiParam;
import com.alibaba.chaosblade.box.common.common.annotation.ApiParam.ApiOperation;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class BaseExperimentActivityDefinition {

    /**
     * 小程序code
     */
    @JSONField(name = "app_code")
    @ApiParam
    String appCode;

    /**
     * 需要人工确认标记,默认不需要,自动流程到下一个流程
     */
    @JSONField(name = "user_check")
    @ApiParam(required = false)
    boolean userCheck;

    /**
     * 当前节点失败后是否立刻终止演练,只在非手动推进才生效,true 为开启，false为关闭,
     */
    boolean interruptedIfFailed;

    /**
     * 失败容忍度,当下面的机器或者子任务运行失败占比超过指定值,当前节点才会认定为失败,值在[0-100]
     */
    int failedTolerance = 0;

    /**
     * 用户定义的活动名称
     */
    @ApiParam(required = false)
    String activityName;

    /**
     * 活动id,演练创建时候为空
     */
    @ApiParam(required = false, operation = ApiOperation.ANY)
    String activityId;

    /**
     * 运行的机器范围
     */
    @ApiParam
    List<Host> scope;

    /**
     * 是否自动同步scope配置
     */
    @ApiParam
    boolean sync;

    /**
     * 前置和后置暂停配置
     */
    ExperimentNodePausesDefinition pauses;

    /**
     * 活动的顺序
     */
    @ApiParam
    private Integer order;

    /**
     * 对应的恢复环节
     */
    private ExperimentActivityDefinition recoveryDefinition;

    /**
     * 如果当前小程序是裂变小程序，api接口不需要传入
     */
    @JSONField(serialize = false, deserialize = false)
    private String executableAppCode;

    /**
     * flowId
     */
    @JSONField(serialize = false, deserialize = false)
    private String flowId;

    /**
     * 是否必须
     */
    boolean required;
}
