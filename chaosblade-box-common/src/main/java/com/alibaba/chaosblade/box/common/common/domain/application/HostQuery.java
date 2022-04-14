package com.alibaba.chaosblade.box.common.common.domain.application;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author haibin
 *
 *
 */
@Data
public class HostQuery implements Serializable {

    /**
     * 必须要有,aone应用
     */
    private String appName;

    /**
     * 分组名,必须要有
     */
    private String nodeGroup;

    /**
     * 机房名
     */
    private List<String> idcs = new ArrayList<>();

    /**
     * 单元名
     */
    private List<String> units = new ArrayList<>();

    /**
     * 机器过滤条件,必须要有
     */
    private HostFilter filter;

}
