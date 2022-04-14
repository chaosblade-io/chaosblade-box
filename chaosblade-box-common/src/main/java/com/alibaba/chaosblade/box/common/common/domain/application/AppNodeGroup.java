package com.alibaba.chaosblade.box.common.common.domain.application;

import lombok.Data;

import java.util.Map;

/**
 * @author haibin
 *
 *
 */
@Data
public class AppNodeGroup {

    private String nodeGroup;

    /**
     * 机房分布的机器数量
     */
    private Map<String, Long> rooms;

    /**
     * 单元分布
     */
    private Map<String, Long> units;

}
