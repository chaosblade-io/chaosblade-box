package com.alibaba.chaosblade.box.common.infrastructure.domain.experiment.flow;

import com.alibaba.chaosblade.box.common.app.sdk.scope.Host;
import lombok.Data;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class MiniFlowGroup {

    private String groupId;

    private String groupName;

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

    private String appName;

    private String appId;

    private Integer appType;

    private List<String> appGroups;

    /**
     * 机器选择方式
     * {@link HostSelectTypes}
     */
    private Integer selectType;

    /**
     * 百分比
     */
    private Integer hostPercent;

    /**
     * 云服务类型
     */
    private String cloudServiceType;

    /**
     * 云服务类型名称
     */
    private String cloudServiceName;

    /**
     * 机器操作系统类型
     */
    private Integer osType;

}
