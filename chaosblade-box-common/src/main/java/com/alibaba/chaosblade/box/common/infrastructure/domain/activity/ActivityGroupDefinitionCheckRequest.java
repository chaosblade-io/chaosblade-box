package com.alibaba.chaosblade.box.common.infrastructure.domain.activity;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import com.alibaba.chaosblade.box.common.common.domain.BaseRequest;
import com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow.MiniFlow;
import lombok.Data;

import java.util.List;

/**
 * @author haibin.lhb
 *
 * 
 */
@Data
public class ActivityGroupDefinitionCheckRequest extends BaseRequest {

    private List<Host> hosts;

    private List<MiniFlow> flows;

    /**
     * 顺序
     */
    private int order;

    /**
     * 组件是否可以删除
     */
    private boolean required;

    /**
     * 机器类型
     */
    private Integer scopeType;

    /**
     * appName
     */
    private String appName;

    private String appId;

    private Integer appType;

    private List<String> appGroups;
}
