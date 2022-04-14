package com.alibaba.chaosblade.box.service.model.overview;

import lombok.Data;

/**
 * @author sunpeng
 * 
 *
 */
@Data
public class OverviewPayPackInfo {

    /**
     * 资源包名称
     */
    private String payPackName;

    /**
     * 资源包过期时间
     */
    private String expiredTime;

    /**
     * 资源包空间总数
     */
    private Integer workspaceTotalCount;

    /**
     * 资源包剩余空间数
     */
    private Integer workspaceCount;

    /**
     * 演练次数总数
     */
    private Integer totalCount;

    /**
     * 演练次数剩余数
     */
    private Integer remainingCount;

    /**
     * 支持演练对象数量
     */
    private Integer targetCount;


}
