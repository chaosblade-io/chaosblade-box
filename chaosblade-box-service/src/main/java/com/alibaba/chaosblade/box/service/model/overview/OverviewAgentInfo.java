package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

/**
 * @author sunpeng
 *
 *
 */
@Data
public class OverviewAgentInfo {

    /**
     * 探针总数
     */
    private Integer totalCount;

    /**
     * 在线数量
     */
    private Integer onlineCount;

    /**
     * 正常数量
     */
    private Integer normalCount;

    /**
     * 异常数量
     */
    private Integer errorCount;

    public OverviewAgentInfo() {
    }

    public OverviewAgentInfo(Integer totalCount, Integer onlineCount, Integer normalCount, Integer errorCount) {
        this.totalCount = totalCount;
        this.onlineCount = onlineCount;
        this.normalCount = normalCount;
        this.errorCount = errorCount;
    }
}
